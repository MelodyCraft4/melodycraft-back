package com.melody.service.impl;

import com.melody.dto.TeacherLoginDTO;
import com.melody.entity.Teacher;
import com.melody.mapper.TeacherMapper;
import com.melody.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    TeacherMapper teacherMapper;

    @Override
    public Teacher login(TeacherLoginDTO teacherLoginDTO) {
        return null;
    }
}
