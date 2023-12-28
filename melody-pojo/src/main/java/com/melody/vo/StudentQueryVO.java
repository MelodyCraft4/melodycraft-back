package com.melody.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentQueryVO {
    @ApiModelProperty("学生主键id")
    private Long id;

    @ApiModelProperty("学生姓名")
    private String name;

    @ApiModelProperty("学生头像地址")
    private String iconUrl;


}
