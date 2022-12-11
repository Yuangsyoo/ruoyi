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
 * @Date: 2022/11/22/16:05
 * @Description:实际数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    @JsonProperty("PlateResult")//解决首字母大写json和javabean转换接收不到值的问题
    @JSONField(name = "PlateResult")
    private  PlateResult plateResult;
}
