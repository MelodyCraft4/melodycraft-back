package com.melody.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClassQueryVO {
    //班级id
    private Long id;
    //班级名称
    private String className;
}
