package com.ruoyi.web.configure;

import com.ruoyi.common.core.domain.entity.ParkingLotInformation;
import com.ruoyi.parking.domain.ParkingRecord;
import com.ruoyi.parking.service.IParkingLotInformationService;
import com.ruoyi.parking.service.IParkingRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Configuration
@EnableScheduling
public class fleeConfig {

	@Autowired
    private IParkingRecordService parkingRecordService;
	@Autowired
    private IParkingLotInformationService parkingLotInformationService;
    @Scheduled(cron = "0 0 0 * * *") // 每天凌晨执行一次(0:0:0)
	//@Scheduled(cron = "0 */10 * * * ?") // 每10分钟执行一次
	//@Scheduled(cron = "*/1 * * * * ?") // 每5秒执行一次
    @Transactional
    public void scheduledTasks() {
        //查询所有orderStae状态为2的车辆
      List<ParkingRecord> list= parkingRecordService.findByFlee();
      if (list.size()!=0) {
          for (ParkingRecord parkingRecord : list) {
              parkingRecord.setPaystate("1");
              parkingRecord.setFlee(1L);
              parkingRecord.setOrderstate("1");
              parkingRecordService.updateParkingRecord(parkingRecord);
              Long parkinglotinformationid = parkingRecord.getParkinglotinformationid();
              ParkingLotInformation parkingLotInformation = parkingLotInformationService.selectParkingLotInformationById(parkinglotinformationid);
             parkingLotInformation.setRemainingParkingSpace( parkingLotInformation.getRemainingParkingSpace()+1);
              parkingLotInformationService.updateParkingLotInformation(parkingLotInformation);
          }

      }

    }
}
