package com.melody.mapper;

import com.melody.entity.Student;
import com.melody.entity.Teacher;
import com.melody.vo.TeacherQueryVO;
import com.melody.vo.TeacherVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

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

    /**
     * 根据username来修改密码
     * @param teacher
     */
    @Update("UPDATE teacher SET password = #{password} WHERE username = #{username} ")
    void resetPasswordByUsername(Teacher teacher);

    /**
     * 管理端: 查询所有教师(可根据name具体查询)
     * @param name
     * @return
     */
    @Select("select id,name,iconurl from teacher where name like concat('%',#{name},'%') and type = 2 and status=1")
    List<TeacherQueryVO> queryTeacherByName(String name);
}
