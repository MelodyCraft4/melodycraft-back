package com.melody.mapper;

import com.melody.entity.MusicClass;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ClassesMapper {

    @Insert("insert into classes (instrument, className, classSize, classTeacherId, classCode, iconUrl, createTime, updateTime, createUser, updateUser) " +
            "values " +
            "(#{instrument},#{className},#{classSize},#{classTeacherId},#{classCode},#{iconUrl},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    void saveClasses(MusicClass MusicClass);

    @Select("select * from classes where classTeacherId = #{id}")
    List<MusicClass> query(Long id);
}
