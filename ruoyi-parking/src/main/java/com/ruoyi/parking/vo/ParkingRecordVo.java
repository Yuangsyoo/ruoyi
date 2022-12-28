package com.ruoyi.parking.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.entity.ParkingLotInformation;
import com.ruoyi.parking.domain.ParkingCoupon;
import com.ruoyi.parking.domain.ParkingCouponrecord;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: yangyang
 * @Date: 2022/12/15/10:21
 * @Description:
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ParkingRecordVo implements Serializable {

    private String license;


    private Long parkinglotinformationid;
    private Long  parkinglotequipmentid;
    /** 入场时间 */


    private Date admissiontime;


    private Date exittime;

    /** 订单编号 */

    private String ordernumber;
    /** 支付金额 */

    private Long money;

    /** 出入口名称 */

    private String entranceandexitname;
    /*优惠金额*/

    private Long discountamount;
    /*应缴金额*/

    private Long amountpayable;
    /*支付方式*/
    private  String paymentmethod;
    //停车场名称
    private String parkingLotInformationName;
    //出口设备名称
    private String parkingLotEquipmentName;

    private String paymentMethod;

}
