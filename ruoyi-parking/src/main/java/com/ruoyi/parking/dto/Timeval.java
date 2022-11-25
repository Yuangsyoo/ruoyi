package com.ruoyi.parking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: yangyang
 * @Date: 2022/11/22/16:22
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Timeval {
            //时间，天
            private int  decday;
            //时间，时
            private int  dechour;
            //时间，分钟
            private int  decmin;
            //时间，月
            private int  decmon;
            //时间，秒
            private int  decsec;
            //时间，年
            private int  decyear;
            //从 1970 年 1 月 1 日到对应帧的秒
            private  int  sec;
            //从 1970 年 1 月 1 日到对应帧的毫秒
            private int   usec;
}
