package com.melody.service.impl;

import com.melody.constant.MessageConstant;
import com.melody.context.BaseContext;
import com.melody.dto.OrderPaymentDTO;
import com.melody.dto.OrderSubmitDTO;
import com.melody.entity.Orders;
import com.melody.entity.Student;
import com.melody.exception.BaseException;
import com.melody.mapper.OrderMapper;
import com.melody.mapper.StudentMapper;
import com.melody.service.OrderService;
import com.melody.service.StudentService;
import com.melody.vo.OrderPaymentVO;
import com.melody.vo.OrderSubmitVO;
import com.melody.vo.StudentVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private StudentService studentService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private StudentMapper studentMapper;
    /**
     * 学生下单
     * @param ordersSubmitDTO 选择的下单方式以及金额
     * @return 返回前端的订单信息(id,订单号,金额,时间)
     */
    public OrderSubmitVO submitOrder(OrderSubmitDTO ordersSubmitDTO) {
//        //判断订单信息是否为空
//        if (ordersSubmitDTO == null){
//            throw new BaseException(MessageConstant.ORDER_IS_NULL);
//        }
        //获取学生ID、手机号码、用户名称
        Long studnetId = BaseContext.getCurrentId();
        StudentVO studentVO = studentService.query();
        String phone = studentVO.getPhone();
        String username = studentVO.getUsername();
        //创建新订单
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO,orders);
        log.info("orders:{}",orders);
        //填充信息
        orders.setOrderNumber(String.valueOf(System.currentTimeMillis()));
        orders.setStudentId(studnetId);
        orders.setGoodsName("微信点评");
        orders.setStatus(Orders.PENDING_PAYMENT);
        orders.setOrderTime(LocalDateTime.now());
        orders.setPayMethod(Orders.UN_PAID);
        orders.setPayStatus(Orders.UN_PAID);
        orders.setUsername(username);
        orders.setPhone(phone);
        log.info("填充后的数据:{}",orders);
        //并插入数据库表
        Long id = orderMapper.submitOrder(orders);
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
     * 订单支付
     * @param orderPaymentDTO
     * @return
     */
    public OrderPaymentVO payment(OrderPaymentDTO orderPaymentDTO) {
        //根据当前学生ID获取openid
        Long studentId = BaseContext.getCurrentId();
        Student student = studentMapper.queryStuById(studentId);
        return null;
    }
}
