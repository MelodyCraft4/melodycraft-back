package com.melody.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassHomeworkDetailVO {
    @ApiModelProperty("班级作业id")
    private Long classHomeworkId;

    @ApiModelProperty("学生id")
    private Long studentId;

    @ApiModelProperty("学生姓名")
    private String studentName;

    @ApiModelProperty("是否完成")
    private Integer completed;

    @ApiModelProperty("作业得分等级")
    private String grade;

    @ApiModelProperty("学生提交视频")
    private String videoUrl;

    @ApiModelProperty("教师评价信息")
    private String judgement;

    @ApiModelProperty("教师评价时间")
    private LocalDateTime judgementTime;

    @ApiModelProperty("学生提交时间")
    private LocalDateTime commitTime;


}
