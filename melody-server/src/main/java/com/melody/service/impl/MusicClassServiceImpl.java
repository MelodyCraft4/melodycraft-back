package com.melody.service.impl;

import com.melody.constant.MessageConstant;
import com.melody.context.BaseContext;
import com.melody.dto.MusicClassDTO;
import com.melody.entity.*;
import com.melody.exception.BaseException;
import com.melody.mapper.HomeworkMapper;
import com.melody.mapper.MusicClassMapper;
import com.melody.mapper.StudentMapper;
import com.melody.mapper.TeacherMapper;
import com.melody.service.MusicClassService;
import com.melody.vo.*;
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

    @Autowired
    private HomeworkMapper homeworkMapper;

    /**
     * 教师 - 新增班级
     * @param musicClassDTO
     */
    public void saveMusicClass(MusicClassDTO musicClassDTO,String inviteCode){
        //创建班级,附上初始值
        MusicClass musicClass = new MusicClass();
        BeanUtils.copyProperties(musicClassDTO,musicClass);
        musicClass.setClassCode(inviteCode);
        musicClass.setTeacherId(BaseContext.getCurrentId());
        musicClass.setClassSize(0);
        //数据库添加数据
        musicClassMapper.saveMusicClass(musicClass);
    }

    /**
     * 教师 - 根据教师id查询班级
     * @return
     */
    public List<MusicClassVO> queryByTeacherId() {
        //根据线程获取当前教师id
        Long teacherId = BaseContext.getCurrentId();
        List<MusicClassVO> list = musicClassMapper.queryAttrClassByTeacherId(teacherId);
        log.info("班级表VO:{}",list);
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
        //性别转换
        studentVO.setSex(student.getSex()==1?"男":"女");

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
        //TODO:管理员审核
        //1.根据学生id和班级id将学生移出班级
        musicClassMapper.deleteStudentById(studentId,classId);

        //2.班级人数-1
        musicClassMapper.reduceClassSize(classId);


    }

    /**
     * 学生 - 加入班级
     * @param classCode
     */
    public void joinClass(String classCode) {
        //1.根据班级码查询获取相应的班级ID
        Long classId = musicClassMapper.queryClassByClassCode(classCode);
        log.info("班级ID:{}",classId);
        if(classId == null){
            throw new BaseException(MessageConstant.CLASSCODE_NOT_FOUND);
        }
        //2.根据线程获取当前学生ID
        long studentId = BaseContext.getCurrentId();
        log.info("当前线程学生ID:{}",studentId);
        //3.创建StudentClass类,用于查询与插入
        StudentClass studentClass = new StudentClass();
        studentClass.setClassId(classId);
        studentClass.setStudentId(studentId);

        //4.判断是否已经存在该学生
        StudentClass studentClass1 = musicClassMapper.queryStudentClass(studentClass);
        if(studentClass1 != null){
            log.info("重复:{}",studentClass1);
            throw new BaseException(MessageConstant.ALREADY_ENTER);
        }

        //5.没有异常,将studenClass插入到 学生班级表 当中
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

        //8.将班级内作业赋予该学生
        //8.1查询班级内所有作业
        List<Homework> homeworkList = homeworkMapper.query(classId);
        homeworkMapper.giveClassHomework(homeworkList,studentId);


    }

    /**
     * 学生 - 根据班级id查询所有班级
     * @return
     */
    public List<MusicClassVO> queryByStudentId() {
        //1.先根据当前线程获取学生id
        Long studentId = BaseContext.getCurrentId();
        //2.利用学生id到 student_class 中查询班级id
        List<Long> classIdList = musicClassMapper.queryAttrClassByStudentId(studentId);
        //如果学生没有加入班级,返回空
        if (classIdList.size() == 0){
            return new ArrayList<>();
        }
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

    /**
     * 管理端: 查询所有班级(可根据name具体查询)
     * @param name 非必须
     * @return
     */
    public List<MusicClassVO> queryClassFromAdmin(String name) {
        if(name == null){
            name = "";
        }
        List<MusicClassVO> list = musicClassMapper.queryClassFromAdmin(name);
        log.info("查询的班级:{}",list);
        return list;
    }

    /**
     * 管理员端 查询教师概况(根据班级id)
     * @return
     */
    public TeacherQueryVO queryClassTch(Long classId) {
        TeacherQueryVO teacherQueryVO = musicClassMapper.queryClassTch(classId);
        return teacherQueryVO;
    }

    /**
     * 管理员端 查询教师概况(根据其id)
     * @param teacherId 传入教师id
     * @return
     */
    public TeacherVO queryClassTchDetail(Long teacherId) {
        //调用teacherMapper层接口,查询教师信息
        Teacher teacher = teacherMapper.queryTchById(teacherId);
        //创建teachervo,并进行属性拷贝
        TeacherVO teacherVO = new TeacherVO();
        BeanUtils.copyProperties(teacher,teacherVO);
        //性别转换
        teacherVO.setSex(teacher.getSex()==1?"男":"女");
        return teacherVO;
    }


}
