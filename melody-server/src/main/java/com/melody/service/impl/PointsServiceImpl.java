package com.melody.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.melody.config.WechatConfiguration;
import com.melody.context.BaseContext;
import com.melody.dto.PointsPurchaseOrderPaymentDTO;
import com.melody.dto.PointsPurchaseOrderSubmitDTO;
import com.melody.dto.RatingExchangeDTO;
import com.melody.entity.ClassHomework;
import com.melody.entity.Orders;
import com.melody.exception.BaseException;
import com.melody.mapper.HomeworkMapper;
import com.melody.mapper.OrderMapper;
import com.melody.mapper.PointsMapper;
import com.melody.service.PointsService;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
public class PointsServiceImpl implements PointsService {

    @Autowired
    PointsMapper pointsMapper;

    @Autowired
    StudentService studentService;

    @Autowired
    HomeworkMapper homeworkMapper;

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    WechatConfiguration wechatConfiguration;

    /**
     *  查询获取当前剩余积分点数
     * @return 剩余积分点
     */
    public LeftoverPointsVO queryLeftoverPointsVO() {
        //获取当前学生ID
        Long studentId = BaseContext.getCurrentId();
        log.info("当前学生ID：{}",studentId);
        Long points = pointsMapper.queryLeftoverPointsById(studentId);
        LeftoverPointsVO leftoverPointsVO = LeftoverPointsVO.builder()
                .studentId(studentId)
                .points(points)
                .build();
        log.info("当前学生剩余点数：{}",leftoverPointsVO);
        return leftoverPointsVO;
    }

    /**
     * 学生下单购买积分
     * @return 返回生产的订单信息，让用户进一步确认
     */
    @Transactional
    public PointsOrderSubmitVO submitPointsOrder(PointsPurchaseOrderSubmitDTO pposDTO) {
        //创建订单，用于插入数据库
        Orders orders = new Orders();

        Long studentId = BaseContext.getCurrentId();
        log.info("当前学生ID:{}",studentId);

        StudentVO studentVO = studentService.query();
        log.info("获取当前学生信息:{}",studentVO);
        String phone = studentVO.getPhone();
        String username = studentVO.getUsername();
        //生成新订单
        BeanUtils.copyProperties(pposDTO,orders);
        String orderNumber = String.valueOf(System.currentTimeMillis());
        orders.setOrderNumber(orderNumber);
        orders.setStudentId(studentId);
        orders.setStatus(Orders.PENDING_PAYMENT);
        orders.setOrderTime(LocalDateTime.now());
        orders.setPayMethod(Orders.WECHAT_PAY);
        orders.setPayStatus(Orders.UN_PAID);
        orders.setUsername(username);
        orders.setPhone(phone);
        log.info("生成新的订单:{}",orders);
        //主键回显出现问题,改用其他方式
        //获取订单表主键id
        orderMapper.submitOrder(orders);
        Long id = orderMapper.getIdByOrderNumber(orderNumber);
        log.info("新生成的订单ID:{}",id);
        //根据学生id获取积分表id
        Long pointsdepotId = pointsMapper.queryIdBystudentId(studentId);
        log.info("积分表id:{}",pointsdepotId);
        //购买的积分
        Long points = pposDTO.getPoints();

        //绑定订单表与积分表
        pointsMapper.addOrderToPoints(id,pointsdepotId,points);

        //创建VO返回给前端进行确认
        PointsOrderSubmitVO pointsOrderSubmitVO = PointsOrderSubmitVO.builder()
                .id(id)
                .orderNumber(orders.getOrderNumber())
                .amount(orders.getAmount())
                .orderTime(orders.getOrderTime())
                .goodsName(orders.getGoodsName())
                .build();
        return pointsOrderSubmitVO;
    }

    /**
     * 学生确认支付积分订单
     * @param ppopDTO 订单号
     * @return 支付信息，供前端调用
     */
    public PointsOrderPaymentVO paymentPointsOrder(PointsPurchaseOrderPaymentDTO ppopDTO) {
        //获取openid
        Long studentId = BaseContext.getCurrentId();
        String openid = studentService.getOpenIdByStudentId(studentId);
        if (openid == null) {
            throw new BaseException("openid为空");
        }
        //将信息转化为orders，传递给支付接口
        Orders order = new Orders();
        BeanUtils.copyProperties(ppopDTO,order);
        //查询获取其余商品信息
        int amount = orderMapper.getAmountByOrderNumber(order.getOrderNumber());
        String goodsName = orderMapper.getGoodsNameByOrderNumber(order.getOrderNumber());
        //填充相应数据
        order.setGoodsName(goodsName);
        order.setAmount(amount);

        log.info("调用统一下单API");
        //创建返回类
        PointsOrderPaymentVO pointsOrderPaymentVO = new PointsOrderPaymentVO();

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
                BeanUtils.copyProperties(vo,pointsOrderPaymentVO);
                return pointsOrderPaymentVO;
            }
        } catch (HttpException e) { // 发送HTTP请求失败
            // 调用e.getHttpRequest()获取请求打印日志或上报监控，更多方法见HttpException定义
            throw new BaseException("http请求发送失败,请检查网络");
        } catch (ServiceException e) { // 服务返回状态小于200或大于等于300 ，例如500
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
        //添加异常处理
        return null;
    }

    /**
     * 支付成功后，调用函数，修改积分相关信息（原order.paySuccess方法废弃）
     * @param orderNumber 订单号
     */
    @Transactional
    public void paySuccess(String orderNumber) {
        log.info("支付成功，修改订单状态");
        Orders orders = Orders.builder()
                .orderNumber(orderNumber)
                .status(Orders.DONE)
                .checkoutTime(LocalDateTime.now())
                .payStatus(Orders.PAID)
                .build();
        //更新订单状态
        orderMapper.update(orders);

        //根据订单号获取其订单id
        Long ordersId = orderMapper.getIdByOrderNumber(orderNumber);

        //根据订单id获取其积分表id
        Long pointsdepotId = pointsMapper.getPointsdepotIdByordersId(ordersId);

        //查询出该订单所购买的积分
        Long points = orderMapper.getPointsByOrdersId(ordersId);

        //给积分表添加积分
        pointsMapper.IncreasePoints(pointsdepotId,points);
    }

    /**
     * 学生兑换评价
     * @param ratingExchangeDTO 指定具体作业和需要的积分
     */
    @Transactional
    public void exchangeRating(RatingExchangeDTO ratingExchangeDTO) {
        //获取班级作业id
        Long homeworkId = ratingExchangeDTO.getClassHomeworkId();
        //获取积分表id
        Long studentId = BaseContext.getCurrentId();
        Long pointsdepotId = pointsMapper.queryIdBystudentId(studentId);
        //给指定作业评价权限

        //先查询是否有评级
        String grade = homeworkMapper.getGradeByClassHomeworkId(homeworkId);
        ClassHomework classHomework = new ClassHomework();
        classHomework.setId(homeworkId);
        //评级为空,将completed设置为 3(已提交作业/已兑换评价/未评级/未评分)
        if(grade == null){
            classHomework.setCompleted(3);
            homeworkMapper.update(classHomework);
        }else {
            //评级不为空,将completed设置为 4(已提交作业/已兑换评价/已评级/未评分)
            classHomework.setCompleted(4);
            homeworkMapper.update(classHomework);
        }

        //消耗积分
        pointsMapper.SubtractPoints(pointsdepotId,ratingExchangeDTO.getConsumedPoints());

    }


}
