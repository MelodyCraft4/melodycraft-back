package com.melody.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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
            "0未提交作业/未付款/未评级/未评分,  " +
            "1已提交作业/未付款/未评级/未评分,  " +
            "2已提交作业/未付款/已评级/未评分,  " +
            "3已提交作业/已付款/未评级/未评分,  " +
            "4已提交作业/已付款/已评级/未评分,  " +
            "5已提交作业/已付款/未评级/已评分,  " +
            "6已提交作业/已付款/已评级/已评分,  " +
            "7退回  " +
            ")")
    private Integer completed;

    @ApiModelProperty("提交日期")
    private LocalDateTime commitTime;

    @ApiModelProperty("等级")
    private String grade;

    @ApiModelProperty("视频url")
    private String videoUrl;

    @ApiModelProperty("教师评价")
    private String judgement;

    @ApiModelProperty("教师评价时间")
    private LocalDateTime judgementTime;

}
