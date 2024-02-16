package com.melody.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderVO {
    @ApiModelProperty("订单id")
    private Long id;
    @ApiModelProperty("订单号")
    private String orderNumber;
    @ApiModelProperty("下单学生id")
    private Long studentId;
    @ApiModelProperty("商品名称")
    private String goodsName;
    @ApiModelProperty("订单状态 1待付款 2待评价 3已评价 4已取消")
    private Integer status;
    @ApiModelProperty("下单时间")
    private LocalDateTime orderTime;
    @ApiModelProperty("结账时间")
    private LocalDateTime checkoutTime;
    @ApiModelProperty("支付方式 1微信，2支付宝")
    private Integer payMethod;
    @ApiModelProperty("支付状态 0未支付 1已支付 2退款")
    private Integer payStatus;
    @ApiModelProperty("实收金额")
    private Integer amount;
    @ApiModelProperty("订单取消时间")
    private LocalDateTime cancelTime;
}
