package com.melody.mapper;

import com.melody.entity.Student;
import com.melody.vo.StudentQueryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface StudentMapper {

    /**
     * 学生登录
     * @param username
     * @return
     */
    @Select("select * from student where username = #{username}")
    Student queryStuByUsername(String username);

    /**
     * 根据id查询学生
     */
    @Select("select * from student where id = #{id}")
    Student queryStuById(Long id);

    /**
     * 学生信息修改
     * @param student
     */
    void update(Student student);


    /**
     * 根据学生id获取学生openid
     * @return
     */
    @Select("SELECT openid FROM student where id = #{id}")
    String getOpenIdByStudentId(Long id);

    /**
     * 管理端: 查询所有学生(可根据name具体查询)
     * @param name
     * @return
     */
    @Select("select id,name,iconurl from student where name like concat('%',#{name},'%') and status = 1")
    List<StudentQueryVO> queryStudentByName(String name);
}
