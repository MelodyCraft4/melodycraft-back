package com.melody.mapper;

import com.melody.annocation.AutoFill;
import com.melody.entity.Teacher;
import com.melody.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AdminMapper {

    @Select("select count(*) from student")
    public Integer queryStudentNum() ;

    @Select("select count(*) from teacher")
    public Integer queryTeacherNum();

    @Select("select count(*) from music_class")
    public Integer queryClassNum();


    int addTeacher(@Param("teacherList") List<Teacher> teacherList);
}
