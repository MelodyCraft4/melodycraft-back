package com.melody.service;

import com.melody.dto.OrderPaymentDTO;
import com.melody.dto.OrderSubmitDTO;
import com.melody.vo.OrderPaymentVO;
import com.melody.vo.OrderQueryVO;
import com.melody.vo.OrderSubmitVO;
import com.melody.vo.OrderVO;

import java.io.IOException;
import java.util.List;

public interface OrderService {

    /**
     * 学生下单
     * @param orderSubmitDTO
     * @return
     */
    public OrderSubmitVO submit(OrderSubmitDTO orderSubmitDTO);

    /**
     * 学生查询全部订单 - 通过学生ID(线程获取)
     * @return
     */
    public List<OrderQueryVO> queryOrdersByStudentId();

    /**
     * 学生获取具体订单信息
     * @return
     */
    public OrderVO queryByOrderId(Long id);

    /**
     * 学生支付订单
     * @param orderPaymentDTO
     * @return
     */
    public OrderPaymentVO payment(OrderPaymentDTO orderPaymentDTO) throws IOException;

    /**
     * 支付成功后,调用函数,修改相关订单信息
     * @param orderNumber
     */
    public void paySuccess(String orderNumber);
}
