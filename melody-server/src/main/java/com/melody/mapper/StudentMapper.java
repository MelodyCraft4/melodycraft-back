package com.melody.mapper;

import com.melody.entity.Student;
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
}
