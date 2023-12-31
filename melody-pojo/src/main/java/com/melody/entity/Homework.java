package com.melody.entity;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Homework {
    @ApiModelProperty("作业主键id")
    private Long id;

    @ApiModelProperty("作业标题")
    private String title;

    @ApiModelProperty("作业内容")
    private String content;

    @ApiModelProperty("温馨提示")
    private String prompt;

    @ApiModelProperty("图片地址")
    private String imgUrls;

    @ApiModelProperty("视频地址")
    private String videoUrls;

    @ApiModelProperty("截止时间")
    private String deadLine;

    @ApiModelProperty("班级主键id")
    private Long classId;

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
