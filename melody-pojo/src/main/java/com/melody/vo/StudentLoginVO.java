package com.melody.vo;

import io.swagger.annotations.Api;
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
public class StudentLoginVO implements Serializable {

    @ApiModelProperty("主键")
    private long id;

    @ApiModelProperty("学生id")
    private long username;

    @ApiModelProperty("学生姓名")
    private String name;

    @ApiModelProperty("jwt令牌")
    private String token;

}
