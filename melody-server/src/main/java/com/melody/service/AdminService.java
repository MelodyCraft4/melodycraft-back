package com.melody.service;

import com.melody.dto.AdminLoginDTO;
import com.melody.entity.Teacher;
import com.melody.vo.EntityVO;
import com.melody.vo.StudentRegVO;
import com.melody.vo.TeacherQueryVO;
import com.melody.vo.TeacherRegVO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface AdminService {
    EntityVO query();

    /**
     * 管理员添加教师
     * @param number  添加教师的数量
     * @return
     */
    List<TeacherRegVO> addTeacher(Integer number);

    /**
     * 管理员添加学生
     * @param number  添加学生的数量
     * @return
     */
    List<StudentRegVO> addStudent(Integer number);

    void exportAccoutData(Integer type,Integer number,HttpServletResponse response);

    Teacher login(AdminLoginDTO adminLoginDTO);

    /**
     *  管理员重置学生密码
     */
    void resetStuPassword(String username);

    /**
     * 管理员重置教师密码
     */
    void resetTchPassword(String username);

}
