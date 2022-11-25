package com.ruoyi.parking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: yangyang
 * @Date: 2022/11/22/17:03
 * @Description:
 * 车辆品牌
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarBrand {
        //车辆品牌
        private int brand;
        //车辆年份
        private int year;
        //车牌类型 0：未知车牌:、1：蓝牌小汽车、2：:黑牌小汽车、
        //3：单排黄牌、4：双排黄牌、 5：警车车牌、6：武警车
        //牌、7：个性化车牌、8：单排军车牌、9：双排军车牌、1
        //0：使馆车牌、11：香港进出中国大陆车牌、12：农用车
        private int type;
}
