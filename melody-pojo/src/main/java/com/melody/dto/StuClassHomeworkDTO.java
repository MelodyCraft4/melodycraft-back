package com.melody.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 学生更新班级作业（本质是提交作用）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StuClassHomeworkDTO {
    @ApiModelProperty("班级作业id")
    private Long classHomeworkId;
}
