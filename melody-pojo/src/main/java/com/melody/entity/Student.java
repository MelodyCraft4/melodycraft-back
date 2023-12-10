package com.melody.entity;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Student implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("主键id")
    private Long id;
    @ApiModelProperty("id")
    private Long studentId;
    @ApiModelProperty("姓名")
    private String studentName;
    @ApiModelProperty("密码")
    private String stuedntPassword;
    @ApiModelProperty("电话")
    private String phone;
    @ApiModelProperty("性别")
    private String sex;
    @ApiModelProperty("学校")
    private String school;
    @ApiModelProperty("班级")
    private String studentClass;
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
