package com.melody.service;

import com.melody.vo.ClassQueryVO;
import com.melody.vo.ClassRankingMemberVO;

import java.util.List;

/**
 * 班级排行榜
 */
public interface ClassRankingService {

    List<ClassQueryVO> queryClassById();

    List<ClassRankingMemberVO> queryClassRanking(Long classId);

    /**
     * 教师查询班级
     * @return
     */
    List<ClassQueryVO> teaQueryClassById();
}
