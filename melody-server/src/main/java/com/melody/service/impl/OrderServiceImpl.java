package com.melody.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.melody.config.WechatConfiguration;
import com.melody.context.BaseContext;
import com.melody.dto.OrderPaymentDTO;
import com.melody.dto.OrderSubmitDTO;
import com.melody.entity.ClassHomework;
import com.melody.entity.Homework;
import com.melody.entity.Orders;
import com.melody.exception.BaseException;
import com.melody.mapper.HomeworkMapper;
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

    @Autowired
    HomeworkMapper homeworkMapper;

    /**
     * 学生下单
     * @param orderSubmitDTO
     * @return
     */
    public OrderSubmitVO submit(OrderSubmitDTO orderSubmitDTO) {
        //TODO:判断是否有已存在订单,有则返回,无则生成(通过传递的班级作业id判断)
        Long ordersId = orderMapper.getOrdersIdByHomeworkId(orderSubmitDTO.getClassHomeworkId());

        Orders orders = new Orders();
        OrderSubmitVO orderSubmitVO;

        if(ordersId == null){
            //重新生成
            //获取学生ID、手机号码、用户名称
            log.info("未查询到该作业具有订单,重新生成");
            Long studnetId = BaseContext.getCurrentId();
            StudentVO studentVO = studentService.query();
            String phone = studentVO.getPhone();
            String username = studentVO.getUsername();
            //生成新订单
            BeanUtils.copyProperties(orderSubmitDTO,orders);
            //填充信息
            String orderNumber = String.valueOf(System.currentTimeMillis());
            orders.setOrderNumber(orderNumber);
            orders.setStudentId(studnetId);
            orders.setGoodsName("微信点评");
            orders.setStatus(Orders.PENDING_PAYMENT);
            orders.setOrderTime(LocalDateTime.now());
            orders.setPayMethod(Orders.WECHAT_PAY);
            orders.setPayStatus(Orders.UN_PAID);
            orders.setUsername(username);
            orders.setPhone(phone);
            log.info("生成新订单,填充后的数据:{}",orders);
            //主键回显出现问题,改用其他方式获取主键id
            orderMapper.submitOrder(orders);
            Long id = orderMapper.getIdByOrderNumber(orderNumber);
            log.info("新生成的订单ID:{}",id);
            //绑定班级作业与订单,并插入班级作业订单表
            Long classHomeworkId = orderSubmitDTO.getClassHomeworkId();
            orderMapper.appendHomeworkOrders(classHomeworkId,id);
            orderSubmitVO = OrderSubmitVO.builder()
                    .id(id)
                    .orderNumber(orders.getOrderNumber())
                    .amount(orders.getAmount())
                    .orderTime(orders.getOrderTime())
                    .build();
        }else {
            //已经存在,可从数据库直接获取
            log.info("已存在该作业对应的订单");
            OrderVO orderVO = orderMapper.queryByOrderId(ordersId);
            orderSubmitVO = OrderSubmitVO.builder()
                    .id(orderVO.getId())
                    .orderNumber(orderVO.getOrderNumber())
                    .amount(orderVO.getAmount())
                    .orderTime(orderVO.getOrderTime())
                    .build();
        }

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
     */
    public OrderVO queryByOrderId(Long id) {
        OrderVO orderVO = orderMapper.queryByOrderId(id);
        return orderVO;
    }

    /**
     * 学生支付订单
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
     * 支付成功后,调用该函数,修改相关订单信息
     * @param orderNumber 商户自定义订单号
     */
    public void paySuccess(String orderNumber) {
        log.info("支付成功,修改订单状态");
        Orders orders = new Orders();
        orders.setOrderNumber(orderNumber);
        orders.setStatus(Orders.TO_BE_EVALUATED);
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setPayStatus(Orders.PAID);
        //更新订单状态
        orderMapper.update(orders);

        log.info("支付成功,修改班级作业表状态");
        //获取订单id
        Long ordersId = orderMapper.getIdByOrderNumber(orderNumber);
        //根据订单id查询获取班级作业id
        Long homeworkId = orderMapper.getHomeworkIdByOrdersId(ordersId);
        //修改班级作业中completed状态
        //先查询是否有评级
        String grade = homeworkMapper.getGradeByClassHomeworkId(homeworkId);
        ClassHomework classHomework = new ClassHomework();
        //为空,将completed设置为 3(已提交作业/已付款/未评级/未评分)
        if(grade == null){
            classHomework.setCompleted(3);
            homeworkMapper.update(classHomework);
        }else {
            //不为空,将completed设置为 4(已提交作业/已付款/已评级/未评分)
            classHomework.setCompleted(4);
            homeworkMapper.update(classHomework);
        }

    }


}
