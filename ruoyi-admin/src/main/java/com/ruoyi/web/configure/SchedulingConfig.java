package com.ruoyi.web.configure;

import com.ruoyi.common.core.domain.entity.ParkingLotInformation;
import com.ruoyi.common.sdk.LPRDemo;
import com.ruoyi.parking.domain.ParkingLotEquipment;

import com.ruoyi.parking.domain.ParkingWhiteList;
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
    @Scheduled(cron = "0 0 0 * * *") // 每天凌晨执行一次(0:0:0)
	//@Scheduled(cron = "0 */10 * * * ?") // 每10分钟执行一次
	//@Scheduled(cron = "*/5 * * * * ?") // 每5秒执行一次
    public void scheduledTasks() {
        Date date = new Date();
        LPRDemo lprDemo = new LPRDemo();
        
        //定时任务业务逻辑
       List<ParkingWhiteList> list = parkingWhiteListService.selectParkingWhiteListList(null);
         for (ParkingWhiteList parkingWhiteList : list) {
             System.out.println(parkingWhiteList);
            if (date.getTime()>parkingWhiteList.getEndtime().getTime()){
                parkingWhiteList.setState(1);
                parkingWhiteListService.updateParkingWhiteList1(parkingWhiteList);
                //删除白名单
                deleteWhiteLis(lprDemo, parkingWhiteList);
            }
           if (parkingWhiteList.getState()==1){
               //删除白名单
               deleteWhiteLis(lprDemo, parkingWhiteList);
           }
        }
      }

    private void deleteWhiteLis(LPRDemo lprDemo, ParkingWhiteList parkingWhiteList) {
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
