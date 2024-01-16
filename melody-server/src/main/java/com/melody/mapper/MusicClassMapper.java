package com.melody.mapper;

import com.melody.annocation.AutoFill;
import com.melody.entity.MusicClass;
import com.melody.entity.StudentClass;
import com.melody.enumeration.OperationType;
import com.melody.vo.MusicClassVO;
import com.melody.vo.StudentQueryVO;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface MusicClassMapper {

    /**
     * 教师 - 创建班级
     * @param MusicClass
     */
    @AutoFill(value = OperationType.INSERT)
    @Insert("insert into music_class (instrument, className, classSize, teacherId, classCode, iconUrl,createTime,updateTime,updateUser,createUser) " +
            "values " +
            "(#{instrument},#{className},#{classSize},#{teacherId},#{classCode},#{iconUrl},#{createTime},#{updateTime},#{updateUser},#{createUser})")
    void saveMusicClass(MusicClass MusicClass);

    /**
     * 教师 - 根据id查询所属班级
     * @param teacherId
     * @return
     */
    @Select("SELECT mc.id,mc.classSize,mc.className,mc.instrument,t.name AS teacherName,DATEDIFF(CURDATE(), mc.createTime) AS days " +
            "FROM music_class mc " +
            "JOIN teacher t ON mc.teacherId = t.id " +
            "where mc.teacherId = #{teacherId}")
    List<MusicClassVO> queryAttrClassByTeacherId(Long teacherId);

    /**
     * 教师 - 查询班级下的学生(根据姓名和班级id)
     * @param name
     * @return
     */
    List<StudentQueryVO> queryStudentByName(@Param("name") String name, @Param("classId") Long classId);

    /**
     * 教师 - 根据学生id查询显示学生班级信息
     * @param id
     * @return
     */
    @Select("select  c.* from student_class sc join music_class c on sc.classId = c.id where sc.studentId = #{id};")
    //TODO:是否有功能相同的地方可以优化?
    ArrayList<MusicClass> getClassByStudentId(Long id);

    /**
     * 教师 - 根据学生id和班级id将学生移出班级
     * @param studentId
     * @param classId
     */
    @Delete("delete from student_class where studentId = #{studentId} and classId = #{classId}")
    void deleteStudentById(@Param("studentId") Long studentId, @Param("classId") Long classId);

    /**
     * TODO 班级人数-1
     * @param classId
     */
    @Update("update music_class set classSize = classSize - 1 where id = #{classId}")
    void reduceClassSize(Long classId);

    /**
     * 学生 - 根据班级码查找班级ID
     * @param classCode
     * @return
     */
    @Select("select id from music_class where classCode = #{classCode}")
    long queryClassByClassCode(String classCode);

    /**
     * 学生 - 查询学生班级表
     * @param studentClass
     * @return 学生所在班级集合
     */
    @Select("select * from student_class where classId = #{classId} and studentId = #{studentId} ")
    StudentClass queryStudentClass(StudentClass studentClass);

    /**
     * 学生 - 加入班级,将相关信息加入 学生班级表
     */
    @Insert("insert into student_class(studentId, classId) VALUES (#{studentId},#{classId})")
    void saveStudentToClass(StudentClass studentClass);

    /**
     * 学生 - 根据学生id,从student_class中查找所属班级id
     * @param studentId
     * @return
     */
    @Select("select classId from student_class where studentId = #{studnetId}")
    List<Long> queryAttrClassByStudentId(Long studentId);

    /**
     * 学生 - 根据班级id列表查询班级
     * @return
     */
    List<MusicClass> queryByClassIdList(List<Long> classIdList);

    /**
     * 班级 - 班级表的更新
     * @param musicClass
     */
    void update(MusicClass musicClass);

    /**
     * 班级 - 根据班级Id查询班级人数
     * @return
     */
    @Select("select classSize from music_class where id = #{classId}")
    Integer getClassSizeByClassId(Long classId);


    /**
     * 根据班级id查询所有学生id
     * @param classId
     * @return
     */
    @Select("select studentId from student_class where classId = #{classId}")
    List<Long> getStudentIdsByClassId(Long classId);


}
