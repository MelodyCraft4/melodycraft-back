package com.melody.service.impl;

import com.melody.context.BaseContext;
import com.melody.dto.MusicClassDTO;
import com.melody.entity.MusicClass;
import com.melody.entity.Student;
import com.melody.entity.StudentClass;
import com.melody.mapper.MusicClassMapper;
import com.melody.mapper.StudentMapper;
import com.melody.mapper.TeacherMapper;
import com.melody.service.MusicClassService;
import com.melody.vo.MusicClassVO;
import com.melody.vo.StudentQueryVO;
import com.melody.vo.StudentVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class MusicClassServiceImpl implements MusicClassService {

    @Autowired
    private MusicClassMapper musicClassMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private TeacherMapper teacherMapper;

    /**
     * 教师 - 新增班级
     * @param musicClassDTO
     */
    public void saveMusicClass(MusicClassDTO musicClassDTO,String inviteCode){
        //创建班级,附上初始值
        MusicClass musicClass = new MusicClass();
        BeanUtils.copyProperties(musicClassDTO,musicClass);
        musicClass.setClassCode(inviteCode);

        //TODO:待aop完善下列信息
        musicClass.setClassSize(0);
        //数据库添加数据
        musicClassMapper.saveMusicClass(musicClass);
    }

    /**
     * 教师 - 根据id查询班级
     * @return
     */
    public List<MusicClass> queryByTeacherId() {
        //根据线程获取当前教师id
        Long teacherId = BaseContext.getCurrentId();
        List<MusicClass> list = musicClassMapper.queryAttrClassByTeacherId(teacherId);
        return list;
    }

    /**
     * 教师 - 查询班级下的学生(根据姓名和班级id)
     * @param name
     * @return
     */
    public List<StudentQueryVO> queryStudentByName(String name,Long classId){
        if (name==null){  //前端没有传入姓名数据
            name="";
        }

        //根据姓名学生信息
        List<StudentQueryVO> list = musicClassMapper.queryStudentByName(name,classId);
        return list;
    }

    /**
     * 教师 - 根据学生id查询显示学生信息
     * @param id
     * @return
     */
    public StudentVO showStudentById(Long id) {
        StudentVO studentVO = new StudentVO();

        //1.先根据id查询学生信息
        Student student = studentMapper.queryStuById(id);

        //2.属性拷贝
        BeanUtils.copyProperties(student,studentVO);

        //把年龄算出来也插入到studentVO中
        LocalDate birthday = student.getBirthday();
        int age = LocalDate.now().getYear() - birthday.getYear();
        studentVO.setAge(age);

        //3.根据学生id查询班级信息（乐器、班级）
        ArrayList<MusicClass> classList = musicClassMapper.getClassByStudentId(id);

        //4.将班级信息中的乐器、班级设置到学生vo中
        ArrayList<String> instrumentList = new ArrayList<>();
        ArrayList<String> classNameList = new ArrayList<>();

        classList.forEach(musicClass -> {
            instrumentList.add(musicClass.getInstrument());
            classNameList.add(musicClass.getClassName());
        });

        studentVO.setInstrumentList(instrumentList);
        studentVO.setClassNameList(classNameList);



        return studentVO;
    }

    /**
     * 教师 - 根据学生id和班级id将学生移出班级
     * @param studentId
     * @param classId
     */
    public void deleteStudentById(Long studentId, Long classId) {

        //1.根据学生id和班级id将学生移出班级
        musicClassMapper.deleteStudentById(studentId,classId);

        //2.班级人数-1  TODO:日后完善传入实体类更新班级再修改
        musicClassMapper.reduceClassSize(classId);


    }

    /**
     * 学生 - 加入班级
     * @param classCode
     */
    public void joinClass(String classCode) {
        //1.根据班级码查询获取相应的班级ID
        long classId = musicClassMapper.queryClassByClassCode(classCode);
        log.info("班级ID:{}",classId);
        //2.根据线程获取当前学生ID
        long studentId = BaseContext.getCurrentId();
        log.info("当前线程学生ID:{}",studentId);
        //3.创建StudentClass类,用于查询与插入
        StudentClass studentClass = new StudentClass();
        studentClass.setClassId(classId);
        studentClass.setStudentId(studentId);

        //4.判断是否已经存在相关数据/超出某些限制
        //TODO:这里使用查询语句进行判断,判断是否已经存在了相关数据,是否有更好的解决方法?
        StudentClass studentClass1 = musicClassMapper.queryStudentClass(studentClass);
        if(studentClass1 != null){
            //TODO:重复,抛异常,全局接收?
            log.info("重复:{}",studentClass1);
            return;
        }

        //5.没有异常,将studenClass插入到 学生班级表 当中
        //TODO:异常处理
        musicClassMapper.saveStudentToClass(studentClass);

        //6.根据classId查询班级人数
        Integer classSize = musicClassMapper.getClassSizeByClassId(classId);

        //7.更新music_class中的班级人数
        log.info("班级人数加一");
        MusicClass musicClass = MusicClass.builder()
                .id(classId)
                .classSize(classSize + 1)
                .build();
        musicClassMapper.update(musicClass);


    }

    /**
     * 学生 - 根据班级id查询班级
     * @return
     */
    public List<MusicClassVO> queryByStudentId() {
        //1.先根据当前线程获取学生id
        Long studentId = BaseContext.getCurrentId();
        //2.利用学生id到 student_class 中查询班级id
        List<Long> classIdList = musicClassMapper.queryAttrClassByStudentId(studentId);
        //3.利用班级list到 music_class 中查询所有班级
        List<MusicClass> musicClassList = musicClassMapper.queryByClassIdList(classIdList);
        //4.将MusicClass转化为MusicClassVO
        List<MusicClassVO> musicClassVOList = new ArrayList<>();
        for (MusicClass musicClass : musicClassList) {
            //4.1通过教师Id查询其姓名
            String teacherName = teacherMapper.getNameById(musicClass.getTeacherId());
            //4.2计算班级授课天数
            Integer days = (int)ChronoUnit.DAYS.between(musicClass.getCreateTime(),LocalDateTime.now());
            MusicClassVO musicClassVO = MusicClassVO.builder()
                    .id(musicClass.getId())
                    .classSize(musicClass.getClassSize())
                    .className(musicClass.getClassName())
                    .instrument(musicClass.getInstrument())
                    .days(days)
                    .teacherName(teacherName)
                    .build();
            //4.3将VO添加进VOList
            musicClassVOList.add(musicClassVO);
        }
        return musicClassVOList;
    }


}
