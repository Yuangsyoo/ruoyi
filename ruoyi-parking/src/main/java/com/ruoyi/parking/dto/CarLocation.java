package com.ruoyi.parking.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: yangyang
 * @Date: 2022/11/22/17:17
 * @Description:车头位置
 * 车头位置
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarLocation {
    //位置为矩形区域；left\right\top\bottom:车牌在图片中位
    // * 置
    @JsonProperty("RECT")//解决首字母大写json和javabean转换接收不到值的问题
    @JSONField(name = "RECT")
    private RECT rect;
}
