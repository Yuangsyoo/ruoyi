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
    @Excel(name = "免费时常")
    private Long freetime;
    /** 类型 */
    @Excel(name = "类型")
    private Long type;

    private int freeTimeState;
    //是否重复收取起步价
    private int startingpriceState;
    private ParkingLotInformation parkingLotInformation;
    //区分是普通计费 还是充值车计费
    private Long distinguish;

    public Long getFreetime() {
        return freetime;
    }

    public void setFreetime(Long freetime) {
        this.freetime = freetime;
    }

    /** 计费时间段信息 */
    private List<ParkingBillingPeriod> parkingBillingPeriodList;

    public Long getDistinguish() {
        return distinguish;
    }

    public void setDistinguish(Long distinguish) {
        this.distinguish = distinguish;
    }

    public int getStartingpriceState() {
        return startingpriceState;
    }

    public void setStartingpriceState(int startingpriceState) {
        this.startingpriceState = startingpriceState;
    }

    public int getFreeTimeState() {
        return freeTimeState;
    }

    public void setFreeTimeState(int freeTimeState) {
        this.freeTimeState = freeTimeState;
    }

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
