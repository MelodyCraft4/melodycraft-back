package com.melody.service.impl;

import com.melody.constant.MessageConstant;
import com.melody.context.BaseContext;
import com.melody.dto.ClassHomeDetailDTO;
import com.melody.dto.HomeworkDTO;
import com.melody.dto.StuClassHomeworkDTO;
import com.melody.entity.ClassHomework;
import com.melody.entity.Homework;
import com.melody.exception.BaseException;
import com.melody.mapper.HomeworkMapper;
import com.melody.mapper.MusicClassMapper;
import com.melody.result.Result;
import com.melody.service.HWService;
import com.melody.utils.AliOssUtil;
import com.melody.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


@Slf4j
@Service
public class HWServiceImpl implements HWService {

    @Autowired
    private AliOssUtil aliOssUtil;

    @Autowired
    private HomeworkMapper homeworkMapper;

    @Autowired
    private MusicClassMapper musicClassMapper;

    /**
     * 教师发布班级作业
     * @param files
     * @param homeworkDTO
     */
    public void assignhomework(List<MultipartFile> files, HomeworkDTO homeworkDTO) {
        //将所有文件上传到Oss服务器,并存储返回路径
        log.info("开始上传文件");
        List<String> imgUrlList = new ArrayList<>();
        List<String> videoList = new ArrayList<>();
        for (MultipartFile file : files) {
            //上传文件
            try {
                //原始文件名
                String originalFilename = file.getOriginalFilename();

                //截取原始文件名的后缀
                String extention = originalFilename.substring(originalFilename.lastIndexOf("."));
                //构造新文件名称：防止文件重名
                String objectName = UUID.randomUUID().toString() + extention;

                //文件的请求路径
                String filePath = aliOssUtil.upload(file.getBytes(), objectName);

                //判断文件是视频还是图片
                String contentType = file.getContentType();
                if (contentType.startsWith("image")) {   //图片
                    imgUrlList.add(filePath);
                } else if (contentType.startsWith("video")) {
                    videoList.add(filePath);   //视频
                }


            } catch (Exception e) {
                log.error("文件上传失败: {}", e);
                throw new BaseException(MessageConstant.UPLOAD_FAILED);

            }


        }

        //将图片和视频的url分别用逗号拼接
        String imgUrls = String.join(",", imgUrlList);
        log.info("图片url: {}", imgUrls);
        String videoUrls = String.join(",", videoList);
        log.info("视频url: {}", videoUrls);

        //将所有数据封装到实体类Homework
        Homework homework = new Homework();
        //属性拷贝
        BeanUtils.copyProperties(homeworkDTO, homework);
        homework.setImgUrls(imgUrls);
        homework.setVideoUrls(videoUrls);

        //将数据存储到数据库：需要进行主键回显操作
        log.info("开始插入教师发布的作业数据");
        int i = homeworkMapper.insertHomework(homework);

        //给学生作业表插入学生作业数据
        log.info("开始插入学生作业数据");
        //1.根据班级id查询所有学生id
        List<Long> studentIdList = musicClassMapper.getStudentIdsByClassId(homeworkDTO.getClassId());

        //2.根据学生id和作业id插入数据到班级作业表
        int result = homeworkMapper.insertClassHomework(studentIdList, homework.getId());
        log.info("插入学生作业数据成功"+result+"条");

    }


    /**
     * 教师查询指定班级所有作业
     * 根据班级id查询班级内所有作业
     * @param classId
     * @return
     */
    public List<HomeworkVO> query(Long classId) {

        List<HomeworkVO> homeworkVOList = new ArrayList<>();

        //1.根据班级id查询所有作业
        List<Homework> homeworkList = homeworkMapper.query(classId);

        //2.查询班级人数
        Integer classSize = musicClassMapper.getClassSizeByClassId(classId);

        //3.遍历所有作业，查询每个作业的完成情况
        for(Homework homework : homeworkList) {
            //根据作业id查询作业完成情况
            Integer finishCount = homeworkMapper.queryFinishCount(homework.getId());

            //封装数据
            HomeworkVO homeworkVO = HomeworkVO.builder()
                    .id(homework.getId())      //作业id
                    .title(homework.getTitle())    //作业标题
                    .classSize(classSize)     //班级人数
                    .completedSize(finishCount)   //完成人数
                    .build();

            homeworkVOList.add(homeworkVO);
        }

        return homeworkVOList;
    }

    /**
     *  学生查询指定班级所有作业
     *  根据url上的班级id查询(班级作业表)
     */
    public List<StuClassHomeworkVO> queryFromStu(Long classId) {
        //先根据classId查询作业表(homework),将该班级下的所有 作业id,title,deadline,classid查询出来
        List<StuClassHomeworkVO> stuClassHomeworkVOList = homeworkMapper.queryHWBriefByClassId(classId);
        //利用上述的list,通过homeworkid继续查询剩下的字段,并组成新的
        //TODO ：添加学生线程id，查询作业
        List<StuClassHomeworkVO> updatelist = homeworkMapper.queryFromStuByHomeworkIdList(stuClassHomeworkVOList);
        //将stuClassHomeworkVOList的数据复制补充到updatelist中
        for (StuClassHomeworkVO updateClassHomeworkVO : updatelist) {
            for (StuClassHomeworkVO stuClassHomeworkVO : stuClassHomeworkVOList) {
                if (updateClassHomeworkVO.getHomeworkId() == stuClassHomeworkVO.getHomeworkId()) {
                    updateClassHomeworkVO.setClassId(stuClassHomeworkVO.getClassId());//复制班级id
                    updateClassHomeworkVO.setTitle(stuClassHomeworkVO.getTitle());//title
                    updateClassHomeworkVO.setDeadline(stuClassHomeworkVO.getDeadline());//deadline
                    break;
                }
            }
        }
        log.info("查询后的班级作业集合:{}",updatelist);
        //返回
        return updatelist;
    }

    /**
     * 学生根据homeworkId从homework表查询作业要求,
     *    根据classHomeworkId从class_homework表查询详细情况
     */
    public StuClassHomeworkDetailVO queryDetailFromStu(Long homeworkId, Long classHomeworkId) {
        //先从homework表查询作业要求
        StuClassHomeworkDetailVO HW1 = homeworkMapper.queryAskByHomeworkId(homeworkId);
        log.info("作业要求:{}",HW1);
        //再从class_homework表查询完成情况
        StuClassHomeworkDetailVO HW2 = homeworkMapper.queryDetailFromStuByClassHomeworkId(classHomeworkId);
        log.info("作业详细情况:{}",HW2);
        //将俩者补充到后者
        HW2.setTitle(HW1.getTitle());//titile
        HW2.setContent(HW1.getContent());//正文
        HW2.setPrompt(HW1.getPrompt());//温馨提示
        HW2.setImgUrls(HW1.getImgUrls());//图片url集合
        HW2.setVideoUrls(HW1.getVideoUrls());//视频url集合
        HW2.setDeadline(HW1.getDeadline());//截止时间
        HW2.setCreateTime(HW1.getCreateTime());//创建时间
        //返回后者
        return HW2;
    }

    /**
     * 学生提交作业,更新homework表
     */
    public void updateHomeworkFromStu(MultipartFile file,StuClassHomeworkDTO stuClassHomeworkDTO) {

        //将视频文件上传到阿里云OSS,获取url
        String filePath = null;
        try {
            //原始文件名
            String originalFilename = file.getOriginalFilename();
            //截取原始文件名的后缀
            String extention = originalFilename.substring(originalFilename.lastIndexOf("."));
            //构造新文件名称：防止文件重名
            String objectName = UUID.randomUUID().toString() + extention;
            //文件上传至阿里云,并获取请求路径
            filePath = aliOssUtil.upload(file.getBytes(), objectName);
        } catch (Exception e) {
            log.error("文件上传失败: {}", e);
            throw new BaseException(MessageConstant.UPLOAD_FAILED);
        }

        //创建classhomework,插入到数据库
        ClassHomework classHomework = new ClassHomework();
        classHomework.setId(stuClassHomeworkDTO.getClassHomeworkId());
        classHomework.setVideoUrl(filePath);
        classHomework.setCommitTime(LocalDateTime.now());
        classHomework.setCompleted(1);
        log.info("classHomework:{}",classHomework);
        //根据classhomework的cid,寻求到准确的数据库路径
        homeworkMapper.update(classHomework);
    }


    /**
     * 教师查询指定班级作业要求
     * @param homeworkId
     * @return
     */
    @Override
    public HomeworkDetailVO queryHWDetailFromTea(Long homeworkId) {
        StuClassHomeworkDetailVO stuClassHomeworkDetailVO = homeworkMapper.queryAskByHomeworkId(homeworkId);

        //封装数据
        HomeworkDetailVO homeworkDetailVO = new HomeworkDetailVO();
        BeanUtils.copyProperties(stuClassHomeworkDetailVO, homeworkDetailVO);

        return homeworkDetailVO;
    }


    /**
     * 教师查询指定班级作业所有同学完成情况
     * @param homeworkId
     * @return
     */
    @Override
    public List<ClassHomeworkDetailVO> queryClassHWDetailFromTea(Long homeworkId) {
        //根据homeworkId查询班级作业表中所有同学的完成情况
        List<ClassHomework> classHomeworkList = homeworkMapper.queryClassHWDetailByHomeworkId(homeworkId);

        List<ClassHomeworkDetailVO> classHomeworkDetailVOList = new ArrayList<>();
        //封装数据
        for (ClassHomework classHomework : classHomeworkList) {
            ClassHomeworkDetailVO classHomeworkDetailVO = new ClassHomeworkDetailVO();
            BeanUtils.copyProperties(classHomework, classHomeworkDetailVO);

            //设置id,因为classhomework实体类中的id和VO中的属性名不一样
            classHomeworkDetailVO.setClassHomeworkId(classHomework.getId());
            classHomeworkDetailVOList.add(classHomeworkDetailVO);
        }

        return classHomeworkDetailVOList;
    }


    /**
     * 教师更新班级作业表信息（点评作业，退回作业）
     * @param classHomeDetailDTO
     */
    @Override
    public void update(ClassHomeDetailDTO classHomeDetailDTO) {
        ClassHomework classHomework = new ClassHomework();

        BeanUtils.copyProperties(classHomeDetailDTO, classHomework);
        classHomework.setId(classHomeDetailDTO.getClassHomeworkId());  //设置id

        //没有评价信息
        if (classHomeDetailDTO.getJudgement().equals("")) {
            classHomework.setJudgement("完成的不错，继续加油哦！");
        }

        //TODO 评级为D的作业退回去(后端逻辑怎么处理)
        if ("D".equals(classHomeDetailDTO.getGrade())) {
            classHomework.setCompleted(3); // 退回状态
        }else {
            classHomework.setCompleted(2); // 已点评状态
        }




        //设置评价时间信息
        classHomework.setJudgementTime(LocalDateTime.now());

        //更新数据库
        homeworkMapper.update(classHomework);




    }


}
