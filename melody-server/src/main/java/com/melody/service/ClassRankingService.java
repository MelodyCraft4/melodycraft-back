package com.melody.service;

import com.melody.vo.ClassQueryVO;

import java.util.List;

/**
 * 班级排行榜
 */
public interface ClassRankingService {

    List<ClassQueryVO> queryClassById();
}
