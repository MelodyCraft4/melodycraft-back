package com.melody.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AdminMapper {

    @Select("select count(*) from student")
    public Integer queryStudentNum() ;

    @Select("select count(*) from teacher")
    public Integer queryTeacherNum();

    @Select("select count(*) from music_class")
    public Integer queryClassNum();
}
