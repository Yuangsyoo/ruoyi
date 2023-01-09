package com.ruoyi.web.configure;

import com.ruoyi.common.core.domain.entity.ParkingLotInformation;
import com.ruoyi.common.sdk.LPRDemo;
import com.ruoyi.parking.domain.ParkingCoupon;
import com.ruoyi.parking.domain.ParkingLotEquipment;

import com.ruoyi.parking.domain.ParkingWhiteList;
import com.ruoyi.parking.mapper.ParkingCouponMapper;
import com.ruoyi.parking.mapper.ParkingLotEquipmentMapper;
import com.ruoyi.parking.service.IParkingLotInformationService;
import com.ruoyi.parking.service.IParkingWhiteListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;
import java.util.List;

@Slf4j
@Configuration
@EnableScheduling
public class SchedulingConfig {

	@Autowired
	private IParkingWhiteListService parkingWhiteListService;
	@Autowired
    private IParkingLotInformationService parkingLotInformationService;
	@Autowired
    private ParkingLotEquipmentMapper parkingLotEquipmentMapper;
	@Autowired
    private ParkingCouponMapper parkingCouponMapper;

    @Scheduled(cron = "0 0 0 * * *") // 每天凌晨执行一次(0:0:0)
	//@Scheduled(cron = "0 */10 * * * ?") // 每10分钟执行一次
	//@Scheduled(cron = "*/5 * * * * ?") // 每5秒执行一次
    public void scheduledTasks() {
        System.out.println(1);
        Date date = new Date();
       LPRDemo lprDemo = new LPRDemo();
        System.out.println(1);
        //定时任务白名单业务逻辑
       List<ParkingWhiteList> list = parkingWhiteListService.selectParkingWhiteListList(null);
         for (ParkingWhiteList parkingWhiteList : list) {
           //  System.out.println(parkingWhiteList);
            if (date.getTime()>parkingWhiteList.getEndtime().getTime()){
                parkingWhiteList.setState(1);
                parkingWhiteListService.updateParkingWhiteList1(parkingWhiteList);
                //删除白名单
                deleteWhiteLis(lprDemo, parkingWhiteList);
            }
        }
        //优惠卷二维码定时器
        List<ParkingCoupon> list1 = parkingCouponMapper.selectParkingCouponList(null);
        for (ParkingCoupon parkingCoupon : list1) {
            if (parkingCoupon.getCount()==parkingCoupon.getReceiveccount()||date.getTime()>parkingCoupon.getQrcodeendtime().getTime()){
                parkingCoupon.setState(1);
                parkingCouponMapper.updateParkingCoupon(parkingCoupon);
            }
        }
      }






    private void deleteWhiteLis(LPRDemo lprDemo, ParkingWhiteList parkingWhiteList) {
        System.out.println(2);
        ParkingLotInformation parkingLotInformation = parkingLotInformationService.selectParkingLotInformationById(parkingWhiteList.getParkinglotinformationid());
        ParkingLotEquipment parkingLotEquipment = new ParkingLotEquipment();
        parkingLotEquipment.setParkinglotinformationid(parkingLotInformation.getId());
        List<ParkingLotEquipment> parkingLotEquipments = parkingLotEquipmentMapper.selectParkingLotEquipmentList(parkingLotEquipment);
        for (ParkingLotEquipment lotEquipment : parkingLotEquipments) {
            int i = lprDemo.InitClient(lotEquipment.getIpadress());
            //删除设备白名单
            lprDemo.DeleteWlistByPlate(i,parkingWhiteList.getLicense());
        }
    }
}
