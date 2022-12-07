package com.ruoyi.common.utils.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: yangyang
 * @Date: 2022/11/24/13:53
 * @Description:
 */
public class DateTime {

    public static Date combineTime2(int y,int m,int d,int h,int m2,int s) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        Date date =null;
        try {
            date = sdf.parse(y + "-" + m + "-" + d + " " + h + ":" + m2 + ":" + s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
            return date;
    }

    public static long dateDiff(Date startTime, Date endTime)  {
        //按照传入的格式生成一个simpledateformate对象

        long nd = 1000*24*60*60;//一天的毫秒数
        long nh = 1000*60*60;//一小时的毫秒数
        long nm = 1000*60;//一分钟的毫秒数
        long ns = 1000;//一秒钟的毫秒数
        long diff;
        //获得两个时间的毫秒时间差异
        diff = endTime.getTime() -startTime.getTime();
        long day = diff/nd;//计算差多少天
        long hour = diff%nd/nh;//计算差多少小时
        long min = diff%nd%nh/nm;//计算差多少分钟
        long sec = diff%nd%nh%nm/ns;//计算差多少秒//输出结果
        System.out.println("时间相差："+day+"天"+hour+"小时"+min+"分钟"+sec+"秒。");
        return min ;
    }

}
