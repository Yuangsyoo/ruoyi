package com.ruoyi.common.core.domain.entity;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 停车场管理对象 parking_lot_information
 * 
 * @author ruoyi
 * @date 2022-11-23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ParkingLotInformation extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id
 */
    private Long id;

    /** 停车场名称 */
    @Excel(name = "停车场名称")
    private String name;

    /** 门面照 */
    @Excel(name = "门面照")
    private String picture;

    /** 联系人 */
    @Excel(name = "联系人")
    private String contacts;

    /** 联系电话 */
    @Excel(name = "联系电话")
    private String phone;

    /** QQ号 */
    @Excel(name = "QQ号")
    private String qq;

    /** 地址 */
    @Excel(name = "地址")
    private String address;

    /** 停车场状态0开启1关闭 */
    @Excel(name = "停车场状态0开启1关闭")
    private String state;

    /** 车位个数 */
    @Excel(name = "车位个数")
    private Long number;

    /** 支付离场时间 */
    @Excel(name = "支付离场时间")
    private Integer payleavingtime;

    /** 默认月费 */
    @Excel(name = "默认月费")
    private Long monthlyfee;

    /** 收费说明不参与计算 */
    @Excel(name = "收费说明不参与计算")
    private String chargedescription;

    /** 营业起使时间 */

    @Excel(name = "营业起使时间")
    private String starttime;

    /** 营业结束时间 */

    @Excel(name = "营业结束时间")
    private String endtime;

    /** 月卡购买最小月数 */
    @Excel(name = "月卡购买最小月数")
    private Integer minmonths;

    /** 月卡购买最大月数 */
    @Excel(name = "月卡购买最大月数")
    private Integer maxmonths;

    /** 是否开启月卡购买0开启1关闭 */
    @Excel(name = "是否开启月卡购买0开启1关闭")
    private String monthlycardpurchase;

    /** 是否开启临时车限制0开启1关闭 */
    @Excel(name = "是否开启临时车限制0开启1关闭")
    private String temporaryvehiclerestrictions;

    /** 是否开启平台支付0开启1关闭 */
    @Excel(name = "是否开启平台支付0开启1关闭")
    private String platformpaymentState;

    /** 超时补费0开启1关闭 */
    @Excel(name = "超时补费0开启1关闭")
    private String overtimecompensation;

    /** 无记录离场收起步价0开启1关闭 */
    @Excel(name = "无记录离场收起步价0开启1关闭")
    private String norecorddeparture;

    /** 是否启用支付宝停车缴费0开启1关闭 */
    @Excel(name = "是否启用支付宝停车缴费0开启1关闭")
    private String alipaypaymentState;

    /** 是否启用微信停车缴费0开启1关闭 */
    @Excel(name = "是否启用微信停车缴费0开启1关闭")
    private String wechatpaymentState;

    /** 是否启用银联停车缴费0开启1关闭 */
    @Excel(name = "是否启用银联停车缴费0开启1关闭")
    private String unionpaypaymentState;

    /** 是否启用ETC停车缴费0开启1关闭 */
    @Excel(name = "是否启用ETC停车缴费0开启1关闭")
    private String etcpaymentState;

    /** 是否启用农信停车缴费0开启1关闭 */
    @Excel(name = "是否启用农信停车缴费0开启1关闭")
    private String ruralcreditpaymentState;

    /** 平台支付关联id */
    @Excel(name = "平台支付关联id")
    private Long platformpaymentId;

    /** 支付宝支付关联id */
    @Excel(name = "支付宝支付关联id")
    private Long alipaypaymentId;

    /** 微信支付关联id */
    @Excel(name = "微信支付关联id")
    private Long wechatpaymentId;

    /** 银联支付关联id */
    @Excel(name = "银联支付关联id")
    private Long unionpaypaymentId;

    /** ETC支付关联id */
    @Excel(name = "ETC支付关联id")
    private Long etcpaymentId;

    /** 农信支付关联id */
    @Excel(name = "农信支付关联id")
    private Long ruralcreditpaymentId;
    /** 备用字段2 */
    @Excel(name = "剩余车位")
    private Long remainingParkingSpace;

    @Excel(name = "免费时常")
    private Long freetime;

    /** 备用字段4 */
    @Excel(name = "备用字段4")
    private String numberfour;


}
