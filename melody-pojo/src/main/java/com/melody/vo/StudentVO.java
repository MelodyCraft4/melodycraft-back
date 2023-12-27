package com.melody.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentVO {
    @ApiModelProperty("学生主键id")
    private Long id;

    @ApiModelProperty("学生用户名")
    private String username;

    @ApiModelProperty("学生姓名")
    private String name;

    @ApiModelProperty("学生头像地址")
    private String iconUrl;

    @ApiModelProperty("学生性别")
    private Integer sex;

    @ApiModelProperty("学生电话号码")
    private String phone;

    @ApiModelProperty("学生地址")
    private String address;

    @ApiModelProperty("学生学校")
    private String school;

    @ApiModelProperty("学生年龄")
    private Integer age;

    @ApiModelProperty("学生班级")
    private ArrayList<String> classNameList;

    @ApiModelProperty("所学乐器")
    private ArrayList<String> instrumentList;



}
