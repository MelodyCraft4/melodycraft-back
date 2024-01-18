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
public class TeacherQueryVO {
    @ApiModelProperty("教师主键id")
    private Long id;
    @ApiModelProperty("教师姓名")
    private String name;
    @ApiModelProperty("头像Url")
    private String iconUrl;
}
