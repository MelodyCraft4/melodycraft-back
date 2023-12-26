package com.melody.mapper;

import com.melody.annocation.AutoFill;
import com.melody.entity.MusicClass;
import com.melody.enumeration.OperationType;
import com.melody.vo.StudentQueryVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MusicClassMapper {

    @AutoFill(value = OperationType.INSERT)
    @Insert("insert into music_class (instrument, className, classSize, teacherId, classCode, iconUrl) " +
            "values " +
            "(#{instrument},#{className},#{classSize},#{teacherId},#{classCode},#{iconUrl})")
    void saveMusicClass(MusicClass MusicClass);

    @Select("select * from music_class where teacherId = #{id}")

    List<MusicClass> query(Long id);

    /**
     * 教师查询班级下的学生(根据姓名和班级id)
     * @param name
     * @return
     */
    List<StudentQueryVO> queryStudentByName(@Param("name") String name, @Param("classId") Long classId);
}
