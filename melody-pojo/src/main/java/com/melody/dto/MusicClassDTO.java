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
public class MusicClassDTO implements Serializable {
    @ApiModelProperty("班级名称")
    private String className;
    @ApiModelProperty("班级乐器")
    private String instrument;
    @ApiModelProperty("班级教师主键ID")
    private long teacherId;
    @ApiModelProperty("班级头像")
    private String iconUrl;
}
