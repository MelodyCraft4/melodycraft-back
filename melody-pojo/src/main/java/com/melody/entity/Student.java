package com.melody.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Student implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("主键id")
    private Long id;
    @ApiModelProperty("学生用户名")
    private Long username;
    @ApiModelProperty("姓名")
    private String name;
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty("电话")
    private String phone;
    @ApiModelProperty("性别")
    private String sex;
    @ApiModelProperty("学校")
    private String school;
    @ApiModelProperty("家庭地址")
    private String address;
    @ApiModelProperty("头像url")
    private String iconUrl;
    @ApiModelProperty("账号状态,1启用,2禁用")
    private Integer status;

    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("创建人主键id")
    private Long createUser;
    @ApiModelProperty("更新人主键id")
    private Long updateUser;
}
