package com.melody.mapper;


import com.melody.annocation.AutoFill;
import com.melody.entity.Homework;
import com.melody.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface HomeworkMapper {
    /**
     * 教师端：插入作业数据(教师发布班级作业)
     * @param homework
     */
    @AutoFill(value = OperationType.INSERT)
    int insertHomework(Homework homework);

    /**
     * 教师端：将学生插入班级作业数据
     * @param
     */
    int insertClassHomework(@Param("studentIdList") List<Long> studentIdList, @Param("homeworkId") Long homeworkId);


    /**
     * 教师端：根据班级id查询班级内所有作业
     * @param classId
     * @return
     */
    @Select("select * from homework where classId = #{classId}")
    List<Homework> query(Long classId);


    /**
     * 教师端：根据作业id查询作业完成情况
     * @param id
     * @return
     */
    @Select("select count(*) from class_homework where homeworkId = #{id} and completed=1")
    Integer queryFinishCount(Long id);
}
