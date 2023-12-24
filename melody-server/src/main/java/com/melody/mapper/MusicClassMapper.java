package com.melody.mapper;

import com.melody.entity.MusicClass;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MusicClassMapper {

    @Insert("insert into music_class (instrument, className, classSize, teacherId, classCode, iconUrl, createTime, updateTime, createUser, updateUser) " +
            "values " +
            "(#{instrument},#{className},#{classSize},#{classTeacherId},#{classCode},#{iconUrl},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    void saveMusicClass(MusicClass MusicClass);

    @Select("select * from music_class where teacherId = #{id}")
    List<MusicClass> query(Long id);
}
