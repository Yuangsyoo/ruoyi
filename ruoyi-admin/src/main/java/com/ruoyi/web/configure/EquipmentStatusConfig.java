package com.ruoyi.web.configure;

import com.ruoyi.common.core.domain.entity.ParkingLotInformation;
import com.ruoyi.common.core.domain.entity.ParkingLotEquipment;
import com.ruoyi.parking.mapper.ParkingCouponMapper;
import com.ruoyi.parking.mapper.ParkingLotEquipmentMapper;
import com.ruoyi.parking.service.IParkingLotInformationService;
import com.ruoyi.parking.service.IParkingWhiteListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Slf4j
@Configuration
@EnableScheduling
public class EquipmentStatusConfig {

	@Autowired
	private IParkingWhiteListService parkingWhiteListService;
	@Autowired
    private IParkingLotInformationService parkingLotInformationService;
	@Autowired
    private ParkingLotEquipmentMapper parkingLotEquipmentMapper;
	@Autowired
    private ParkingCouponMapper parkingCouponMapper;
	@Autowired
    private RedisTemplate<Object,Object>redisTemplate;

   //@Scheduled(cron = "0 0 0 * * *") // 每天凌晨执行一次(0:0:0)
	//@Scheduled(cron = "0 */10 * * * ?") // 每10分钟执行一次
	@Scheduled(cron = "*/1 * * * * ?") // 每5秒执行一次
    public void scheduledTasks() {

        List<ParkingLotInformation> list = parkingLotInformationService.selectParkingLotInformationListOne();
        for (ParkingLotInformation parkingLotInformation : list) {
            List<ParkingLotEquipment> list1 = parkingLotEquipmentMapper.byParkinglotinformationid(parkingLotInformation.getId());
            for (ParkingLotEquipment parkingLotEquipment : list1) {
                String s = (String) redisTemplate.opsForValue().get(parkingLotEquipment.getCameraserialnumber() + "state");
                if (s==null ){
                    if ( parkingLotEquipment.getState().equals("0")){
                    parkingLotEquipment.setState("1");
                    parkingLotEquipmentMapper.updateParkingLotEquipment(parkingLotEquipment);
                    log.info("停车场【"+parkingLotInformation.getName()+"】设备名称【"+parkingLotEquipment.getName()+"】状态异常");
                    }
                }
                if (s!=null){
                    if (parkingLotEquipment.getState().equals("1")) {
                        parkingLotEquipment.setState("0");
                        parkingLotEquipmentMapper.updateParkingLotEquipment(parkingLotEquipment);
                    }
                }
            }
        }
    }
}
