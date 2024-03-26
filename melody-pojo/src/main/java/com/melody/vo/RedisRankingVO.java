package com.melody.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RedisRankingVO {
    //学生id
    private Long studentId;
    //学生姓名
    private String studentName;
    //学生头像URL
    private String iconUrl;
    //班级id
    private Long classId;
}
