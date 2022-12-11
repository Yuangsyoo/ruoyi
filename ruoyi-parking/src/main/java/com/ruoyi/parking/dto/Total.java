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
 * @Date: 2022/11/22/20:23
 * @Description:车牌识别结果推送对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Total {
        @JsonProperty("AlarmInfoPlate")//解决首字母大写json和javabean转换接收不到值的问题
        @JSONField(name = "AlarmInfoPlate")
        private AlarmInfoPlate alarmInfoPlate;


}
