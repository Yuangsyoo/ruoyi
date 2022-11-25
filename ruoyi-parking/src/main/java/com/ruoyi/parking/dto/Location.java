package com.ruoyi.parking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: yangyang
 * @Date: 2022/11/22/17:01
 * @Description:车牌在图片中位置
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    //位置为矩形区域；left\right\top\bottom:车牌在图片中位
    // * 置
    private RECT rect;
}
