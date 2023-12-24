package com.melody.mapper;

import com.melody.entity.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StudentMapper {

    /**
     * 学生登录
     * @param username
     * @return
     */
    @Select("select * from student where username = #{username}")
    Student getByUsername(String username);

}
