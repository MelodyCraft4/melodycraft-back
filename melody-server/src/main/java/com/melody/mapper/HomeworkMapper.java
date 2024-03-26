package com.melody.mapper;


import com.melody.annocation.AutoFill;
import com.melody.entity.ClassHomework;
import com.melody.entity.Homework;
import com.melody.enumeration.OperationType;
import com.melody.vo.ClassHomeworkDetailVO;
import com.melody.vo.ClassRankingMemberVO;
import com.melody.vo.StuClassHomeworkDetailVO;
import com.melody.vo.StuClassHomeworkVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
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
     */
    @Select("select * from homework where classId = #{classId}")
    List<Homework> query(Long classId);


    /**
     * 教师端：根据作业id查询作业完成情况
     */
    @Select("select count(*) from class_homework where homeworkId = #{id} and completed between 1 and 6")
    Integer queryFinishCount(Long id);

    /**
     * 学生端:根据班级id查询该班级旗下所有的作业概况,查询内容包括 作业id,班级id,作业标题,作业截止时间(homework表)
     */
    @Select("select id AS homeworkId, title, deadLine, classId from homework where classId = #{classId}")
    List<StuClassHomeworkVO> queryHWBriefByClassId(Long classId);


    /**
     * 学生端:根据班级id查询该班级所有作业
     */
    List<StuClassHomeworkVO> queryFromStuByHomeworkIdList(@Param("stuClassHomeworkVOList") List<StuClassHomeworkVO> stuClassHomeworkVOList ,@Param("studentId") Long studentId);


    /**
     * 学生端:根据班级作业id查询具体作业详细情况(class_homework表)
     */
    @Select("select id AS classHomeworkId, completed, grade, videoUrl, judgement, judgementTime, commitTime " +
            "FROM class_homework where " +
            "id = #{classHomeworkId}")
    StuClassHomeworkDetailVO queryDetailFromStuByClassHomeworkId(Long classHomeworkId);

    /**
     * 学生端:根据作业id查询具体作业要求(homework表)
     */
    @Select("select title, content, prompt, imgUrls, videoUrls, deadLine,createTime " +
            "FROM homework " +
            "where id = #{homeworkId}")
    StuClassHomeworkDetailVO queryAskByHomeworkId(Long homeworkId);

    /**
     * 教师端：根据homeworkId查询班级作业完成情况
     */
    @Select("select ch.id as classHomeworkId ,ch.studentId as studentId ,ch.completed as completed ,ch.grade as grade , " +
            "ch.videoUrl as videoUrl , ch.judgement as judgement ,ch.judgementTime as judgementTime ,ch.commitTime as commitTime , " +
            "s.name as studentName " +
            "from class_homework ch " +
            "join student s on ch.studentId = s.id " +
            "where ch.homeworkId = #{homeworkId} ")
    List<ClassHomeworkDetailVO> queryClassHWDetailByHomeworkId(Long homeworkId);

    /**
     * 根据班级作业id获取付款时间
     */
    @Select("SELECT ors.checkoutTime " +
            "FROM orders ors " +
            "JOIN homework_orders ho ON ho.homeworkId = #{classHomeworkId} " +
            "WHERE ors.id = ho.ordersId ")
    LocalDateTime getCheckoutTimeByClassHomeworkId(Long classHomeworkId);

    /**
     * 教师端：更新班级作业表信息（评级,评价）
     */
    void update(ClassHomework classHomework);

    int giveClassHomework(@Param("homeworkList") List<Homework> homeworkList, @Param("studentId") Long studentId);

    /**
     * 根据班级作业id获取评级
     */
    @Select("SELECT grade FROM class_homework WHERE id = #{id}")
    String getGradeByClassHomeworkId(Long id);

    /**
     * 根据班级作业id获取作业状态
     */
    @Select("SELECT completed FROM class_homework WHERE id = #{id}")
    Integer getCompletedByClassHomeworkId(Long id);

    /**
     *  根据班级id查询班级内学生排行榜信息
     * @param classId
     * @return
     */
    List<ClassRankingMemberVO> queryClassStudentRank(Long classId);

    ClassRankingMemberVO queryClassRankingMemberVOByClassHomeworkIdAndStudentId(@Param("classId") Long classId, @Param("classhomeworkId") Long classhomeworkId);

    /**
     *
     * 根据班级作业id查询班级id
     * @param classHomeworkId
     * @return
     */
    @Select("SELECT h.classId FROM class_homework ch join homework h on ch.homeworkId = h.id WHERE ch.id = #{classHomeworkId}")
    Long getClassIdByClassHomeworkId(Long classHomeworkId);
}
