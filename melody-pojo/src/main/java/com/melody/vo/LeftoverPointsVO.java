package com.melody.vo;

import lombok.Builder;
import lombok.Data;

/**
 * 剩余点数
 */
@Data
@Builder
public class LeftoverPointsVO {
    //学生ID
    private Long studentId;
    //剩余点数
    private Long points;
}
