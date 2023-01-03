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
    private List<SerialPort>list=new ArrayList();
}
