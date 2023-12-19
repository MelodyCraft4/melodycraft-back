package com.melody.service.impl;

import com.melody.mapper.AdminMapper;
import com.melody.service.AdminService;
import com.melody.vo.EntityVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    AdminMapper adminMapper;

    @Override
    public EntityVO query() {
        //查询学生数量
        Integer studentNum = adminMapper.queryStudentNum();
        if (studentNum == null) {
            studentNum = 0;
        }

        //查询教师数量
        Integer teacherNum = adminMapper.queryTeacherNum();
        if (teacherNum == null) {
            teacherNum = 0;
        }

        //查询班级数量
        Integer classNum = adminMapper.queryClassNum();
        if (classNum == null) {
            classNum = 0;
        }

        EntityVO entityVO = EntityVO.builder().
                studentNum(studentNum).
                teacherNum(teacherNum).
                classNum(classNum).
                build();

        return entityVO;
    }
}
