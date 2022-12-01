package com.ruoyi.parking.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 白名单收费记录对象 parking_white_list_charge_record
 * 
 * @author ruoyi
 * @date 2022-11-30
 */
public class ParkingWhiteListChargeRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 停车场id */
    @Excel(name = "停车场id")
    private Long parkinglotinformationid;

    /** 缴费时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+8")
    @Excel(name = "缴费时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date paymenttime;

    /** 缴费方式 */
    @Excel(name = "缴费方式")
    private String paymentmethod;

    /** 车牌号码 */
    @Excel(name = "车牌号码")
    private String license;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+8")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date starttime;

    /** $column.columnComment */
    @Excel(name = "结束时间", readConverterExp = "$column.readConverterExp()")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+8")
    private Date endtime;

    /** 实收金额 */
    @Excel(name = "实收金额")
    private Long money;

    /** 收费员 */
    @Excel(name = "收费员")
    private String operator;

    /** 操作类型 */
    @Excel(name = "操作类型")
    private String operationtype;

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
    public void setPaymenttime(Date paymenttime) 
    {
        this.paymenttime = paymenttime;
    }

    public Date getPaymenttime() 
    {
        return paymenttime;
    }
    public void setPaymentmethod(String paymentmethod) 
    {
        this.paymentmethod = paymentmethod;
    }

    public String getPaymentmethod() 
    {
        return paymentmethod;
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
    public void setMoney(Long money) 
    {
        this.money = money;
    }

    public Long getMoney() 
    {
        return money;
    }
    public void setOperator(String operator) 
    {
        this.operator = operator;
    }

    public String getOperator() 
    {
        return operator;
    }
    public void setOperationtype(String operationtype) 
    {
        this.operationtype = operationtype;
    }

    public String getOperationtype() 
    {
        return operationtype;
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
            .append("paymenttime", getPaymenttime())
            .append("paymentmethod", getPaymentmethod())
            .append("license", getLicense())
            .append("starttime", getStarttime())
            .append("endtime", getEndtime())
            .append("money", getMoney())
            .append("operator", getOperator())
            .append("operationtype", getOperationtype())
            .append("remarks", getRemarks())
            .toString();
    }
}
