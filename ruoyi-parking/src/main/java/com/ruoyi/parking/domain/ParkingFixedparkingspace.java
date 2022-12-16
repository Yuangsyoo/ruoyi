package com.ruoyi.parking.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 停车场固定车位对象 parking_fixedparkingspace
 * 
 * @author ruoyi
 * @date 2022-12-13
 */
public class ParkingFixedparkingspace extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 序列号 */
    private Long id;

    /** 停车场 */
    @Excel(name = "停车场")
    private Long parkinglotinformationid;

    /** 区域 */
    @Excel(name = "区域")
    private String region;

    /** 车位号 */
    @Excel(name = "车位号")
    private String parkingspacenumber;

    /** 业主姓名 */
    @Excel(name = "业主姓名")
    private String nameofemployer;

    /** 电话 */
    @Excel(name = "电话")
    private String phone;

    /** 绑定车牌 */
    @Excel(name = "绑定车牌")
    private String license;

    /** 有效期起 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "有效期起", width = 30, dateFormat = "yyyy-MM-dd")
    private Date starttime;

    /** 有效期止 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "有效期止", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endtime;

    /** 占用状态0未占用1已占用 */
    @Excel(name = "占用状态0未占用1已占用")
    private Integer state;

    /** 占用车牌 */
    @Excel(name = "占用车牌")
    private String occupationoflicenseplate;

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
    public void setRegion(String region) 
    {
        this.region = region;
    }

    public String getRegion() 
    {
        return region;
    }
    public void setParkingspacenumber(String parkingspacenumber) 
    {
        this.parkingspacenumber = parkingspacenumber;
    }

    public String getParkingspacenumber() 
    {
        return parkingspacenumber;
    }
    public void setNameofemployer(String nameofemployer) 
    {
        this.nameofemployer = nameofemployer;
    }

    public String getNameofemployer() 
    {
        return nameofemployer;
    }
    public void setPhone(String phone) 
    {
        this.phone = phone;
    }

    public String getPhone() 
    {
        return phone;
    }
    public void setLicense(String license) 
    {
        this.license = license;
    }

    public String getLicense() 
    {
        return license;
    }
    public void setStarttime(Date starttime) 
    {
        this.starttime = starttime;
    }

    public Date getStarttime() 
    {
        return starttime;
    }
    public void setEndtime(Date endtime) 
    {
        this.endtime = endtime;
    }

    public Date getEndtime() 
    {
        return endtime;
    }
    public void setState(Integer state) 
    {
        this.state = state;
    }

    public Integer getState() 
    {
        return state;
    }
    public void setOccupationoflicenseplate(String occupationoflicenseplate) 
    {
        this.occupationoflicenseplate = occupationoflicenseplate;
    }

    public String getOccupationoflicenseplate() 
    {
        return occupationoflicenseplate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("parkinglotinformationid", getParkinglotinformationid())
            .append("region", getRegion())
            .append("parkingspacenumber", getParkingspacenumber())
            .append("nameofemployer", getNameofemployer())
            .append("phone", getPhone())
            .append("license", getLicense())
            .append("starttime", getStarttime())
            .append("endtime", getEndtime())
            .append("state", getState())
            .append("occupationoflicenseplate", getOccupationoflicenseplate())
            .toString();
    }
}
