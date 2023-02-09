package com.ruoyi.web.controller.parking;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.domain.entity.ParkingLotInformation;
import com.ruoyi.common.core.domain.entity.ParkingLotEquipment;
import com.ruoyi.parking.service.IParkingLotEquipmentService;
import com.ruoyi.parking.service.IParkingLotInformationService;
import com.ruoyi.parking.utils.SerialPortUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: yangyang
 * @Date: 2023/01/05/16:17
 * @Description:
 */
@RestController
@RequestMapping("/parkingSerialData")
@Slf4j
public class ParkingSerialData {
    // TODO: 2023/1/31 采用轮询做开杆操作
@Autowired
private RedisTemplate<Object,Object>redisTemplate;

@Autowired
private IParkingLotEquipmentService parkingLotEquipmentService;
@Autowired
private IParkingLotInformationService parkingLotInformationService;
    @PostMapping("/operation")
    @Transactional
    @Anonymous
    public String ss(@RequestParam(value = "device_name")String deviceName,
    @RequestParam(value = "ipaddr")String ipaddr,
                   @RequestParam(value = "port")String port,
                   @RequestParam(value = "user_name")String userName,
                   @RequestParam(value = "pass_wd")String password,
                   @RequestParam(value = "serialno")String serialno,
                   @RequestParam(value = "channel_num")String channelNum) {
                    log.info(deviceName);
                    log.info(ipaddr);
                    log.info(port);
                    log.info(userName);
                    log.info(serialno);

        ParkingLotEquipment parkingLotEquipment = parkingLotEquipmentService.findParkingLotEquipmentBySerialno(serialno);
        /*ParkingLotInformation parkingLotInformation = parkingLotInformationService.selectParkingLotInformationById(parkingLotEquipment.getParkinglotinformationid());*/
        //log.info(parkingLotInformation.getName()+"停车场设备【"+parkingLotEquipment.getName()+"】心跳续约正常");
                    //可以考虑做后台向设备发送开杆指令
        // TODO: 2023/1/31 判断redis中是否有值，没有就是普通心调 有就是发送开干指令

        String s = SerialPortUtils.returnHeartbeat();
        String s1 = (String) redisTemplate.opsForValue().get(serialno);
        String s2 = (String) redisTemplate.opsForValue().get(serialno + "revise");
        if (s1!=null){
          s= s1;
          redisTemplate.delete(serialno);
            return  s;
        }
        if (s2!=null){
            s= s2;
            redisTemplate.delete(serialno + "revise");
            return  s;
        }
        //添加数据redis中  等待定时任务查询修改设备状态  设置10s过期时间
        redisTemplate.opsForValue().set(serialno+"state",serialno,10, TimeUnit.SECONDS);
      return s;
    }





}
