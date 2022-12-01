package com.ruoyi.parking.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;

/**
 * 停车场白名单对象 parking_white_list
 * 
 * @author ruoyi
 * @date 2022-11-29
 */
public class ParkingWhiteList extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 停车场id */
    @Excel(name = "停车场id")
    private Long parkinglotinformationid;

    /** 车牌号 */
    @Excel(name = "车牌号")
    @NotNull(message = "请填写车牌号")
    private String license;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+8")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date starttime;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+8")
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endtime;

    /** 添加时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+8")
    @Excel(name = "添加时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date addtime;

    /** 白名单用户姓名 */
    @Excel(name = "白名单用户姓名")
    private String name;

    /** 手机号 */
    @Excel(name = "手机号")
    //手机号码也用个性化提示，使用正则表达式进行匹配，非空时不验证
    @Pattern(regexp="^1(3|4|5|7|8)\\d{9}$",message="手机号码格式错误！")
    private String phone;

    /** 地址 */
    @Excel(name = "地址")
    private String adress;

    /** 月费 */
    @Excel(name = "月费")
    private Long monthlyfee;

    /** 状态0代表启用1代表禁用 */
    @Excel(name = "状态0代表启用1代表禁用")
    private Integer state;

    /** 操作员 */
    @Excel(name = "操作员")
    private String operator;

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
    public void setAddtime(Date addtime) 
    {
        this.addtime = addtime;
    }

    public Date getAddtime() 
    {
        return addtime;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setPhone(String phone) 
    {
        this.phone = phone;
    }

    public String getPhone() 
    {
        return phone;
    }
    public void setAdress(String adress) 
    {
        this.adress = adress;
    }

    public String getAdress() 
    {
        return adress;
    }
    public void setMonthlyfee(Long monthlyfee) 
    {
        this.monthlyfee = monthlyfee;
    }

    public Long getMonthlyfee() 
    {
        return monthlyfee;
    }
    public void setState(Integer state) 
    {
        this.state = state;
    }

    public Integer getState() 
    {
        return state;
    }
    public void setOperator(String operator) 
    {
        this.operator = operator;
    }

    public String getOperator() 
    {
        return operator;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("parkinglotinformationid", getParkinglotinformationid())
            .append("license", getLicense())
            .append("starttime", getStarttime())
            .append("endtime", getEndtime())
            .append("addtime", getAddtime())
            .append("name", getName())
            .append("phone", getPhone())
            .append("adress", getAdress())
            .append("monthlyfee", getMonthlyfee())
            .append("state", getState())
            .append("operator", getOperator())
            .toString();
    }
}
