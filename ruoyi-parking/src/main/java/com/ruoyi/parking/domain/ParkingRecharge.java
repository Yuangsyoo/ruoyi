package com.ruoyi.parking.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.entity.ParkingLotInformation;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 充值车管理对象 parking_recharge
 * 
 * @author ruoyi
 * @date 2023-02-01
 */
public class ParkingRecharge extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 序列号 */
    private Long id;

    /** 停车场 */
    @Excel(name = "停车场")
    private Long parkinglotinformationid;

    /** 车牌号 */
    @Excel(name = "车牌号")
    private String license;

    /** 余额 */
    @Excel(name = "余额")
    private Long balance;

    /** 姓名 */
    @Excel(name = "姓名")
    private String name;

    /** 地址 */
    @Excel(name = "地址")
    private String address;

    /** 备注 */
    @Excel(name = "备注")
    private String remarks;

    /** 添加时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "添加时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date addtime;

    /** 本次充值金额 */
    @Excel(name = "本次充值金额")
    private Long money;
    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    private ParkingLotInformation parkingLotInformation;

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
    public void setBalance(Long balance) 
    {
        this.balance = balance;
    }

    public Long getBalance() 
    {
        return balance;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setAddress(String address) 
    {
        this.address = address;
    }

    public String getAddress() 
    {
        return address;
    }
    public void setRemarks(String remarks) 
    {
        this.remarks = remarks;
    }

    public String getRemarks() 
    {
        return remarks;
    }
    public void setAddtime(Date addtime) 
    {
        this.addtime = addtime;
    }

    public Date getAddtime() 
    {
        return addtime;
    }
    public void setMoney(Long money) 
    {
        this.money = money;
    }

    public Long getMoney() 
    {
        return money;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("parkinglotinformationid", getParkinglotinformationid())
            .append("license", getLicense())
            .append("balance", getBalance())
            .append("name", getName())
            .append("address", getAddress())
            .append("remarks", getRemarks())
            .append("addtime", getAddtime())
            .append("money", getMoney())
            .toString();
    }
}
