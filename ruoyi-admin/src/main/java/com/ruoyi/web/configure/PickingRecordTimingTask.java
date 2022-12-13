package com.ruoyi.web.configure;
import com.ruoyi.parking.domain.ParkingCoupon;
import com.ruoyi.parking.domain.ParkingCouponrecord;
import com.ruoyi.parking.mapper.ParkingCouponMapper;
import com.ruoyi.parking.mapper.ParkingCouponrecordMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: yangyang
 * @Date: 2022/12/13/9:23
 * @Description:
 */
@Slf4j
@Configuration
@EnableScheduling
public class PickingRecordTimingTask {
    @Autowired
    private ParkingCouponrecordMapper parkingCouponrecordMapper;
    @Autowired
    private ParkingCouponMapper parkingCouponMapper;
    @Scheduled(cron = "0 0 0 * * *") // 每天凌晨执行一次(0:0:0)
    //@Scheduled(cron = "0 */10 * * * ?") // 每10分钟执行一次
    //@Scheduled(cron = "*/5 * * * * ?") // 每5秒执行一次
    public void scheduledTasks() {
        Date date = new Date();
        ParkingCouponrecord parkingCouponrecord = new ParkingCouponrecord();
        parkingCouponrecord.setState("0");
        //查询未使用的优惠卷领取记录
        List<ParkingCouponrecord> list = parkingCouponrecordMapper.selectParkingCouponrecordList(parkingCouponrecord);
        //循环优惠卷领取记录
        for (ParkingCouponrecord couponrecord : list) {
            //查询优惠卷  判断优惠卷是当天消费还是二维码结束时间
            ParkingCoupon parkingCoupon = parkingCouponMapper.selectParkingCouponById(couponrecord.getId());
            //如果是当天消费模式
            if (parkingCoupon.getUsetype()==0){
                //设置过期状态
                couponrecord.setState("2");
                parkingCouponrecordMapper.updateParkingCouponrecord(couponrecord);
            }
            //如果是失效时间是二维码结束时间
            if (parkingCoupon.getUsetype()==1){
               if (date.getTime()>=parkingCoupon.getQrcodeendtime().getTime()){
                   //设置过期状态
                   couponrecord.setState("2");
                   parkingCouponrecordMapper.updateParkingCouponrecord(couponrecord);
               }
            }

        }

    }
}
