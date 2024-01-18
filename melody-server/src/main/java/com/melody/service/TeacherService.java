package com.melody.service;

import com.melody.dto.TeacherDTO;
import com.melody.dto.TeacherLoginDTO;
import com.melody.entity.Teacher;
import com.melody.vo.TeacherQueryVO;
import com.melody.vo.TeacherVO;

import java.util.List;

public interface TeacherService {
    /**
     * 教师登录
     * @param teacherLoginDTO
     * @return
     */
    Teacher login(TeacherLoginDTO teacherLoginDTO);

    /**
     * 教师查询个人信息
     * @return
     */
    TeacherVO query();

    /**
     * 教师端更新/完善个人信息
     */
    void update(TeacherDTO teacherDTO);

    /**
     * 管理端: 查询所有教师(可根据name具体查询)
     * @param name
     * @return
     */
    List<TeacherQueryVO> queryTeacherByName(String name);
}
