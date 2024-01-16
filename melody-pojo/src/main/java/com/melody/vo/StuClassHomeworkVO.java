package com.melody.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用于学生点击班级后,查询班级内布置的作业概况
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StuClassHomeworkVO {

    @ApiModelProperty("作业id")
    private Long homeworkId;
    @ApiModelProperty("作业标题")
    private String title;
    @ApiModelProperty("班级id")
    private Long classId;
    @ApiModelProperty("截止日期")
    private LocalDateTime deadline;
    @ApiModelProperty("班级作业主键id")
    private Long id;
    @ApiModelProperty("是否完成(0未提交，1提交未评价，2提交老师已评价)")
    private Integer completed;
    @ApiModelProperty("作业得分等级")
    private String grade;

}
