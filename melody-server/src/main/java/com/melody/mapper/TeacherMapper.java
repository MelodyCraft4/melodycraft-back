package com.melody.mapper;

import com.melody.entity.Teacher;
import com.melody.vo.TeacherVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TeacherMapper {

    /**
     * 根据教师用户名查询教师
     * @param username
     * @return
     */
    @Select("select * from teacher where username = #{username};")
    Teacher queryTchByUsername(String username);

    /**
     * 根据教师Id查询教师姓名
     * @param teacherId
     * @return
     */
    @Select("select name from teacher where id = #{teacherId}")
    String getNameById(Long teacherId);

    /**
     * 根据教师Id查询教师
     * @param teacherId
     * @return
     */
    @Select("select * from teacher where id = #{teacherId}")
    Teacher queryTchById(Long teacherId);

    /**
     * 教师更新/完善个人信息
     */
    void update(Teacher teacher);
}
