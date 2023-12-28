package com.melody.utils;


/**
 * 邀请码工具类
 */
public class InviteCodeUtil {

    //生成一共随机邀请码
    public static String genInviteCode(Long id)
    {
        //获取系统当前时间：秒的级别
        long time = System.currentTimeMillis() / 1000;
        String s = Long.toString(time) + Long.toString(id);
        //将字符串转化为16进制表示
        String hex = Long.toHexString(Long.parseLong(s));
        return hex;
    }
}
