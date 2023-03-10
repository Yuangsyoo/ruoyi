package com.ruoyi.parking.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: yangyang
 * @Date: 2023/01/03/8:23
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SerialData {
    //串口的通道号，通道 0 为 485 口 1，通道 1 根据跳线方式为 485 口 2或者 232
    private String  serialChannel;
    //串口数据
    private String  data;
    //串口数据实际长度
    private int  dataLen;
}
