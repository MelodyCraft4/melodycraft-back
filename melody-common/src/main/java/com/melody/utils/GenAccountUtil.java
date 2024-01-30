package com.melody.utils;




import java.time.LocalDate;
import java.time.Month;
import java.time.Year;

/**
 * 生成账号工具类
 */
public class GenAccountUtil {

    /**
     * 生成教师账号
     * @param num
     * @return
     */
    public static String GenTeacherAccount(Integer num){
        //第一个字母:T + 年份后两位(如22,23等等) + 月份(02/05/11) + 5位数字(顺序)
        return  "T" + Year.now().getValue() % 100 + String.format("%02d", LocalDate.now().getMonthValue()) + String.format("%05d", num);
    }

    /**
     *  生成学生账号
     * @param num
     * @return
     */
    public static String GenStudentAccount(Integer num){
        //第一个字母:S + 年份后两位(如22,23等等) + 月份(02/05/11) + 5位数字(顺序)
        return  "S" + Year.now().getValue() % 100 + String.format("%02d", LocalDate.now().getMonthValue()) + String.format("%05d", num);
    }

}
