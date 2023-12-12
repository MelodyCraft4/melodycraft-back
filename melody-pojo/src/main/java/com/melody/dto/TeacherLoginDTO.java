package com.melody.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class TeacherLoginDTO implements Serializable {
        @ApiModelProperty("教师用户名")
        private String teacherId;

        @ApiModelProperty("教师密码")
        private String password;
}
