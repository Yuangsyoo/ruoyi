package com.ruoyi.web.controller.parking;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.parking.utils.A;
import com.ruoyi.parking.utils.SerialData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public void ss(@RequestBody A a){
        SerialData serialData = a.getSerialData();
        System.out.println(serialData);

    }
}
