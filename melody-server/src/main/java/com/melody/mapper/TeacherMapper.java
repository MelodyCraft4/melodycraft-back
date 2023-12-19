package com.melody.mapper;

import com.melody.entity.Teacher;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TeacherMapper {

    @Select("select * from teacher where username = #{username};")
    Teacher getByteacherId(String username);
}
