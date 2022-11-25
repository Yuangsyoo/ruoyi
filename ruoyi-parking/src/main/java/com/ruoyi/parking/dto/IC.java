package com.ruoyi.parking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: yangyang
 * @Date: 2022/11/22/18:45
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IC {
    //IO OUT 序号，当前最大 4 个 IOout
    private int ionum;
    //开闸类型：HTTP_IO_OUT_STATUS
    private int ctrltype;
}
