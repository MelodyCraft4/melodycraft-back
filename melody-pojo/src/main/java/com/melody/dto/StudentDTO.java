package com.melody.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentDTO implements Serializable {
    @ApiModelProperty("主键id")
    private Long id;
    @ApiModelProperty("学生姓名")
    private String name;
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty("电话号码")
    private String phone;
    @ApiModelProperty("性别")
    private String sex;
    @ApiModelProperty("学校")
    private String school;
    @ApiModelProperty("头像url")
    private String iconUrl;
}
