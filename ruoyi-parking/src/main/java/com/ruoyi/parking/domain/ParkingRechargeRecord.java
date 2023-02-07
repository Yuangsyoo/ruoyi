package com.ruoyi.parking.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.entity.ParkingLotInformation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 充值车操作记录对象 parking_recharge_record
 * 
 * @author ruoyi
 * @date 2023-02-01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkingRechargeRecord extends BaseEntity
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

    /** 操作时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "操作时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date operationtime;

    /** 操作类型 */
    @Excel(name = "操作类型")
    private String operationtype;

    /** 充值金额 */
    @Excel(name = "充值金额")
    private String money;

    /** 操作员 */
    @Excel(name = "操作员")
    private String operation;
    private ParkingLotInformation parkingLotInformation;


}
