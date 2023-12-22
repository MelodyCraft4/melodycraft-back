package com.melody.entity;

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
public class StudentClass implements Serializable {
    @ApiModelProperty("主键id")
    Long id;

    @ApiModelProperty("学生id")
    Long studentId;

    @ApiModelProperty("班级id")
    Long classId;


}
