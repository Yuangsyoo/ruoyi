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

    public ParkingCoupon() {
    }

    public ParkingCoupon(Long id, Long parkinglotinformationid, Integer type, String name, Long count, Integer usetype, Date qrcodestarttime, Date qrcodeendtime, Long receiveccount, Integer state, String qrcode, Integer preferentialfacevalue, String discountamount, ParkingLotInformation parkingLotInformation) {
        this.id = id;
        this.parkinglotinformationid = parkinglotinformationid;
        this.type = type;
        this.name = name;
        this.count = count;
        this.usetype = usetype;
        this.qrcodestarttime = qrcodestarttime;
        this.qrcodeendtime = qrcodeendtime;
        this.receiveccount = receiveccount;
        this.state = state;
        this.qrcode = qrcode;
        this.preferentialfacevalue = preferentialfacevalue;
        this.discountamount = discountamount;
        this.parkingLotInformation = parkingLotInformation;
    }

    private ParkingLotInformation parkingLotInformation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParkinglotinformationid() {
        return parkinglotinformationid;
    }

    public void setParkinglotinformationid(Long parkinglotinformationid) {
        this.parkinglotinformationid = parkinglotinformationid;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Integer getUsetype() {
        return usetype;
    }

    public void setUsetype(Integer usetype) {
        this.usetype = usetype;
    }

    public Date getQrcodestarttime() {
        return qrcodestarttime;
    }

    public void setQrcodestarttime(Date qrcodestarttime) {
        this.qrcodestarttime = qrcodestarttime;
    }

    public Date getQrcodeendtime() {
        return qrcodeendtime;
    }

    public void setQrcodeendtime(Date qrcodeendtime) {
        this.qrcodeendtime = qrcodeendtime;
    }

    public Long getReceiveccount() {
        return receiveccount;
    }

    public void setReceiveccount(Long receiveccount) {
        this.receiveccount = receiveccount;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public Integer getPreferentialfacevalue() {
        return preferentialfacevalue;
    }

    public void setPreferentialfacevalue(Integer preferentialfacevalue) {
        this.preferentialfacevalue = preferentialfacevalue;
    }

    public String getDiscountamount() {
        return discountamount;
    }

    public void setDiscountamount(String discountamount) {
        this.discountamount = discountamount;
    }

    public ParkingLotInformation getParkingLotInformation() {
        return parkingLotInformation;
    }

    public void setParkingLotInformation(ParkingLotInformation parkingLotInformation) {
        this.parkingLotInformation = parkingLotInformation;
    }
}
