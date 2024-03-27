package com.melody.service.impl;

import com.alibaba.fastjson.JSON;
import com.melody.constant.RedisConstant;
import com.melody.context.BaseContext;
import com.melody.mapper.HomeworkMapper;
import com.melody.mapper.MusicClassMapper;
import com.melody.mapper.StudentMapper;
import com.melody.service.ClassRankingService;
import com.melody.vo.ClassQueryVO;
import com.melody.vo.ClassRankingMemberVO;
import com.melody.vo.RedisRankingVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class ClassRankingServiceImpl implements ClassRankingService {

    @Autowired
    StudentMapper studentMapper;

    @Autowired
    MusicClassMapper musicClassMapper;

    @Autowired
    HomeworkMapper homeworkMapper;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    /**
     * 根据学生id查询学生所有班级
     * @return
     */
    public List<ClassQueryVO> queryClassById() {
        Long studentId = BaseContext.getCurrentId();
        log.info("当前学生id为:{}",studentId);
        List<ClassQueryVO> classQueryVOS = musicClassMapper.queryClassById(studentId);
        log.info("学生id为:{}的所有班级:{}",studentId,classQueryVOS);
        return classQueryVOS;
    }

    /**
     * 学生端 教师端查询班级排行榜
     * @param classId
     * @return
     */
    @Override
    public List<ClassRankingMemberVO> queryClassRanking(Long classId) {
        String key = RedisConstant.CLASS_RANKING+classId;
        Set<String> stringSet = stringRedisTemplate.opsForZSet().reverseRange(key, 0, -1);

        if (stringSet == null || stringSet.size() == 0) {
            //说明当前排行版没有数据，需要从数据库加载数据，并存入redis
            log.info("当前排行榜没有数据，需要从数据库加载数据，并存入redis");
            List<ClassRankingMemberVO> classRankingMemberVOS = homeworkMapper.queryClassStudentRank(classId);
            log.info("班级id为:{}的学生排行榜:{}",classId,classRankingMemberVOS);
            //把数据存储到redis中
            for (ClassRankingMemberVO classRankingMemberVO : classRankingMemberVOS) {
               // RedisRankingVO redisRankingVO = new RedisRankingVO();
                //属性拷贝
                //BeanUtils.copyProperties(classRankingMemberVO,redisRankingVO);
                stringRedisTemplate.opsForZSet().add(key, JSON.toJSONString(classRankingMemberVO), classRankingMemberVO.getTotal());
            }
            stringSet = stringRedisTemplate.opsForZSet().reverseRange(key, 0, -1);
        }

        // json转换
        List<ClassRankingMemberVO> classRankingMemberVOS = new ArrayList<>();;
        for (String s : stringSet) {
            ClassRankingMemberVO classRankingMemberVO = JSON.parseObject(s, ClassRankingMemberVO.class);
            classRankingMemberVOS.add(classRankingMemberVO);
        }
        return classRankingMemberVOS;

    }

    @Override
    public List<ClassQueryVO> teaQueryClassById() {
        Long teacherId = BaseContext.getCurrentId();
        log.info("当前教师id为:{}",teacherId);
        List<ClassQueryVO> classQueryVOS = musicClassMapper.queryClassByTeacherId(teacherId);
        log.info("教师id为:{}的所有班级:{}",teacherId,classQueryVOS);
        return classQueryVOS;
    }

    /**
     * 根据班级id查询班级排行榜信息
     */

}
