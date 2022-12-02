package com.ruoyi.web.controller.parking;

import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.domain.entity.ParkingLotInformation;
import com.ruoyi.common.sdk.LPRDemo;
import com.ruoyi.common.utils.CodeGenerateUtils;
import com.ruoyi.common.utils.DateTime.DateTime;
import com.ruoyi.parking.domain.ParkingLotEquipment;

import com.ruoyi.parking.domain.ParkingRecord;
import com.ruoyi.parking.domain.ParkingWhiteList;
import com.ruoyi.parking.dto.AlarmInfoPlate;
import com.ruoyi.parking.dto.Timeval;
import com.ruoyi.parking.dto.Total;
import com.ruoyi.parking.mapper.ParkingWhiteListMapper;
import com.ruoyi.parking.service.IParkingLotEquipmentService;
import com.ruoyi.parking.service.IParkingLotInformationService;
import com.ruoyi.parking.service.IParkingRecordService;
import com.ruoyi.parking.service.impl.ParkingLotInformationServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: yangyang
 * @Date: 2022/11/22/18:55
 * @Description:
 *
 * 进场出场业务逻辑
 */
@RestController
@RequestMapping("/parking")
@Anonymous
@Slf4j
public class ParkingController extends Thread {
    //停车记录
    @Autowired
    private IParkingRecordService parkingRecordService;
    //设备
    @Autowired
    private IParkingLotEquipmentService parkingLotEquipmentService;
    @Autowired
    private IParkingLotInformationService parkingLotInformationService;
    //保证多个线程获取同一把锁
    static Lock lock=new ReentrantLock();
    @Autowired
    private RedisTemplate<Object,Object>redisTemplate;
    @Autowired
    private ParkingWhiteListMapper parkingWhiteListMapper;
    @PostMapping("/operation")
    @Transactional
    public void test1(@RequestBody Total total){
        //获取车牌号
        String license =total.getAlarmInfoPlate().getResult().getPlateResult().getLicense();
        //获取出入场图片
        String imagePath = total.getAlarmInfoPlate().getResult().getPlateResult().getImagePath();
        //获取推送数据设备序列号
        String serialno = total.getAlarmInfoPlate().getSerialno();
        //通过序列号获取推送数据设备信息
        ParkingLotEquipment parkingLotEquipment=parkingLotEquipmentService.findParkingLotEquipmentBySerialno(serialno);
        // TODO: 2022/11/30 后面可能查询收费规则信息
        //查询停车场通过序列号
        ParkingLotInformation parkingLotInformation=parkingLotEquipmentService.findBySerialno(serialno);

        Timeval timeval = total.getAlarmInfoPlate().getResult().getPlateResult().getTimeStamp().getTimeval();
        //获取出入场时间
        Date date = DateTime.combineTime2(timeval.getDecyear(), timeval.getDecmon(), timeval.getDecday(), timeval.getDechour(), timeval.getDecmin(), timeval.getDecsec());


       //等于0则是进口，不等则是出口
        if(parkingLotEquipment.getDirection().equals("0")){

            //通过停车场id，车牌号查询有无未支付订单
            ParkingRecord parkingRecord= parkingRecordService.findByLicense(license,parkingLotInformation.getId());
            if (parkingRecord!=null){
                //如果有就删除掉
                parkingRecordService.deleteParkingRecordById(parkingRecord.getId());
            }
            //车牌颜色
            int carColor = total.getAlarmInfoPlate().getResult().getPlateResult().getCarColor();
            //生成订单号
            String s = CodeGenerateUtils.generateUnionPaySn();
            //进入口名称
            String name = parkingLotEquipment.getName();

            //判断剩余车位数是否大于1
            if (parkingLotInformation.getRemainingParkingSpace()>0){
                //加锁
                lock.lock();
                try {
                    //判断剩余车位数是否大于1  防止其他线程修改数据
                    if (parkingLotInformation.getRemainingParkingSpace()>0){
                        //保存进场信息
                        saveParkingRecord(date, carColor, s, name,license,parkingLotInformation.getId(),imagePath);
                        //设备开闸
                        SwitchOn(total.getAlarmInfoPlate().getIpaddr());
                        // 停车场车位数减一
                        parkingLotInformation.setRemainingParkingSpace(parkingLotInformation.getRemainingParkingSpace()-1);
                        parkingLotInformationService.updateParkingLotInformation(parkingLotInformation);
                    }
                } finally {
                    lock.unlock();
                }
            }
        }
        else {
            // TODO: 2022/11/30 免费时常放行   关联查收费规则
            // TODO: 2022/11/25 判断是否在白名单范围  在直接放行
            ParkingWhiteList byLicense = parkingWhiteListMapper.findByLicense(license, parkingLotInformation.getId());

            if (byLicense!=null){
                if (!byLicense.getState().equals("1")){
                    //设备开闸
                    SwitchOn(total.getAlarmInfoPlate().getIpaddr());
                    //停车场车位数加一
                    updateRemainingParkingSpace(parkingLotInformation);
                    // 修改停车场记录  需要修改金钱
                    //获取出闸口设备名称
                    String name = parkingLotEquipment.getName();
                    //获取停车场id
                    Long id = parkingLotInformation.getId();
                    //通过停车场id，车牌号,当前时间（通过时间排序获取最近时间）获取出场车停车记录
                    ParkingRecord parkingRecord=parkingRecordService.findByParkingLotInformationLicense(parkingLotInformation.getId(),license);
                    parkingRecord.setExittime(date);
                    //有bug 要改进
                    parkingRecord.setPaystate("1");
                    parkingRecord.setOrderstate("1");
                    parkingRecord.setMoney(0L);
                    parkingRecord.setNumberthree(imagePath);
                    parkingRecord.setEntranceandexitname(parkingRecord.getEntranceandexitname()+","+name);
                    //出场后修改停车场记录
                    parkingRecordService.updateParkingRecord(parkingRecord);
                    return;
                }
            }
            //停车场内支付业务逻辑
            //通过 停车场id，车牌号，和支付状态查询是否有无未支付订单，没有放行
           if (parkingRecordService.findByLicense(license,parkingLotInformation.getId())==null){
               //通过停车场id，车牌号,当前时间（通过时间排序获取最近时间）获取出场车停车记录
               ParkingRecord parkingRecord=parkingRecordService.findByParkingLotInformationLicense(parkingLotInformation.getId(),license);
               //判断支付时间和当前开闸时间是否超过15分钟  超过时间具体业务待想
               if (date.getTime()-parkingRecord.getPayTime().getTime()>1000*60*5){
                   log.info("支付到离场时间超过15分钟，不允开闸，待处理");
                   return;
               }

               //设备开闸
               SwitchOn(total.getAlarmInfoPlate().getIpaddr());
               //出场后修改停车场记录
               updateParkingRecord(parkingLotEquipment, parkingLotInformation,date,license,imagePath);
              //停车场车位数加一
               updateRemainingParkingSpace(parkingLotInformation);
               redisTemplate.delete(license);
           }
           //门闸口支付业务逻辑
           else {
               Date date1 = new Date();
               Boolean a=true;
               while(a) {
                   try {
                       //从redis中取值  判断支付服务回调接口是否修改数据
                       if(redisTemplate.opsForValue().get(license)!=null){
                            //开闸操作
                            SwitchOn(total.getAlarmInfoPlate().getIpaddr());
                            //出场后修改停车场记录
                           updateParkingRecord(parkingLotEquipment, parkingLotInformation,date1,license,imagePath);
                            //停车场车位数加一
                            updateRemainingParkingSpace(parkingLotInformation);
                           System.out.println("已支付");
                            redisTemplate.delete(license);
                            a=false;
                       }
                     if (date1.getTime()-date.getTime()>1*1000*60*5){
                           //结束循环
                           a=false;
                           log.info("超过5分钟未支付，不允开闸");
                       }
                   } catch (Exception e) {
                       e.printStackTrace();
                   }
               }
           }

        }

    }
    //出场后修改停车场记录
    private void updateParkingRecord(ParkingLotEquipment parkingLotEquipment, ParkingLotInformation parkingLotInformation,Date date,String license,String imagePath ) {
        //获取出闸口设备名称
        String name = parkingLotEquipment.getName();

        //获取停车场id
        Long id = parkingLotInformation.getId();
        //通过停车场id，车牌号,当前时间（通过时间排序获取最近时间）获取出场车停车记录
        ParkingRecord parkingRecord=parkingRecordService.findByParkingLotInformationLicense(parkingLotInformation.getId(),license);
        parkingRecord.setExittime(date);
        //保存出场照片
        parkingRecord.setNumberthree(imagePath);
        //有bug 要改进
        parkingRecord.setPaystate("1");
        parkingRecord.setOrderstate("1");
        parkingRecord.setEntranceandexitname(parkingRecord.getEntranceandexitname()+","+name);
        //出场后修改停车场记录
        parkingRecordService.updateParkingRecord(parkingRecord);
    }

    //停车场车位数加一
    private void updateRemainingParkingSpace(ParkingLotInformation parkingLotInformation) {
        Long remainingParkingSpace = parkingLotInformation.getRemainingParkingSpace();
        parkingLotInformation.setRemainingParkingSpace(remainingParkingSpace+1);
        parkingLotInformationService.updateParkingLotInformation(parkingLotInformation);
    }

    //开闸方法
    private void SwitchOn(String ipAdress) {
        LPRDemo lprDemo = new LPRDemo();
        //初始化返回句柄
        int handle = lprDemo.InitClient(ipAdress);
        //开闸
        int i1 = lprDemo.switchOn(handle, 0, 500);
        //关闭设备的控制句柄
        lprDemo.VzLPRClient_Close(handle);
        //执行结束释放
        lprDemo.VzLPRClient_Cleanup();
    }
    //保存进场信息
    private void saveParkingRecord(Date date, int carColor, String s, String name,String license,Long parkingLotInformationId,String imagePath) {

        //添加进场信息
        ParkingRecord parkingRecord1 = new ParkingRecord();
        parkingRecord1.setLicense(license);
        parkingRecord1.setAdmissiontime(date);
        //进场照片
        parkingRecord1.setNumbertwo(imagePath);
        parkingRecord1.setParkinglotinformationid(parkingLotInformationId);
        /** 车牌颜色 */
        parkingRecord1.setLicensepllatecolor(String.valueOf(carColor));
        /** 订单编号 */
        parkingRecord1.setOrdernumber(s);
        /** 订单状态0代表未支付1代表已支付 */
        parkingRecord1.setOrderstate("0");
        /** 支付状态0代表未支付1代表已支付 */
        parkingRecord1.setPaystate("0");
        /** 出入口名称     出口时要拼接出口名称*/
        parkingRecord1.setEntranceandexitname(name);
        parkingRecordService.insertParkingRecord(parkingRecord1);
    }


}
