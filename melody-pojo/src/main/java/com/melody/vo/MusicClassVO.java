package com.melody.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MusicClassVO implements Serializable {
    @ApiModelProperty("主键ID")
    private Long id;
    @ApiModelProperty("班级人数")
    private Integer classSize;
    @ApiModelProperty("班级名称")
    private String className;
    @ApiModelProperty("班级乐器")
    private String instrument;
    @ApiModelProperty("教师名称")
    private String teacherName;
    @ApiModelProperty("班级授课天数")
    private Integer days;
}
