package com.ruoyi.parking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: yangyang
 * @Date: 2022/12/26/14:43
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor

public class MoneyDto {
    private Long month;
    private Long money;
}
