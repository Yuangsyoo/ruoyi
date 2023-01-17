package com.ruoyi.parking.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.entity.ParkingLotInformation;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 停车场手动开杆管理对象 parking_manual_opening
 * 
 * @author ruoyi
 * @date 2022-12-01
 */
public class ParkingManualOpening extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 序号 */
    private Long id;

    /** 停车场id */
    @Excel(name = "停车场id")
    private Long parkinglotinformationid;
    private ParkingLotInformation parkingLotInformation;
    /** 设备id */
    @Excel(name = "设备id")
    private Long parkinglotequipmentid;
    private ParkingLotEquipment parkingLotEquipment;
    /** 操作员 */
    @Excel(name = "操作员")
    private String operator;

    public ParkingLotInformation getParkingLotInformation() {
        return parkingLotInformation;
    }

    public void setParkingLotInformation(ParkingLotInformation parkingLotInformation) {
        this.parkingLotInformation = parkingLotInformation;
    }

    public ParkingLotEquipment getParkingLotEquipment() {
        return parkingLotEquipment;
    }

    public void setParkingLotEquipment(ParkingLotEquipment parkingLotEquipment) {
        this.parkingLotEquipment = parkingLotEquipment;
    }

    /** 操作时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+8")
    @Excel(name = "操作时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date time;

    /** 备注 */
    @Excel(name = "备注")
    private String remarks;

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
    public void setParkinglotequipmentid(Long parkinglotequipmentid) 
    {
        this.parkinglotequipmentid = parkinglotequipmentid;
    }

    public Long getParkinglotequipmentid() 
    {
        return parkinglotequipmentid;
    }
    public void setOperator(String operator) 
    {
        this.operator = operator;
    }

    public String getOperator() 
    {
        return operator;
    }
    public void setTime(Date time) 
    {
        this.time = time;
    }

    public Date getTime() 
    {
        return time;
    }
    public void setRemarks(String remarks) 
    {
        this.remarks = remarks;
    }

    public String getRemarks() 
    {
        return remarks;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("parkinglotinformationid", getParkinglotinformationid())
            .append("parkinglotequipmentid", getParkinglotequipmentid())
            .append("operator", getOperator())
            .append("time", getTime())
            .append("remarks", getRemarks())
            .toString();
    }
}
