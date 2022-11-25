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
}
