package com.melody.service;


import com.melody.dto.MusicClassDTO;
import com.melody.entity.MusicClass;
import com.melody.vo.StudentQueryVO;
import com.melody.vo.StudentVO;

import java.util.List;

public interface MusicClassService {

    /**
     * 新增班级
     * @param MusicClassDTO
     */
    void saveMusicClass(MusicClassDTO MusicClassDTO,String inviteCode);

    /**
     * 教师查询班级
     * @return
     */
    List<MusicClass> query();

    /**
     * 教师查询班级下的学生(根据姓名和班级id)
     * @param name
     * @return
     */
    List<StudentQueryVO> queryStudentByName(String name,Long classId);

    /**
     * 根据id查询显示学生信息
     * @param id
     * @return
     */
    StudentVO showStudentById(Long id);

    void deleteStudentById(Long studentId, Long classId);
}
