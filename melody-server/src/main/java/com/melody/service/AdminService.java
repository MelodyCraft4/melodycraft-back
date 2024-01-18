package com.melody.service;

import com.melody.vo.EntityVO;
import com.melody.vo.TeacherRegVO;

import java.util.List;

public interface AdminService {
    EntityVO query();

    /**
     * 管理员添加教师
     * @param number  添加教师的数量
     * @return
     */
    List<TeacherRegVO> addTeacher(Integer number);
}
