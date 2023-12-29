package com.melody.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherVO {
    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("教师用户名")
    private String username;

    @ApiModelProperty("教师姓名")
    private String name;

    @ApiModelProperty("教师头像")
    private String iconUrl;

    @ApiModelProperty("教师电话")
    private String phone;

    @ApiModelProperty("教师性别，1是男，2是女")
    private Integer sex;

    @ApiModelProperty("教师所在学校")
    private String school;

    @ApiModelProperty("教师职称")
    private String ranking;

    @ApiModelProperty("教师出生日期")
    private LocalDate birthday;

}
