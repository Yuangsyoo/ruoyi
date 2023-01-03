package com.ruoyi.parking.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: yangyang
 * @Date: 2023/01/03/8:19
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Response_AlarmInfoPlate {
    //回复 ok 开闸
    private String info;
    // 当前车牌 id
    private String plateid;
    //回复开闸端口号，若无，则默认为 0
    private Long channelNum=0L;
    //回复 ok 进行手动触发
    private String manualTrigger;
    //是否支付
    private Boolean is_pay;
    //回复串口数据可以发送到相应串口
    private SerialData serialData;




}
