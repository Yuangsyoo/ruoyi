package com.ruoyi.parking.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: yangyang
 * @Date: 2022/12/23/0:42
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultVo {
    //判断在区间上
    private Boolean state;
    //停车开始时间在区间中是在开始还是结束区间前
    private Long type;
}
