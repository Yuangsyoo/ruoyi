package com.ruoyi.parking.dto;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.entity.ParkingLotInformation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: yangyang
 * @Date: 2022/12/02/13:50
 * @Description:
 */
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class ParkingLotEquipmentDto {

    private Long id;

    /** 停车场名称 */

    private String name;
    private String loginPassword;

    /** 门面照 */

    private String picture;

    /** 联系人 */

    private String contacts;

    /** 联系电话 */

    private String phone;

    /** QQ号 */

    private String qq;

    /** 地址 */

    private String address;

    /** 停车场状态0开启1关闭 */

    private String state;

    /** 车位个数 */

    private Long number;

    /** 支付离场时间 */

    private Integer payleavingtime;

    /** 默认月费 */

    private Long monthlyfee;

    /** 收费说明不参与计算 */

    private String chargedescription;

    /** 营业起使时间 */


    private String starttime;

    /** 营业结束时间 */


    private String endtime;

    /** 月卡购买最小月数 */

    private Integer minmonths;

    /** 月卡购买最大月数 */

    private Integer maxmonths;

    /** 是否开启月卡购买0开启1关闭 */

    private String monthlycardpurchase;

    /** 是否开启临时车限制0开启1关闭 */

    private String temporaryvehiclerestrictions;

    /** 是否开启平台支付0开启1关闭 */

    private String platformpaymentState;

    /** 超时补费0开启1关闭 */

    private String overtimecompensation;

    /** 无记录离场收起步价0开启1关闭 */

    private String norecorddeparture;

    /** 是否启用支付宝停车缴费0开启1关闭 */

    private String alipaypaymentState;

    /** 是否启用微信停车缴费0开启1关闭 */

    private String wechatpaymentState;

    /** 是否启用银联停车缴费0开启1关闭 */

    private String unionpaypaymentState;

    /** 是否启用ETC停车缴费0开启1关闭 */

    private String etcpaymentState;

    /** 是否启用农信停车缴费0开启1关闭 */

    private String ruralcreditpaymentState;

    /** 平台支付关联id */

    private Long platformpaymentId;

    /** 支付宝支付关联id */

    private Long alipaypaymentId;

    /** 微信支付关联id */

    private Long wechatpaymentId;

    /** 银联支付关联id */

    private Long unionpaypaymentId;

    /** ETC支付关联id */

    private Long etcpaymentId;

    /** 农信支付关联id */

    private Long ruralcreditpaymentId;
    /** 备用字段2 */

    private Long remainingParkingSpace;


    /** 备用字段4 */

    private String numberfour;



}
