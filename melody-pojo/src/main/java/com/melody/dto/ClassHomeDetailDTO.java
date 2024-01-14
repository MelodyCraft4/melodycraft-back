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
public class ClassHomeDetailDTO {
    @ApiModelProperty("班级作业id")
    private Long classHomeworkId;

    @ApiModelProperty("作业评分等级")
    private String grade;

    @ApiModelProperty("教师评价信息")
    private String judgement;

}
