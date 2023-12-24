package com.melody.service;

import com.melody.dto.StudentLoginDTO;
import com.melody.entity.Student;
import java.util.List;

public interface StudentService {
    /**
     * 学生登录
     * @param studentLoginDTO
     * @return
     */
    Student login(StudentLoginDTO studentLoginDTO);


}
