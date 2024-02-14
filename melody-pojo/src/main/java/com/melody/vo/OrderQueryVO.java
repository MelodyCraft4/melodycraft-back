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
public class OrderQueryVO {
    @ApiModelProperty("订单id")
    private Long id;
    @ApiModelProperty("商品名称")
    private String goodsName;
    @ApiModelProperty("下单时间")
    private LocalDateTime orderTime;
}
