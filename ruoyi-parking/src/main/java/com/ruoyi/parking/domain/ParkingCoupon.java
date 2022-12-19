package com.ruoyi.parking.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.entity.ParkingLotInformation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 优惠卷对象 parking_coupon
 * 
 * @author ruoyi
 * @date 2022-12-12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkingCoupon extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 序号 */
    private Long id;

    /** 停车场id */
    @Excel(name = "停车场id")
    private Long parkinglotinformationid;

    /** 0代表次卷1代表小时卷2代表金额卷 */
    @Excel(name = "0代表次卷1代表小时卷2代表金额卷")
    private Integer type;

    /** 优惠卷名称 */
    @Excel(name = "优惠卷名称")
    private String name;

    /** 数量 */
    @Excel(name = "数量")
    private Long count;

    /** 优惠卷使用时间期限0代表当天，1代表二维码结束期限 */
    @Excel(name = "优惠卷使用时间期限0代表当天，1代表二维码结束期限")
    private Integer usetype;

    /** 二维码开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "二维码开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date qrcodestarttime;

    /** 二维码结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "二维码结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date qrcodeendtime;

    /** 领取数量 */
    @Excel(name = "领取数量")
    private Long receiveccount;

    /** 状态 */
    @Excel(name = "状态")
    private Integer state;

    /** 二维码 */
    @Excel(name = "二维码")
    private String qrcode;

    /** 优惠面值 单位小时  优惠卷必须是小时卷 */
    @Excel(name = "优惠面值 单位小时  优惠卷必须是小时卷")
    private Integer preferentialfacevalue;

    /** 优惠金额 优惠卷必须是金额卷 */
    @Excel(name = "优惠金额 优惠卷必须是金额卷")
    private String discountamount;

    private ParkingLotInformation parkingLotInformation;



}
