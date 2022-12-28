package com.ruoyi.parking.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: yangyang
 * @Date: 2022/12/19/14:38
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoneyVo {
    /**/
    /** 优惠金额 */
    private Long discountamount;
    /**/
    /** 应缴金额 */
    private Long amountpayable;
    /** 支付金额 */
    private Long money;
    /*支付方式*/
    private  String paymentmethod;

}
