package com.melody.service;

import com.melody.dto.StudentDTO;
import com.melody.dto.StudentLoginDTO;
import com.melody.dto.StudentWxLoginDTO;
import com.melody.entity.Student;
import com.melody.vo.StudentQueryVO;
import com.melody.vo.StudentVO;

import java.util.List;

public interface StudentService {
    /**
     * 学生登录
     * @param studentLoginDTO
     * @return
     */
    Student login(StudentLoginDTO studentLoginDTO);

    /**
     * 学生信息修改
     * @param studentDTO
     */
    void update(StudentDTO studentDTO);

    /**
     * 学生查询个人信息
     * @return
     */
    StudentVO query();

    /**
     * 管理端: 查询所有学生(可根据name具体查询)
     * @param name
     * @return
     */
    List<StudentQueryVO> queryStudentByName(String name);

    /**
     * 学生端：微信授权登录
     * @param studentWxLoginDTO
     * @return
     */
    Student wxLogin(StudentWxLoginDTO studentWxLoginDTO);

    /**
     * 根据学生id获取学生openid
     * @return
     */
    String getOpenIdByStudentId(Long id);
}
