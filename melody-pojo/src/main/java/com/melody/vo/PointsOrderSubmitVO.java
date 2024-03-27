package com.melody.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PointsOrderSubmitVO implements Serializable {
    @ApiModelProperty("订单id")
    private Long id;
    @ApiModelProperty("订单号")
    private String orderNumber;
    @ApiModelProperty("商品名称")
    private String goodsName;
    @ApiModelProperty("订单金额")
    private Integer amount;
    @ApiModelProperty("下单时间")
    private LocalDateTime orderTime;
}
