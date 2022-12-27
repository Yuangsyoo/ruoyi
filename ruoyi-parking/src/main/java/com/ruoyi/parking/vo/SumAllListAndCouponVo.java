package com.ruoyi.parking.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: yangyang
 * @Date: 2022/12/27/15:41
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SumAllListAndCouponVo {
    private List<String> name=new ArrayList<>();
    private List<Long> count=new ArrayList<>();
}
