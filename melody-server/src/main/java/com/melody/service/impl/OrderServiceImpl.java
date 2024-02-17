package com.melody.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.melody.config.WechatConfiguration;
import com.melody.context.BaseContext;
import com.melody.dto.OrderPaymentDTO;
import com.melody.dto.OrderSubmitDTO;
import com.melody.entity.Orders;
import com.melody.exception.BaseException;
import com.melody.mapper.OrderMapper;
import com.melody.properties.WeChatProperties;
import com.melody.service.OrderService;
import com.melody.service.StudentService;
import com.melody.vo.*;
import com.wechat.pay.java.core.exception.HttpException;
import com.wechat.pay.java.core.exception.MalformedMessageException;
import com.wechat.pay.java.core.exception.ServiceException;
import com.wechat.pay.java.core.exception.ValidationException;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayResponse;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    StudentService studentService;

    @Autowired
    WechatConfiguration wechatConfiguration;

    @Autowired
    WeChatProperties weChatProperties;

    @Autowired
    OrderMapper orderMapper;

    /**
     * 学生下单
     * @param orderSubmitDTO
     * @return
     */
    public OrderSubmitVO submit(OrderSubmitDTO orderSubmitDTO) {
        //获取学生ID、手机号码、用户名称
        Long studnetId = BaseContext.getCurrentId();
        StudentVO studentVO = studentService.query();
        String phone = studentVO.getPhone();
        String username = studentVO.getUsername();
        //生成新订单
        Orders orders = new Orders();
        BeanUtils.copyProperties(orderSubmitDTO,orders);
        log.info("orders:{}",orders);
        //填充信息
        orders.setOrderNumber(String.valueOf(System.currentTimeMillis()));
        orders.setStudentId(studnetId);
        orders.setGoodsName("微信点评");
        orders.setStatus(Orders.PENDING_PAYMENT);
        orders.setOrderTime(LocalDateTime.now());
        orders.setPayMethod(Orders.WECHAT_PAY);
        orders.setPayStatus(Orders.UN_PAID);
        orders.setUsername(username);
        orders.setPhone(phone);
        log.info("填充后的数据:{}",orders);
        //并插入数据库表
        Long id = orderMapper.submitOrder(orders);

        //绑定班级作业与订单,并插入班级作业订单表
        Long classHomeworkId = orderSubmitDTO.getClassHomeworkId();
        orderMapper.appendHomeworkOrders(classHomeworkId,id);

        //封装VO返回结果
        OrderSubmitVO orderSubmitVO = OrderSubmitVO.builder()
                .id(id)
                .orderNumber(orders.getOrderNumber())
                .amount(orders.getAmount())
                .orderTime(orders.getOrderTime())
                .build();

        return orderSubmitVO;

    }

    /**
     * 学生查询全部订单 - 通过学生ID(线程获取)
     * @return
     */
    public List<OrderQueryVO> queryOrdersByStudentId() {
        //获取studentId
        Long studentId = BaseContext.getCurrentId();
        //利用studentId到数据库中查询
        List<OrderQueryVO> list = orderMapper.queryOrdersByStudentId(studentId);
        return list;
    }

    /**
     * 学生获取具体订单信息
     * @return
     */
    public OrderVO queryByOrderId(Long id) {
        OrderVO orderVO = orderMapper.queryByOrderId(id);
        return orderVO;
    }

    /**
     * 学生支付订单
     * @param orderPaymentDTO
     * @return
     */
    public OrderPaymentVO payment(OrderPaymentDTO orderPaymentDTO){
        log.info("支付订单");
        //获取openid
        //TODO:如果openid为空的异常处理
        Long id = BaseContext.getCurrentId();
        String openid = studentService.getOpenIdByStudentId(id);
        if(openid == null){
            throw new BaseException("openid为空");
        }
        log.info("学生openid为:{}",openid);
        //将信息转化为orders,传给wechatConfiguration
        Orders order = new Orders();
        BeanUtils.copyProperties(orderPaymentDTO,order);

        //查询数据库表,获取商品名称和商品金额
        int amount = orderMapper.getAmountByOrderNumber(order.getOrderNumber());
        String goodsName = orderMapper.getGoodsNameByOrderNumber(order.getOrderNumber());
        //填充相应数据
        order.setGoodsName(goodsName);
        order.setAmount(amount);

        log.info("调用统一下单API");
        //创建返回类
        OrderPaymentVO orderPaymentVO = new OrderPaymentVO();

        try {
            //调用统一下单API
            PrepayResponse response = wechatConfiguration.prepay(order, openid);
            String prepayId = response.getPrepayId();
            log.info("prepayId:{}",prepayId);
            if(prepayId != null){
                JSONObject jsonObject = wechatConfiguration.pay(prepayId);
                //TODO:订单已支付判断
                OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
                vo.setPackageStr(jsonObject.getString("package"));
                log.info("或者这里");
                return vo;
            }
        } catch (HttpException e) { // 发送HTTP请求失败
            // 调用e.getHttpRequest()获取请求打印日志或上报监控，更多方法见HttpException定义
            throw new BaseException("http请求发送失败,请检查网络");
        } catch (ServiceException e) { // 服务返回状态小于200或大于等于300，例如500
            // 调用e.getResponseBody()获取返回体打印日志或上报监控，更多方法见ServiceException定义
            throw new BaseException("服务返回失败,请检查网络");
        } catch (MalformedMessageException e) { // 服务返回成功，返回体类型不合法，或者解析返回体失败
            // 调用e.getMessage()获取信息打印日志或上报监控，更多方法见MalformedMessageException定义
            throw new BaseException("服务返回成功,但返回类型不合法,请联系后端人员处理");
        } catch (ValidationException e){
            throw new BaseException("验证微信支付签名失败");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //TODO:添加异常处理
        return null;
    }

    /**
     * 支付成功后,调用函数,修改相关订单信息
     * @param orderNumber
     */
    public void paySuccess(String orderNumber) {
        log.info("支付成功后,调用paySuccess方法,修改订单状态");
        Orders orders = new Orders();
        orders.setOrderNumber(orderNumber);
        orders.setStatus(Orders.TO_BE_EVALUATED);
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setPayStatus(Orders.PAID);

        //修改
        orderMapper.update(orders);
    }


}
