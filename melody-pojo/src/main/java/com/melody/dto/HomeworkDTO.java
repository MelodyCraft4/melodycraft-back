package com.melody.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HomeworkDTO {

    @ApiModelProperty("作业标题")
    private String title;

    @ApiModelProperty("作业内容")
    private String content;

    @ApiModelProperty("温馨提示")
    private String prompt;

    @ApiModelProperty("截止时间")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime deadline;

    @ApiModelProperty("班级主键id")
    private Long classId;


}
