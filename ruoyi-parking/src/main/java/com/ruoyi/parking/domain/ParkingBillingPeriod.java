package com.ruoyi.parking.domain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 计费时间段对象 parking_billing_period
 * 
 * @author ruoyi
 * @date 2022-12-19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ParkingBillingPeriod extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 计费规则id */
    private Long parkingchargingid;

    /** 开始时间 */
    @Excel(name = "开始时间")
    private String startime;

    /** 结束时间 */
    @Excel(name = "结束时间")
    private String endtime;

    /** 收费金额或者每小时加收 */
    @Excel(name = "收费金额或者每小时加收")
    private Long addmoney;

    /** 起步价 */
    @Excel(name = "起步价")
    private Long startingprice;

    /** 起步时长 */
    @Excel(name = "起步时长")
    private Long startingtime;

    /** 按时记或者一口价 */
    @Excel(name = "按时记或者一口价")
    private Integer type;

    /** 是否重复收取起步价 */
    @Excel(name = "是否重复收取起步价")
    private Integer repea;
    @Excel(name = "分钟计费")
    private Long minutecharge;
    @Excel(name = "单日上现")
    private Long superiorlimit;

}
