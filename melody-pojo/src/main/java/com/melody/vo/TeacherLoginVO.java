package com.melody.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class TeacherLoginVO implements Serializable {
    @ApiModelProperty("教师主键值")
    private Long id;

    @ApiModelProperty("教师姓名")
    private String teacherName;

    @ApiModelProperty("教师用户名")
    private String teacherId;

    @ApiModelProperty("jwt令牌")
    private String token;

}
