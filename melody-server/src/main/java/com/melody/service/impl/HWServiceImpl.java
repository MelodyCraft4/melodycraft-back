package com.melody.service.impl;

import com.melody.constant.MessageConstant;
import com.melody.dto.HomeworkDTO;
import com.melody.entity.Homework;
import com.melody.mapper.HomeworkMapper;
import com.melody.mapper.MusicClassMapper;
import com.melody.result.Result;
import com.melody.service.HWService;
import com.melody.utils.AliOssUtil;
import com.melody.vo.HomeworkVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
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

    @Override
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
     * 根据班级id查询班级内所有作业
     * @param classId
     * @return
     */
    @Override
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


}
