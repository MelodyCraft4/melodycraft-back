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
public class classesDTO {
    @ApiModelProperty("班级名称")
    private String className;
    @ApiModelProperty("班级乐器")
    private String instrument;
    @ApiModelProperty("班级教师id")
    private long classTeacherId;
}
