package com.ruoyi.parking.domain;

import com.ruoyi.common.core.domain.entity.ParkingLotInformation;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 停车场设备管理对象 parking_lot_equipment
 * 
 * @author ruoyi
 * @date 2022-11-24
 */
public class ParkingLotEquipment extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 停车场id */
    @Excel(name = "停车场id")
    private Long parkinglotinformationid;
    private ParkingLotInformation parkingLotInformation;

    /** 设备名称 */
    @Excel(name = "设备名称")
    private String name;

    /** 摄象机序列号 */
    @Excel(name = "摄象机序列号")
    private String cameraserialnumber;

    /** 主板序列号 */
    @Excel(name = "主板序列号")
    private String motherboardserialnumber;

    /** 0代表进口闸门1代表出口闸门 */
    @Excel(name = "0代表进口闸门1代表出口闸门")
    private String direction;

    /** 控制板 */
    @Excel(name = "控制板")
    private String controlplate;

    /** 一行显示 */
    @Excel(name = "一行显示")
    private String onedisplay;

    /** 二行显示 */
    @Excel(name = "二行显示")
    private String twodisplay;

    /** 三行显示 */
    @Excel(name = "三行显示")
    private String threedisplay;

    /** 四行显示 */
    @Excel(name = "四行显示")
    private String fourdisplay;

    /** 二维码 */
    @Excel(name = "二维码")
    private String qrcode;

    /** 0代表正常1代表异常 */
    @Excel(name = "0代表正常1代表异常")
    private String state;

    /** ip地址 */
    @Excel(name = "ip地址")
    private String ipadress;

    /** 无记录离场0开启1关闭 */
    @Excel(name = "无记录离场0开启1关闭")
    private String departurewithoutrecords;

    /** 车牌防伪0开启1关闭 */
    @Excel(name = "车牌防伪0开启1关闭")
    private String licenseplatanticounterfeiting;

    /** 特定车辆出0开启1关闭 */
    @Excel(name = "特定车辆出0开启1关闭")
    private String specificvehicleexit;

    /** 余位屏0开启1关闭 */
    @Excel(name = "余位屏0开启1关闭")
    private String residualscreen;

    /** 音量范围0-10 */
    @Excel(name = "音量范围0-10")
    private Integer volume;

    /** 预留字段 */
    @Excel(name = "预留字段")
    private String numberone;

    /** 预留字段2 */
    @Excel(name = "预留字段2")
    private String numbertwo;

    /** 预留字段3 */
    @Excel(name = "预留字段3")
    private String numberthree;

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
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setCameraserialnumber(String cameraserialnumber) 
    {
        this.cameraserialnumber = cameraserialnumber;
    }

    public String getCameraserialnumber() 
    {
        return cameraserialnumber;
    }
    public void setMotherboardserialnumber(String motherboardserialnumber) 
    {
        this.motherboardserialnumber = motherboardserialnumber;
    }

    public String getMotherboardserialnumber() 
    {
        return motherboardserialnumber;
    }
    public void setDirection(String direction) 
    {
        this.direction = direction;
    }

    public String getDirection() 
    {
        return direction;
    }
    public void setControlplate(String controlplate) 
    {
        this.controlplate = controlplate;
    }

    public String getControlplate() 
    {
        return controlplate;
    }
    public void setOnedisplay(String onedisplay) 
    {
        this.onedisplay = onedisplay;
    }

    public String getOnedisplay() 
    {
        return onedisplay;
    }
    public void setTwodisplay(String twodisplay) 
    {
        this.twodisplay = twodisplay;
    }

    public String getTwodisplay() 
    {
        return twodisplay;
    }
    public void setThreedisplay(String threedisplay) 
    {
        this.threedisplay = threedisplay;
    }

    public String getThreedisplay() 
    {
        return threedisplay;
    }
    public void setFourdisplay(String fourdisplay) 
    {
        this.fourdisplay = fourdisplay;
    }

    public String getFourdisplay() 
    {
        return fourdisplay;
    }
    public void setQrcode(String qrcode) 
    {
        this.qrcode = qrcode;
    }

    public String getQrcode() 
    {
        return qrcode;
    }
    public void setState(String state) 
    {
        this.state = state;
    }

    public String getState() 
    {
        return state;
    }
    public void setIpadress(String ipadress) 
    {
        this.ipadress = ipadress;
    }

    public String getIpadress() 
    {
        return ipadress;
    }
    public void setDeparturewithoutrecords(String departurewithoutrecords) 
    {
        this.departurewithoutrecords = departurewithoutrecords;
    }

    public String getDeparturewithoutrecords() 
    {
        return departurewithoutrecords;
    }
    public void setLicenseplatanticounterfeiting(String licenseplatanticounterfeiting) 
    {
        this.licenseplatanticounterfeiting = licenseplatanticounterfeiting;
    }

    public String getLicenseplatanticounterfeiting() 
    {
        return licenseplatanticounterfeiting;
    }
    public void setSpecificvehicleexit(String specificvehicleexit) 
    {
        this.specificvehicleexit = specificvehicleexit;
    }

    public String getSpecificvehicleexit() 
    {
        return specificvehicleexit;
    }
    public void setResidualscreen(String residualscreen) 
    {
        this.residualscreen = residualscreen;
    }

    public String getResidualscreen() 
    {
        return residualscreen;
    }
    public void setVolume(Integer volume) 
    {
        this.volume = volume;
    }

    public Integer getVolume() 
    {
        return volume;
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
            .append("parkinglotinformationid", getParkinglotinformationid())
            .append("name", getName())
            .append("cameraserialnumber", getCameraserialnumber())
            .append("motherboardserialnumber", getMotherboardserialnumber())
            .append("direction", getDirection())
            .append("controlplate", getControlplate())
            .append("onedisplay", getOnedisplay())
            .append("twodisplay", getTwodisplay())
            .append("threedisplay", getThreedisplay())
            .append("fourdisplay", getFourdisplay())
            .append("qrcode", getQrcode())
            .append("state", getState())
            .append("ipadress", getIpadress())
            .append("departurewithoutrecords", getDeparturewithoutrecords())
            .append("licenseplatanticounterfeiting", getLicenseplatanticounterfeiting())
            .append("specificvehicleexit", getSpecificvehicleexit())
            .append("residualscreen", getResidualscreen())
            .append("volume", getVolume())
            .append("numberone", getNumberone())
            .append("numbertwo", getNumbertwo())
            .append("numberthree", getNumberthree())
            .toString();
    }
}
