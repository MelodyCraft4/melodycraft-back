package com.melody.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 班级实体类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MusicClass implements Serializable {
    @ApiModelProperty("班级主键id")
    private long id;
    @ApiModelProperty("班级乐器")
    private String instrument;
    @ApiModelProperty("班级名称")
    private String className;
    @ApiModelProperty("班级人数")
    private int classSize;
    @ApiModelProperty("班级老师主键")
    private Long teacherId;
    @ApiModelProperty("班级码")
    private String classCode;
    @ApiModelProperty("班级头像url")
    private String iconUrl;
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
    @ApiModelProperty("创建人主键id")
    private Long createUser;
    @ApiModelProperty("更新人主键id")
    private Long updateUser;
}
