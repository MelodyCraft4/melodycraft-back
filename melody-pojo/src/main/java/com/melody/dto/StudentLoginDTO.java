package com.melody.dto;

import io.swagger.annotations.ApiModel;
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
public class StudentLoginDTO implements Serializable {
    @ApiModelProperty("学生id")
    private String username;
    @ApiModelProperty("学生密码")
    private String password;
}
