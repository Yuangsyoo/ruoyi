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
 * @Date: 2022/12/27/10:06
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyInformationVo {
    private List<String> name=new ArrayList<>();
    private List<ParkingLots> parkingLots=new ArrayList<>();
}
