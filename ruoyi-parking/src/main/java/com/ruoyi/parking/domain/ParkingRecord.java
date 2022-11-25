package com.ruoyi.parking.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 停车记录对象 parking_record
 * 
 * @author ruoyi
 * @date 2022-11-24
 */
public class ParkingRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 车牌号 */
    @Excel(name = "车牌号")
    private String license;

    /** 停车场id */
    @Excel(name = "停车场id")
    private Long parkinglotinformationid;

    /** 入场时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "入场时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date admissiontime;

    /** 出场时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "出场时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date exittime;

    /** 车牌颜色 */
    @Excel(name = "车牌颜色")
    private String licensepllatecolor;

    /** 订单编号 */
    @Excel(name = "订单编号")
    private String ordernumber;

    /** 订单状态0代表未支付1代表已支付 */
    @Excel(name = "订单状态0代表未支付1代表已支付")
    private String orderstate;

    /** 支付状态0代表未支付1代表已支付 */
    @Excel(name = "支付状态0代表未支付1代表已支付")
    private String paystate;

    /** 支付金额 */
    @Excel(name = "支付金额")
    private Long money;

    /** 出入口名称 */
    @Excel(name = "出入口名称")
    private String entranceandexitname;

    /** 预留字段1 */
    @Excel(name = "预留字段1")
    private String numberone;

    /** 预留字段2 */
    @Excel(name = "预留字段2")
    private String numbertwo;

    /** 预留字段3 */
    @Excel(name = "预留字段3")
    private String numberthree;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setLicense(String license) 
    {
        this.license = license;
    }

    public String getLicense() 
    {
        return license;
    }
    public void setParkinglotinformationid(Long parkinglotinformationid) 
    {
        this.parkinglotinformationid = parkinglotinformationid;
    }

    public Long getParkinglotinformationid() 
    {
        return parkinglotinformationid;
    }
    public void setAdmissiontime(Date admissiontime) 
    {
        this.admissiontime = admissiontime;
    }

    public Date getAdmissiontime() 
    {
        return admissiontime;
    }
    public void setExittime(Date exittime) 
    {
        this.exittime = exittime;
    }

    public Date getExittime() 
    {
        return exittime;
    }
    public void setLicensepllatecolor(String licensepllatecolor) 
    {
        this.licensepllatecolor = licensepllatecolor;
    }

    public String getLicensepllatecolor() 
    {
        return licensepllatecolor;
    }
    public void setOrdernumber(String ordernumber) 
    {
        this.ordernumber = ordernumber;
    }

    public String getOrdernumber() 
    {
        return ordernumber;
    }
    public void setOrderstate(String orderstate) 
    {
        this.orderstate = orderstate;
    }

    public String getOrderstate() 
    {
        return orderstate;
    }
    public void setPaystate(String paystate) 
    {
        this.paystate = paystate;
    }

    public String getPaystate() 
    {
        return paystate;
    }
    public void setMoney(Long money) 
    {
        this.money = money;
    }

    public Long getMoney() 
    {
        return money;
    }
    public void setEntranceandexitname(String entranceandexitname) 
    {
        this.entranceandexitname = entranceandexitname;
    }

    public String getEntranceandexitname() 
    {
        return entranceandexitname;
    }
    public void setNumberone(String numberone) 
    {
        this.numberone = numberone;
    }

    public String getNumberone() 
    {
        return numberone;
    }
    public void setNumbertwo(String numbertwo) 
    {
        this.numbertwo = numbertwo;
    }

    public String getNumbertwo() 
    {
        return numbertwo;
    }
    public void setNumberthree(String numberthree) 
    {
        this.numberthree = numberthree;
    }

    public String getNumberthree() 
    {
        return numberthree;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("license", getLicense())
            .append("parkinglotinformationid", getParkinglotinformationid())
            .append("admissiontime", getAdmissiontime())
            .append("exittime", getExittime())
            .append("licensepllatecolor", getLicensepllatecolor())
            .append("ordernumber", getOrdernumber())
            .append("orderstate", getOrderstate())
            .append("paystate", getPaystate())
            .append("money", getMoney())
            .append("entranceandexitname", getEntranceandexitname())
            .append("numberone", getNumberone())
            .append("numbertwo", getNumbertwo())
            .append("numberthree", getNumberthree())
            .toString();
    }
}
