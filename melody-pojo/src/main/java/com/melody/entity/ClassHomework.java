package com.melody.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClassHomework {
    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("作业主键id")
    private Long homeworkId;

    @ApiModelProperty("学生主键id")
    private Long studentId;

    @ApiModelProperty("班级作业状态:" +
            "0未提交作业/未兑换评价/未评级/未点评,  " +
            "1已提交作业/未兑换评价//未评级/未点评,  " +
            "2已提交作业/未兑换评价//已评级/未点评,  " +
            "3已提交作业/已兑换评价//未评级/未点评,  " +
            "4已提交作业/已兑换评价//已评级/未点评,  " +
            "5已提交作业/已兑换评价//未评级/已点评,  " +
            "6已提交作业/已兑换评价//已评级/已点评,  " +
            "7退回  " +
            ")")
    private Integer completed;

    @ApiModelProperty("提交日期")
    private LocalDateTime commitTime;

    @ApiModelProperty("等级")
    private String grade;

    @ApiModelProperty("作业得分")
    private Integer score;

    @ApiModelProperty("视频url")
    private String videoUrl;

    @ApiModelProperty("教师评价")
    private String judgement;

    @ApiModelProperty("教师评价时间")
    private LocalDateTime judgementTime;

}
