package com.melody.service;

import com.melody.dto.ClassHomeDetailDTO;
import com.melody.dto.HomeworkDTO;
import com.melody.dto.StuClassHomeworkDTO;
import com.melody.vo.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface HWService {
    /**
     * 教师发布班级作业
     * @param files
     * @param homeworkDTO
     */
    void assignhomework(List<MultipartFile> files, HomeworkDTO homeworkDTO);

    /**
     * 教师查询指定班级所有作业
     * @param classId
     * @return
     */
    List<HomeworkVO> query(Long classId);

    /**
     *  学生查询指定班级所有作业(概况)
     *  根据url上的班级id查询(班级作业表)
     */
    List<StuClassHomeworkVO> queryFromStu(Long classId);

    /**
     * 学生根据homeworkId从homework表查询作业要求,
     *    根据classHomeworkId从class_homework表查询完成情况
     * @param homeworkId
     * @param classHomeworkId
     * @return
     */
    StuClassHomeworkDetailVO queryDetailFromStu(Long homeworkId,Long classHomeworkId);

    /**
     * 学生提交作业,更新homework表
     */
    void updateHomeworkFromStu(MultipartFile file,StuClassHomeworkDTO stuClassHomeworkDTO);


    /**
     * 教师查询指定班级作业要求
     * @param homeworkId
     * @return
     */
    HomeworkDetailVO queryHWDetailFromTea(Long homeworkId);


    /**
     * 教师查询指定班级作业所有同学完成情况
     * @param homeworkId
     * @return
     */
    List<ClassHomeworkDetailVO> queryClassHWDetailFromTea(Long homeworkId);

    /**
     * 教师更新班级作业表信息（点评作业，退回作业）
     * @param classHomeDetailDTO
     */
    void update(ClassHomeDetailDTO classHomeDetailDTO);
}
