package com.melody.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 学生点击具体作业后,所回传的作业具体信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StuClassHomeworkDetailVO {
    //此块由查询作业表(homework)
    @ApiModelProperty("作业标题")
    private String title;
    @ApiModelProperty("作业内容正文(要求)")
    private String content;
    @ApiModelProperty("温馨提示")
    private String prompt;
    @ApiModelProperty("作业发布所携带的图片url集合")
    private String imgUrls;
    @ApiModelProperty("作业发布所携带的视频url集合")
    private String videoUrls;
    @ApiModelProperty("作业截止时间")
    private LocalDateTime deadline;

    @ApiModelProperty("作业创建时间")
    private LocalDateTime createTime;

    //查询班级作业表(class_homework)获取
    @ApiModelProperty("班级主键id")
    private Long classHomeworkId;
    @ApiModelProperty("是否完成(0未提交，1提交未评价，2提交老师已评价，3退回)")
    private Integer completed;
    @ApiModelProperty("作业得分等级(ABCD)")
    private String grade;
    @ApiModelProperty("学生提交视频url")
    private String videoUrl;
    @ApiModelProperty("教师评价信息")
    private String judgement;
    @ApiModelProperty("教师评价时间")
    private LocalDateTime judgementTime;
    @ApiModelProperty("学生提交作业时间")
    private LocalDateTime commitTime;
}
