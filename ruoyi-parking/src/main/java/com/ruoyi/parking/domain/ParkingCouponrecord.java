package com.ruoyi.parking.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.entity.ParkingLotInformation;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 停车场优惠卷记录对象 parking_couponrecord
 * 
 * @author ruoyi
 * @date 2022-12-12
 */
public class ParkingCouponrecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 序列号 */
    private Long id;

    /** 优惠卷 */
    @Excel(name = "优惠卷")
    private Long parkingcouponid;

    /** 停车场 */
    @Excel(name = "停车场")
    private Long parkinglotinformationid;

    /** 车牌 */
    @Excel(name = "车牌")
    private String license;

    /** 订单号 */
    @Excel(name = "订单号")
    private String ordernumber;

    /** 领取时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+8")
    @Excel(name = "领取时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date time;

    /** 使用状态0代表未使用1代表已使用 */
    @Excel(name = "使用状态0代表未使用1代表已使用")
    private String state;

    private ParkingCoupon parkingCoupon;

    private ParkingLotInformation parkingLotInformation;


    public ParkingCoupon getParkingCoupon() {
        return parkingCoupon;
    }

    public void setParkingCoupon(ParkingCoupon parkingCoupon) {
        this.parkingCoupon = parkingCoupon;
    }

    public ParkingLotInformation getParkingLotInformation() {
        return parkingLotInformation;
    }

    public void setParkingLotInformation(ParkingLotInformation parkingLotInformation) {
        this.parkingLotInformation = parkingLotInformation;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setParkingcouponid(Long parkingcouponid) 
    {
        this.parkingcouponid = parkingcouponid;
    }

    public Long getParkingcouponid() 
    {
        return parkingcouponid;
    }
    public void setParkinglotinformationid(Long parkinglotinformationid) 
    {
        this.parkinglotinformationid = parkinglotinformationid;
    }

    public Long getParkinglotinformationid() 
    {
        return parkinglotinformationid;
    }
    public void setLicense(String license) 
    {
        this.license = license;
    }

    public String getLicense() 
    {
        return license;
    }
    public void setOrdernumber(String ordernumber) 
    {
        this.ordernumber = ordernumber;
    }

    public String getOrdernumber() 
    {
        return ordernumber;
    }
    public void setTime(Date time) 
    {
        this.time = time;
    }

    public Date getTime() 
    {
        return time;
    }
    public void setState(String state) 
    {
        this.state = state;
    }

    public String getState() 
    {
        return state;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("parkingcouponid", getParkingcouponid())
            .append("parkinglotinformationid", getParkinglotinformationid())
            .append("license", getLicense())
            .append("ordernumber", getOrdernumber())
            .append("time", getTime())
            .append("state", getState())
            .toString();
    }
}
