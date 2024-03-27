package com.melody.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointsPurchaseOrderSubmitDTO implements Serializable {
    //付款方式
    private int paymMethod;
    //实收金额
    private Integer amount;
    //购买的点数
    private Long points;
    //商品名称,如购买三十积分，则可以填写为购买积分三十点
    private String goodsName;
}
