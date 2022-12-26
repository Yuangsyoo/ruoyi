package com.ruoyi.parking.domain;
import java.util.List;

import com.ruoyi.common.core.domain.entity.ParkingLotInformation;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 计费规则对象 parking_charging
 * 
 * @author ruoyi
 * @date 2022-12-19
 */
public class ParkingCharging extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 序列号 */
    private Long id;

    /** 起步价 */
    @Excel(name = "起步价")
    private Long startingprice;

    /** 起步价时长 */
    @Excel(name = "起步价时长")
    private Long startingpriceduration;

    /** 每超过1小时加收 */
    @Excel(name = "每超过1小时加收")
    private Long increaseincome;

    /** 单日收费上限
 */
    @Excel(name = "单日收费上限 ")
    private Long superiorlimit;

    /** 停车场id */
    @Excel(name = "停车场id")
    private Long parkinglotinformationid;

    /** 类型 */
    @Excel(name = "类型")
    private Long type;
    private ParkingLotInformation parkingLotInformation;

    /** 计费时间段信息 */
    private List<ParkingBillingPeriod> parkingBillingPeriodList;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public ParkingLotInformation getParkingLotInformation() {
        return parkingLotInformation;
    }

    public void setParkingLotInformation(ParkingLotInformation parkingLotInformation) {
        this.parkingLotInformation = parkingLotInformation;
    }

    public Long getId()
    {
        return id;
    }
    public void setStartingprice(Long startingprice) 
    {
        this.startingprice = startingprice;
    }

    public Long getStartingprice() 
    {
        return startingprice;
    }
    public void setStartingpriceduration(Long startingpriceduration) 
    {
        this.startingpriceduration = startingpriceduration;
    }

    public Long getStartingpriceduration() 
    {
        return startingpriceduration;
    }
    public void setIncreaseincome(Long increaseincome) 
    {
        this.increaseincome = increaseincome;
    }

    public Long getIncreaseincome() 
    {
        return increaseincome;
    }
    public void setSuperiorlimit(Long superiorlimit) 
    {
        this.superiorlimit = superiorlimit;
    }

    public Long getSuperiorlimit() 
    {
        return superiorlimit;
    }
    public void setParkinglotinformationid(Long parkinglotinformationid) 
    {
        this.parkinglotinformationid = parkinglotinformationid;
    }

    public Long getParkinglotinformationid() 
    {
        return parkinglotinformationid;
    }
    public void setType(Long type) 
    {
        this.type = type;
    }

    public Long getType() 
    {
        return type;
    }

    public List<ParkingBillingPeriod> getParkingBillingPeriodList()
    {
        return parkingBillingPeriodList;
    }

    public void setParkingBillingPeriodList(List<ParkingBillingPeriod> parkingBillingPeriodList)
    {
        this.parkingBillingPeriodList = parkingBillingPeriodList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("startingprice", getStartingprice())
            .append("startingpriceduration", getStartingpriceduration())
            .append("increaseincome", getIncreaseincome())
            .append("superiorlimit", getSuperiorlimit())
            .append("parkinglotinformationid", getParkinglotinformationid())
            .append("type", getType())
            .append("parkingBillingPeriodList", getParkingBillingPeriodList())
            .toString();
    }
}
