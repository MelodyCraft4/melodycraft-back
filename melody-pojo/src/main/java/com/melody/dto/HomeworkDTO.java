package com.melody.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HomeworkDTO {

    @ApiModelProperty("作业标题")
    private String title;

    @ApiModelProperty("作业内容")
    private String content;

    @ApiModelProperty("温馨提示")
    private String prompt;

    @ApiModelProperty("截止时间")
    private String deadLine;

    @ApiModelProperty("班级主键id")
    private Long classId;


}
