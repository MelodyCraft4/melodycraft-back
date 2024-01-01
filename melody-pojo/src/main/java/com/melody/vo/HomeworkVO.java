package com.melody.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HomeworkVO {
    @ApiModelProperty("作业主键id")
    private Long id;

    @ApiModelProperty("作业标题")
    private String title;

    @ApiModelProperty("班级人数")
    private Integer classSize;

    @ApiModelProperty("已完成人数")
    private Integer completedSize;

}
