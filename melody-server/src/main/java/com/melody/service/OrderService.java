package com.melody.service;

import com.melody.dto.OrderPaymentDTO;
import com.melody.dto.OrderSubmitDTO;
import com.melody.vo.OrderPaymentVO;
import com.melody.vo.OrderSubmitVO;

public interface OrderService {
    /**
     * 学生下单
     * @param ordersSubmitDTO 选择的下单方式以及金额
     * @return 返回前端的订单信息(id,订单号,金额,时间)
     */
    OrderSubmitVO submitOrder(OrderSubmitDTO ordersSubmitDTO);

    /**
     * 订单支付
     * @param orderPaymentDTO
     * @return
     */
    OrderPaymentVO payment(OrderPaymentDTO orderPaymentDTO);
}
