package com.melody.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingExchangeDTO implements Serializable {
    @ApiModelProperty("班级作业id")
    private Long classHomeworkId;
    @ApiModelProperty("兑换该作业评价需要消耗的积分点数")
    private Long consumedPoints;
}
