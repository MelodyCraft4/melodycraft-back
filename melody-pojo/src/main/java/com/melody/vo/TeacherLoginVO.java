package com.melody.vo;

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
public class TeacherLoginVO implements Serializable {
    @ApiModelProperty("教师主键值")
    private Long id;

    @ApiModelProperty("教师姓名")
    private String Name;

    @ApiModelProperty("教师用户名")
    private String username;

    @ApiModelProperty("jwt令牌")
    private String token;

}
