package com.melody.mapper;

import com.melody.annocation.AutoFill;
import com.melody.entity.MusicClass;
import com.melody.enumeration.OperationType;
import com.melody.vo.StudentQueryVO;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;
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

    /**
     * 根据学生id查询显示学生班级信息
     * @param id
     * @return
     */
    @Select("select  c.* from student_class sc join music_class c on sc.classId = c.id where sc.studentId = #{id};")
    ArrayList<MusicClass> getByStudentId(Long id);

    /**
     * 根据学生id和班级id将学生移出班级
     * @param studentId
     * @param classId
     */
    @Delete("delete from student_class where studentId = #{studentId} and classId = #{classId}")
    void deleteStudentById(@Param("studentId") Long studentId, @Param("classId") Long classId);

    /**
     * 班级人数-1
     * @param classId
     */
    @Update("update music_class set classSize = classSize - 1 where id = #{classId}")
    void reduceClassSize(Long classId);
}
