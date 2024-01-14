package com.melody.mapper;


import com.melody.annocation.AutoFill;
import com.melody.entity.ClassHomework;
import com.melody.entity.Homework;
import com.melody.enumeration.OperationType;
import com.melody.vo.StuClassHomeworkDetailVO;
import com.melody.vo.StuClassHomeworkVO;
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

    /**
     * 学生端:根据班级id查询该班级旗下所有的作业,查询内容包括 作业id,班级id,作业标题,作业截止时间(homework表)
     * @return
     */
    @Select("select id AS homeworkId, title, deadLine, classId from homework where classId = #{classId}")
    List<StuClassHomeworkVO> queryHWBriefByClassId(Long classId);


    /**
     * 学生端:根据班级id查询该班级所有作业
     * @return
     */
    List<StuClassHomeworkVO> queryFromStuByHomeworkIdList(List<StuClassHomeworkVO> stuClassHomeworkVOList);

    /**
     * 学生端:根据班级作业id查询具体作业详细情况(class_homework表)
     * @param classHomeworkId
     * @return
     */
    @Select("select id AS classHomeworkId, completed, grade, videoUrl, judgement, judgementTime, commitTime " +
            "FROM class_homework where " +
            "id = #{classHomeworkId}")
    StuClassHomeworkDetailVO queryDetailFromStuByClassHomeworkId(Long classHomeworkId);

    /**
     * 学生端:根据作业id查询具体作业要求(homework表)
     * @param homeworkId
     * @return
     */
    @Select("select title, content, prompt, imgUrls, videoUrls, deadLine,createTime " +
            "FROM homework where " +
            "id = #{homeworkId}")
    StuClassHomeworkDetailVO queryAskFromStuByHomeworkId(Long homeworkId);

    /**
     * 学生提交作业,更新homework表
     */
    void updateFromStu(ClassHomework classHomework);



    /**
     * 教师端：根据homeworkId查询作业详情
     * @param homeworkId
     * @return
     */
    @Select("select * from homework where id = #{homeworkId}")
    Homework queryHWDetaileByHomeworkId(Long homeworkId);

    /**
     * 教师端：根据homeworkId查询班级作业完成情况
     * @param homeworkId
     * @return
     */
    @Select("select * from class_homework where homeworkId = #{homeworkId}")
    List<ClassHomework> queryClassHWDetailByHomeworkId(Long homeworkId);

    /**
     * 教师端：更新班级作业表信息（点评作业，退回作业）
     * @param classHomework
     */
    void update(ClassHomework classHomework);
}
