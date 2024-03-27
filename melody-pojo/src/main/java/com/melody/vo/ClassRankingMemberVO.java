package com.melody.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClassRankingMemberVO {
    //班级排行主键
    private Long id;
    //学生id
    private Long studentId;
    //学生姓名
    private String studentName;
    //学生头像URL
    private String iconUrl;
    //班级id
    private Long classId;
    //总分
    private Long total;
}
