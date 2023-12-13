package com.melody.service;

import com.melody.dto.TeacherLoginDTO;
import com.melody.entity.Teacher;

public interface TeacherService {
    Teacher login(TeacherLoginDTO teacherLoginDTO);
}
