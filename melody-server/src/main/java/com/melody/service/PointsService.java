package com.melody.service;

import com.melody.dto.PointsPurchaseOrderPaymentDTO;
import com.melody.dto.PointsPurchaseOrderSubmitDTO;
import com.melody.dto.RatingExchangeDTO;
import com.melody.vo.LeftoverPointsVO;
import com.melody.vo.PointsOrderPaymentVO;
import com.melody.vo.PointsOrderSubmitVO;

public interface PointsService {
    /**
     *  查询获取当前剩余积分点数
     * @return 剩余积分点
     */
    public LeftoverPointsVO queryLeftoverPointsVO();

    /**
     * 学生下单购买积分
     * @return 返回生产的订单信息，让用户进一步确认
     */
    public PointsOrderSubmitVO submitPointsOrder(PointsPurchaseOrderSubmitDTO pposDTO);

    /**
     * 学生确认支付积分订单
     * @param ppopDTO 订单号
     * @return 支付信息，供前端调用
     */
    public PointsOrderPaymentVO paymentPointsOrder(PointsPurchaseOrderPaymentDTO ppopDTO);

    /**
     * 支付成功后，调用函数，修改积分相关信息（原order.paySuccess方法废弃）
     * @param orderNumber 订单号
     */
    public void paySuccess(String orderNumber);

    /**
     * 学生兑换评价
     * @param ratingExchangeDTO 指定具体作业和需要的积分
     */
    public void exchangeRating(RatingExchangeDTO ratingExchangeDTO);
}
