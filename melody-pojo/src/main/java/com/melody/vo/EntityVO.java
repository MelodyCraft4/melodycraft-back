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
public class EntityVO {
    @ApiModelProperty("学生数量")
    private Integer studentNum;

    @ApiModelProperty("教师数量")
    private Integer teacherNum;

    @ApiModelProperty("班级数量")
    private Integer classNum;
}
