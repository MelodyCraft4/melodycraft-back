package com.melody.mapper;

import com.melody.entity.classes;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ClassesMapper {

    @Insert("insert into classes (instrument, className, classSize, classTeacherId, classCode, iconUrl, createTime, updateTime, createUser, updateUser) " +
            "values " +
            "(#{instrument},#{className},#{classSize},#{classTeacherId},#{classCode},#{iconUrl},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    void saveClasses(classes classes);

    @Select("select * from classes where classTeacherId = #{id}")
    List<classes> query(Long id);
}
