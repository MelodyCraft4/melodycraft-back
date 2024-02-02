package com.melody.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 提交订单DTO
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSubmitDTO {
    //付款方式
    private int payMethod;
    //实收金额
    private BigDecimal amount;
}
