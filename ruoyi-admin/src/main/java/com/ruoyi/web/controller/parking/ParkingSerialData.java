package com.ruoyi.web.controller.parking;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.parking.utils.SerialPortUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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
                    log.info(deviceName.toString());
                    log.info(ipaddr.toString());
                    log.info(port.toString());
                    log.info(userName.toString());
                    log.info(serialno.toString());
                    log.info("心跳续约正常");
        String s = SerialPortUtils.returnHeartbeat();
        return s;

    }
}
