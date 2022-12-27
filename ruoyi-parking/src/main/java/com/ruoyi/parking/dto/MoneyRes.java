package com.ruoyi.parking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: yangyang
 * @Date: 2022/12/26/15:04
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoneyRes implements Serializable {
    private List<Long> month=new ArrayList<>();
    private List<Long> money=new ArrayList<>();
}
