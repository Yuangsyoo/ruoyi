package com.ruoyi.parking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: yangyang
 * @Date: 2023/01/18/17:25
 * @Description:
 */
@AllArgsConstructor
@NoArgsConstructor
@Data

public class PayState {


    private String  tradeCode;
    private String   payType;
    private InPayData  inPayData;

}
