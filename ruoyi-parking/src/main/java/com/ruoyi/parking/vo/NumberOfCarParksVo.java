package com.ruoyi.parking.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NumberOfCarParksVo {
    // 停车场停车位的数量
    private List<Long> value = new ArrayList<>();

    // 存放停车场名字
    private List<String> name = new ArrayList<>();
}