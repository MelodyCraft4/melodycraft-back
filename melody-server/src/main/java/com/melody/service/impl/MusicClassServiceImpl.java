package com.melody.service.impl;

import com.melody.context.BaseContext;
import com.melody.dto.MusicClassDTO;
import com.melody.entity.MusicClass;
import com.melody.entity.Student;
import com.melody.mapper.MusicClassMapper;
import com.melody.mapper.StudentMapper;
import com.melody.service.MusicClassService;
import com.melody.vo.StudentQueryVO;
import com.melody.vo.StudentVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class MusicClassServiceImpl implements MusicClassService {

    @Autowired
    private MusicClassMapper musicClassMapper;

    @Autowired
    private StudentMapper studentMapper;

    /**
     * 新增班级
     * @param musicClassDTO
     */
    public void saveMusicClass(MusicClassDTO musicClassDTO,String inviteCode){
        MusicClass musicClass = new MusicClass();
        BeanUtils.copyProperties(musicClassDTO,musicClass);
        musicClass.setClassCode(inviteCode);

        //TODO:待aop完善下列信息
        musicClass.setClassSize(0);



        musicClassMapper.saveMusicClass(musicClass);
    }

    /**
     * 教师查询班级
     * @return
     */
    public List<MusicClass> query() {
        //根据线程获取当前教师id
        Long id = BaseContext.getCurrentId();
        List<MusicClass> list = musicClassMapper.query(id);
        return list;
    }

    /**
     * 教师查询班级下的学生(根据姓名)
     * @param name
     * @return
     */
    @Override
    public List<StudentQueryVO> queryStudentByName(String name,Long classId){
        if (name==null){  //前端没有传入姓名数据
            name="";
        }

        //根据姓名学生信息
        List<StudentQueryVO> list = musicClassMapper.queryStudentByName(name,classId);
        return list;
    }

    /**
     *  根据id查询显示学生信息
     * @param id
     * @return
     */
    @Override
    public StudentVO showStudentById(Long id) {
        StudentVO studentVO = new StudentVO();

        //1.先根据id查询学生信息
        Student student = studentMapper.getById(id);

        //2.属性拷贝
        BeanUtils.copyProperties(student,studentVO);

        //把年龄算出来也插入到studentVO中
        LocalDate birthday = student.getBirthday();
        int age = LocalDate.now().getYear() - birthday.getYear();
        studentVO.setAge(age);

        //3.根据学生id查询班级信息（乐器、班级）
        ArrayList<MusicClass> classList = musicClassMapper.getByStudentId(id);

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
     * 根据学生id和班级id将学生移出班级
     * @param studentId
     * @param classId
     */
    @Override
    public void deleteStudentById(Long studentId, Long classId) {

        //1.根据学生id和班级id将学生移出班级
        musicClassMapper.deleteStudentById(studentId,classId);

        //2.班级人数-1  TODO:日后完善传入实体类更新班级再修改
        musicClassMapper.reduceClassSize(classId);
    }
}
