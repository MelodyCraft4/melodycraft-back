package com.melody.service;


import com.melody.dto.MusicClassDTO;
import com.melody.entity.MusicClass;
import com.melody.vo.MusicClassVO;
import com.melody.vo.StudentQueryVO;
import com.melody.vo.StudentVO;

import java.util.List;

public interface MusicClassService {

    /**
     * 教师 - 新增班级
     * @param MusicClassDTO
     */
    void saveMusicClass(MusicClassDTO MusicClassDTO,String inviteCode);

    /**
     * 教师 - 根据id查询班级
     * @return
     */
    List<MusicClass> queryByTeacherId();

    /**
     * 教师 - 查询班级下的学生(根据姓名和班级id)
     * @param name
     * @return
     */
    List<StudentQueryVO> queryStudentByName(String name,Long classId);

    /**
     * 教师 - 根据学生id查询显示学生信息
     * @param id
     * @return
     */
    StudentVO showStudentById(Long id);

    void deleteStudentById(Long studentId, Long classId);

    /**
     * 学生 - 加入班级
     * @param classCode 班级邀请码
     */
    void joinClass(String classCode);

    /**
     * 学生 - 根据班级id查询班级
     * @return
     */
    List<MusicClassVO> queryByStudentId();
}
