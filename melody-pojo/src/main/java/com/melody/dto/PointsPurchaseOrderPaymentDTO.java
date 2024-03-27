package com.melody.dto;

import lombok.Data;


@Data
public class PointsPurchaseOrderPaymentDTO {
    //订单号
    private String orderNumber;
    //付款方式
    private Integer paymethod;
}
