package com.melody.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 订单项实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Orders implements Serializable{

    /**
     * 订单状态 1待付款 2待评价 3已评价 4已取消 X
     * 订单状态 1代付款 2已完成 3取消
     */
    public static final Integer PENDING_PAYMENT = 1;
    //public static final Integer TO_BE_EVALUATED = 2;
    //public static final Integer RATED = 3;
    public static final Integer DONE = 2;
    public static final Integer CANCELLED = 3;

    /**
     * 支付状态 0未支付 1已支付 2退款
     */
    public static final Integer UN_PAID = 0;
    public static final Integer PAID = 1;
    public static final Integer REFUND = 2;

    /**
     * 支付方式 1微信支付 2支付宝支付
     */
    public static final Integer WECHAT_PAY = 1;
    public static final Integer ALI_PAY = 2;

    private static final long serialVersionUID = 1L;
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
    @ApiModelProperty("用户名称")
    private String username;
    @ApiModelProperty("手机号码")
    private String phone;
    @ApiModelProperty("订单取消时间")
    private LocalDateTime cancelTime;
}

