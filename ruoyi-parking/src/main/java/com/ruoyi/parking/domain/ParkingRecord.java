package com.ruoyi.parking.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.entity.ParkingLotInformation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 停车记录对象 parking_record
 * 
 * @author ruoyi
 * @date 2022-11-24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+8")
    @Excel(name = "入场时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date admissiontime;

    /** 出场时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+8")
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+8")
    @Excel(name = "支付时间")
    private Date payTime;

    /** 预留字段2 */

    @Excel(name = "进场图片")
    private String numbertwo;

    /** 预留字段3 */
    @Excel(name = "出场图片")
    private String numberthree;
    /*优惠金额*/
    @Excel(name = "优惠金额")
    private Long discountamount;
    /*应缴金额*/
    @Excel(name = "应缴金额")
    private Long amountpayable;
    /*支付方式*/
    @Excel(name = "支付方式")
    private  String paymentmethod;
    private ParkingLotInformation parkingLotInformation;
    private String openid;
    /** 是否是室内支付 */
    private String indoorPayment;
    //最近离场停车设备id
    private Long parkinglotequipmentid;
    //是否逃费
    private Long flee;
    //超时补费
    private Long  supplementaryfee;

    private String wxorder;

    private Date starttime;
    private Date endtime;
    //最近离场停车设备id
    private Long parkingeqid;

    private String date;
}
