package com.ruoyi.parking.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: yangyang
 * @Date: 2022/12/19/11:25
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkingChargingDto {
    private Long parkinglotinformationid;
    private Date startTime;
    private Date endTime;
    private String license;
}
