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
}
