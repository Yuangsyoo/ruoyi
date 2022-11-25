package com.ruoyi.parking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: yangyang
 * @Date: 2022/11/22/15:56
 * 推送结果为车牌识别结果
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlarmInfoPlate {
        //默认通道号（预留）
        private int channel;
        //设备名称
        private  String deviceName;
        //设备 ip 地址
        private String ipaddr;
        //实际数据
        private Result result;
        //设备序列号，设备唯一
        private String serialno;

}
