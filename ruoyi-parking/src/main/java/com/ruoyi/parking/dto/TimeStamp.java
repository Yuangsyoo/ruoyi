package com.ruoyi.parking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: yangyang
 * @Date: 2022/11/22/16:55
 * @Description:识别结果对应帧的时间戳
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeStamp {
    private Timeval timeval;
}
