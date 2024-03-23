package com.melody.service.impl;

import com.melody.context.BaseContext;
import com.melody.mapper.MusicClassMapper;
import com.melody.mapper.StudentMapper;
import com.melody.service.ClassRankingService;
import com.melody.vo.ClassQueryVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ClassRankingServiceImpl implements ClassRankingService {

    @Autowired
    StudentMapper studentMapper;

    @Autowired
    MusicClassMapper musicClassMapper;

    /**
     * 根据学生id查询学生所有班级
     * @return
     */
    public List<ClassQueryVO> queryClassById() {
        Long studentId = BaseContext.getCurrentId();
        log.info("当前学生id为:{}",studentId);
        List<ClassQueryVO> classQueryVOS = musicClassMapper.queryClassById(studentId);
        log.info("学生id为:{}的所有班级:{}",studentId,classQueryVOS);
        return classQueryVOS;
    }
}
