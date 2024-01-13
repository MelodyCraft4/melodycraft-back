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


    @ApiModelProperty("是否完成,1是，0否")
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
