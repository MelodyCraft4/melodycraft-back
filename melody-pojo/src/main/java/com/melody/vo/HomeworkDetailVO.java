package com.melody.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HomeworkDetailVO {
    @ApiModelProperty("作业标题")
    private String title;

    @ApiModelProperty("作业内容正文")
    private String content;

    @ApiModelProperty("温馨提示")
    private String prompt;

    @ApiModelProperty("图片地址")
    private String imgUrls;

    @ApiModelProperty("视频地址")
    private String videoUrls;

    @ApiModelProperty("截止时间")
    private LocalDateTime deadline;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
}
