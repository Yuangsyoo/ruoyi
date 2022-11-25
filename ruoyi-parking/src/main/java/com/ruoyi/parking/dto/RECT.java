package com.ruoyi.parking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: yangyang
 * @Date: 2022/11/22/16:57
 * @Description:
 * 位置为矩形区域；left\right\top\bottom:车牌在图片中位
 * 置
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RECT {

    private int bottom;
    private int left;
    private int right;
    private int top;
}