package com.melody.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("学生登录是传递的数据模型")
public class StudentLoginDTO implements Serializable {
    @ApiModelProperty("学生名")
    private String studentName;
    @ApiModelProperty("学生密码")
    private String studentPassword;
}
