package com.ruoyi.parking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: yangyang
 * @Date: 2023/01/18/17:27
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InPayData {

    private String  order_out_no;
    private String order_time;
    private String reqSsn;
    private String reqTime;
    private String cashier_code;
    private String user_id;
    private String sub_mchid;
    //车牌
    private String carId;
    //商户号
    private String mchtNo;
    //交易金额
    private String order_amountl;
    //订单描述
    private String order_subject;
    //进场时间
    private String inTime;
    //出场时间
    private String outTime;
    //停车场名称
    private String carpark;
    //原交易流水号，原支付交易的order_out_no
    private String origReqSsn;
    //原交易时间，原支付交易的order_time
    private String origReqTime;
}
