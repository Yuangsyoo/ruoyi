package com.ruoyi.web.configure;

import com.ruoyi.parking.domain.ParkingCoupon;
import com.ruoyi.parking.domain.ParkingCouponrecord;
import com.ruoyi.parking.mapper.ParkingCouponMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: yangyang
 * @Date: 2023/02/08/9:10
 * @Description:
 */
@Slf4j
@Configuration
@EnableScheduling
public class ParkingCountReferConfig {

    @Autowired
    private ParkingCouponMapper parkingCouponMapper;
    @Scheduled(cron = "0 10 0 1 * ?") // 每月1号的0:10:00执行
    //@Scheduled(cron = "0 */10 * * * ?") // 每10分钟执行一次
    //@Scheduled(cron = "*/5 * * * * ?") // 每5秒执行一次
    public void scheduledRefers() {
        ParkingCoupon parkingCoupon = new ParkingCoupon();
        parkingCoupon.setState(0);
        parkingCoupon.setRefresh(0L);
        //查询未使用的优惠卷领取记录
        List<ParkingCoupon> list = parkingCouponMapper.selectParkingCouponList(parkingCoupon);
        for (ParkingCoupon coupon : list) {
            coupon.setReceiveccount(0L);
            parkingCouponMapper.updateParkingCoupon(coupon);
        }
    }
}
