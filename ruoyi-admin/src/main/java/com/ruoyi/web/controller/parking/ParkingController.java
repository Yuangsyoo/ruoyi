package com.ruoyi.web.controller.parking;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.domain.entity.ParkingLotInformation;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.sdk.LPRDemo;
import com.ruoyi.common.utils.CodeGenerateUtils;
import com.ruoyi.common.utils.DateTime.DateTime;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.web.service.TokenService;

import com.ruoyi.framework.websocket.WebSocketService;
import com.ruoyi.parking.domain.*;

import com.ruoyi.parking.dto.AlarmInfoPlate;
import com.ruoyi.parking.dto.Timeval;
import com.ruoyi.parking.dto.Total;
import com.ruoyi.parking.mapper.ParkingBlackListMapper;
import com.ruoyi.parking.mapper.ParkingRecordMapper;
import com.ruoyi.parking.mapper.ParkingWhiteListMapper;
import com.ruoyi.parking.service.*;
import com.ruoyi.parking.service.impl.ParkingChargingServiceImpl;
import com.ruoyi.parking.service.impl.ParkingLotInformationServiceImpl;
import com.ruoyi.parking.utils.SerialPortUtils;
import com.ruoyi.parking.vo.*;
import com.ruoyi.system.mapper.SysUserMapper;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
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
    @Autowired
    private ParkingBlackListMapper parkingBlackListMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private WebSocketService webSocketService;
    @Autowired
    private IParkingCouponrecordService parkingCouponrecordService;
    @Autowired
    private IParkingFixedparkingspaceService parkingFixedparkingspaceService;
    @Autowired
    private ParkingChargingServiceImpl parkingChargingService;


    @PostMapping("/operation")
    @Transactional
    @Anonymous
    public String test1(@RequestBody Total total) {
        //获取车牌号
        String license =total.getAlarmInfoPlate().getResult().getPlateResult().getLicense();
        //车牌id
        int plateid = total.getAlarmInfoPlate().getResult().getPlateResult().getPlateid();
        //获取出入场图片
        String imagePath = total.getAlarmInfoPlate().getResult().getPlateResult().getImageFile();
        //获取推送数据设备序列号
        String serialno = total.getAlarmInfoPlate().getSerialno();
        //通过序列号获取推送数据设备信息
        ParkingLotEquipment parkingLotEquipment=parkingLotEquipmentService.findParkingLotEquipmentBySerialno(serialno);
        //查询停车场通过序列号
        ParkingLotInformation parkingLotInformation=parkingLotEquipmentService.findBySerialno(serialno);
        Timeval timeval = total.getAlarmInfoPlate().getResult().getPlateResult().getTimeStamp().getTimeval();
        //获取出入场时间
        Date date = DateTime.combineTime2(timeval.getDecyear(), timeval.getDecmon(), timeval.getDecday(), timeval.getDechour(), timeval.getDecmin(), timeval.getDecsec());
       //等于0则是进口，不等则是出口
        if(parkingLotEquipment.getDirection().equals("0")){
            //停车场营业时间
            String starttime = parkingLotInformation.getStarttime();
            String endtime = parkingLotInformation.getEndtime();
            //当前时间需转为HH:mm的时间格式：
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            String now = sdf.format(date);
            boolean effectiveDate=true;
            //HH:mm格式的当前时间
            try {
                Date nowTime = sdf.parse(now);
                //时间区间String转Date
                Date startTime = sdf.parse(starttime);
                Date endTime  = sdf.parse(endtime);
                 effectiveDate = DateTime.isEffectiveDate(nowTime, startTime, endTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (!effectiveDate){
                log.info("{\"Response_AlarmInfoPlate\":{\"info\":\"no\",\"content\":\"不在营业范围内\",\"is_pay\":\"false\"}}");
                return "{\"Response_AlarmInfoPlate\":{\"info\":\"no\",\"content\":\"不在营业范围内\",\"is_pay\":\"false\"}}";
            }
            //判断停车场状态异常停车场不可用 只针对进口a
            if (parkingLotInformation.getState().equals("1")){
                log.info("{\"Response_AlarmInfoPlate\":{\"info\":\"no\",\"content\":\"车场状态异常停车场不可用\",\"is_pay\":\"false\"}}");
                String data = SerialPortUtils.abnormal();
                return data;
            }
            //判断设备是否可用
            if (parkingLotEquipment.getState().equals("1")){
                String data = SerialPortUtils.equipmentAbnormality();
                log.info("{\"Response_AlarmInfoPlate\":{\"info\":\"no\",\"content\":\"设备不可用\",\"is_pay\":\"false\"}}");
                return data;
            }
            //判断是否时临时车限制
            if (parkingLotInformation.getTemporaryvehiclerestrictions().equals("0")){
                ParkingFixedparkingspace parkingFixedparkingspace=parkingFixedparkingspaceService.findByParkingLotInformationIdAndLicense(parkingLotInformation.getId(),license);
                ParkingWhiteList byLicense = parkingWhiteListMapper.findByLicense(license, parkingLotInformation.getId());

                if (parkingFixedparkingspace==null && byLicense==null  ){
                    String data = SerialPortUtils.temporaryVehicle(license);
                    log.info("{\"Response_AlarmInfoPlate\":{\"info\":\"no\",\"content\":\"临时车限制进入\",\"is_pay\":\"false\"}}");
                    return data;
                }
            }
            //黑名单拒绝放行
           ParkingBlackList parkingBlackList= parkingBlackListMapper.selectParkingBlackListByIdAndLicense(parkingLotInformation.getId(),license);
            if (parkingBlackList!=null){
                String data = SerialPortUtils.blacklist(license);
                log.info("{\"Response_AlarmInfoPlate\":{\"info\":\"no\",\"content\":\"黑名单拒绝放行\",\"is_pay\":\"false\"}}");
                return data;
            }
            //通过停车场id，车牌号查询有无未支付订单
            ParkingRecord parkingRecord= parkingRecordService.findByLicense(license,parkingLotInformation.getId());
            if (parkingRecord!=null){
                //如果有就删除掉
                updateRemainingParkingSpace(parkingLotInformation);
                parkingRecordService.deleteParkingRecordById(parkingRecord.getId());
            }
            //车牌颜色
            int carColor = total.getAlarmInfoPlate().getResult().getPlateResult().getColorType();
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
                        saveParkingRecord(date,parkingLotEquipment, carColor, s, name,license,parkingLotInformation.getId(),imagePath);
                        // 停车场车位数减一
                        long l = parkingLotInformation.getRemainingParkingSpace() - 1;
                        parkingLotInformation.setRemainingParkingSpace(l);
                        parkingLotInformationService.updateParkingLotInformation(parkingLotInformation);
                         String data = SerialPortUtils.addSerialPort(license,l);
                         log.info(license+"车主进入停车场内");
                        return data;
                    }
                } finally {
                    lock.unlock();
                }
            }else {
                    ParkingFixedparkingspace parkingFixedparkingspace=parkingFixedparkingspaceService.findByParkingLotInformationIdAndLicense(parkingLotInformation.getId(),license);
                    ParkingWhiteList byLicense = parkingWhiteListMapper.findByLicense(license, parkingLotInformation.getId());
                    if (parkingFixedparkingspace!=null || byLicense!=null  ){
                        //加锁
                        lock.lock();
                        try {
                            //保存进场信息
                            saveParkingRecord(date, parkingLotEquipment, carColor, s, name, license, parkingLotInformation.getId(), imagePath);
                            //设备开闸
                            SwitchOn(total.getAlarmInfoPlate().getIpaddr());
                            // 停车场车位数减一
                            long l = parkingLotInformation.getRemainingParkingSpace() - 1;
                            parkingLotInformation.setRemainingParkingSpace(l);
                            parkingLotInformationService.updateParkingLotInformation(parkingLotInformation);
                            String data = SerialPortUtils.addSerialPort(license, l);
                            log.info(license + "车主进场");
                            return data;
                        }finally {
                            lock.unlock();
                        }
                    }
            }
        }
        else {
            //判断设备是否可用
            if (parkingLotEquipment.getState().equals("1")){
                String data = SerialPortUtils.equipmentAbnormality();
                log.info(parkingLotEquipment.getName()+"设备异常");
                return data;
            }
            //查询没有支付得记录信息
            ParkingRecord parkingRecord=parkingRecordService.findByParkingLotInformationLicenseAndPayOrder(parkingLotInformation.getId(),license);
            ParkingWhiteList byLicense = parkingWhiteListMapper.findByLicense(license, parkingLotInformation.getId());
            //特定车辆
            String specificvehicleexit = parkingLotEquipment.getSpecificvehicleexit();
            if (specificvehicleexit.equals("0")){
                int type1 = total.getAlarmInfoPlate().getResult().getPlateResult().getType();
                int []type={5,6,8,9,10,11,15,16,17};
                boolean contains = Arrays.asList(type).contains(type1);
                if (contains){
                    //停车场车位数加一
                    updateRemainingParkingSpace(parkingLotInformation);
                    //获取出闸口设备名称
                    String name = parkingLotEquipment.getName();
                    //获取停车场id
                    parkingRecord.setExittime(date);
                    parkingRecord.setPayTime(new Date());
                    //有bug 要改进
                    parkingRecord.setPaystate("1");
                    parkingRecord.setOrderstate("1");
                    parkingRecord.setNumberthree("data:image/jpg;base64,"+imagePath);
                    // 获取全部的进出口名称
                    String allName = parkingRecord.getEntranceandexitname();
                    // 用逗号分隔符分割开
                    String [] a = allName.split(",");
                    // 判断分割的长度是否小于2
                    if (a.length<2){
                        // 如果小于2直接赋值进数据库
                        parkingRecord.setEntranceandexitname(parkingRecord.getEntranceandexitname()+","+name);
                    }else {
                        // 如果大于2就提取最后一次出口的名字
                        String name1 = parkingLotEquipment.getName();
                        // 获取入口的长度值
                        int aa = a[0].length();
                        // 提取入口名字
                        String b = allName.substring(0,aa);
                        // 把入口名字和提取出的最后一次出口名字进行拼接并赋值
                        parkingRecord.setEntranceandexitname(b+","+name1);
                    }
                    parkingRecord.setPaymentmethod("特定车辆");
                    //实际支金额
                    parkingRecord.setMoney(0L);
                    //优惠金额
                    parkingRecord.setDiscountamount(0L);
                    //应收金额
                    parkingRecord.setAmountpayable(0L);
                    //出场后修改停车场记录
                    parkingRecordService.updateParkingRecord(parkingRecord);
                    List<ParkingRecord> list = new ArrayList<>();
                    list.add(parkingRecord);
                    String s = JSON.toJSONString(list);
                    List<SysUser> list1 = sysUserMapper.findUserList(parkingLotInformation.getId());
                    for (SysUser user : list1) {
                        webSocketService.sendMessage(user.getUserName(),s);
                    }
                    log.info(license+"特定车辆，直接放行");
                    String data = SerialPortUtils.specificvehicleexit(license);
                    return data;
                }
            }
            // 判断是否在白名单范围  在直接放行
            if (byLicense!=null){
                if (!byLicense.getState().equals("1")){
                    //停车场车位数加一
                    updateRemainingParkingSpace(parkingLotInformation);
                    //获取出闸口设备名称
                    String name = parkingLotEquipment.getName();
                    //获取停车场id
                    Long id = parkingLotInformation.getId();
                    parkingRecord.setExittime(date);
                    parkingRecord.setPayTime(new Date());
                    //有bug 要改进
                    parkingRecord.setPaystate("1");
                    parkingRecord.setOrderstate("1");
                    parkingRecord.setMoney(0L);
                    parkingRecord.setNumberthree("data:image/jpg;base64,"+imagePath);
                    // 获取全部的进出口名称
                    String allName = parkingRecord.getEntranceandexitname();
                    // 用逗号分隔符分割开
                    String [] a = allName.split(",");
                    // 判断分割的长度是否小于2
                    if (a.length<2){
                        // 如果小于2直接赋值进数据库
                        parkingRecord.setEntranceandexitname(parkingRecord.getEntranceandexitname()+","+name);
                    }else {
                        // 如果大于2就提取最后一次出口的名字
                        String name1 = parkingLotEquipment.getName();
                        // 获取入口的长度值
                        int aa = a[0].length();
                        // 提取入口名字
                        String b = allName.substring(0,aa);
                        // 把入口名字和提取出的最后一次出口名字进行拼接并赋值
                        parkingRecord.setEntranceandexitname(b+","+name1);
                    }
                    parkingRecord.setPaymentmethod("白名单");
                    //实际支金额
                    parkingRecord.setMoney(0L);
                    //优惠金额
                    parkingRecord.setDiscountamount(0L);
                    //应收金额
                    parkingRecord.setAmountpayable(0L);
                    //出场后修改停车场记录

                    parkingRecordService.updateParkingRecord(parkingRecord);
                    List<ParkingRecord> list = new ArrayList<>();
                    list.add(parkingRecord);
                    String s = JSON.toJSONString(list);
                    List<SysUser> list1 = sysUserMapper.findUserList(parkingLotInformation.getId());
                    for (SysUser user : list1) {
                        webSocketService.sendMessage(user.getUserName(),s);
                    }
                    Date starttime = byLicense.getStarttime();
                    Date endtime = byLicense.getEndtime();
                    long l = (endtime.getTime() - date.getTime()) / (1000 * 60 * 60 * 24);
                    String data = SerialPortUtils.whiteList(license,l);
                    log.info(license+"白名单车主，直接放行");
                    return data;
                }
            }
            //   判断有无优惠卷次卷 停车场id 车牌号  次卷  状态(待使用状态)
           ParkingCouponrecord parkingCouponrecord=parkingCouponrecordService.findByParkingLotInformationIdAndLicense( parkingLotInformation.getId(),license);
            if (parkingCouponrecord!=null){
                //出场后修改停车场记录
                String s = update(parkingLotEquipment, parkingLotInformation, date, license, imagePath);
                //停车场车位数加一
                updateRemainingParkingSpace(parkingLotInformation);
                parkingCouponrecord.setState("1");
                parkingCouponrecord.setOrdernumber(s);
                parkingCouponrecordService.updateParkingCouponrecord(parkingCouponrecord);
                //出口次卷下发临显指令
                String data = SerialPortUtils.ExportSecondaryVolume(license);
                log.info(license+"拥有次卷优惠卷直接放行");
                return data;
            }

            // 2022/12/13 判断是否是固定停车位
          ParkingFixedparkingspace parkingFixedparkingspace=parkingFixedparkingspaceService.findByParkingLotInformationIdAndLicense(parkingLotInformation.getId(),license);
            if (parkingFixedparkingspace!=null && date.getTime()<parkingFixedparkingspace.getEndtime().getTime()){
                //停车场车位数加一
                updateRemainingParkingSpace(parkingLotInformation);
                //获取出闸口设备名称
                String name = parkingLotEquipment.getName();
                //获取停车场id
                parkingRecord.setExittime(date);
                parkingRecord.setPayTime(new Date());
                //有bug 要改进
                parkingRecord.setPaystate("1");
                parkingRecord.setOrderstate("1");
                parkingRecord.setMoney(0L);
                parkingRecord.setNumberthree("data:image/jpg;base64,"+imagePath);
                // 获取全部的进出口名称
                String allName = parkingRecord.getEntranceandexitname();
                // 用逗号分隔符分割开
                String [] a = allName.split(",");
                // 判断分割的长度是否小于2
                if (a.length<2){
                    // 如果小于2直接赋值进数据库
                    parkingRecord.setEntranceandexitname(parkingRecord.getEntranceandexitname()+","+name);
                }else {
                    // 如果大于2就提取最后一次出口的名字
                    String name1 = parkingLotEquipment.getName();
                    // 获取入口的长度值
                    int aa = a[0].length();
                    // 提取入口名字
                    String b = allName.substring(0,aa);
                    // 把入口名字和提取出的最后一次出口名字进行拼接并赋值
                    parkingRecord.setEntranceandexitname(b+","+name1);
                }
                parkingRecord.setPaymentmethod("固定车位车辆");
                //实际支金额
                parkingRecord.setMoney(0L);
                //优惠金额
                parkingRecord.setDiscountamount(0L);
                //应收金额
                parkingRecord.setAmountpayable(0L);
                //出场后修改停车场记录
                parkingRecordService.updateParkingRecord(parkingRecord);
                List<ParkingRecord> list = new ArrayList<>();
                list.add(parkingRecord);
                String s = JSON.toJSONString(list);
                List<SysUser> list1 = sysUserMapper.findUserList(parkingLotInformation.getId());
                for (SysUser user : list1) {
                    webSocketService.sendMessage(user.getUserName(),s);
                }
                String data = SerialPortUtils.ExportSecondaryVolume(license);
                log.info("固定停车位车主，直接放行");
                return data;
            }

            //停车场内支付业务逻辑
            //通过 停车场id，车牌号，和支付状态查询是否有无未支付订单，没有放行
            if (parkingRecord==null){
               //通过停车场id，车牌号,当前时间（通过时间排序获取最近时间）获取出场车停车记录
               ParkingRecord parkingRecord1=parkingRecordService.findByParkingLotInformationLicense1(parkingLotInformation.getId(),license);
               //判断支付时间和当前开闸时间是否超过停车场设置离场时间
                long l = DateTime.dateDiff(parkingRecord1.getPayTime(),date);
                if (l>parkingLotInformation.getPayleavingtime()){
                    //  超时补费
                    //开启超时补费
                    if (parkingLotInformation.getOvertimecompensation().equals("0")){
                        ParkingChargingDto parkingChargingDto = new ParkingChargingDto(parkingLotInformation.getId(),parkingRecord1.getPayTime(),date,license);
                        //金额
                        MoneyVo moneyVo = parkingChargingService.overtimeCompensation(parkingChargingDto);
                        //获取出闸口设备名称
                        String name = parkingLotEquipment.getName();
                        parkingRecord1.setExittime(date);
                        //保存出场照片
                        parkingRecord1.setNumberthree("data:image/jpg;base64,"+imagePath);
                        //超时补费状态
                        parkingRecord1.setPaystate("2");
                        parkingRecord1.setOrderstate("1");
                        // 获取全部的进出口名称
                        String allName = parkingRecord1.getEntranceandexitname();
                        // 用逗号分隔符分割开
                        String [] a = allName.split(",");
                        // 判断分割的长度是否小于2
                        if (a.length<2){
                            // 如果小于2直接赋值进数据库
                            parkingRecord1.setEntranceandexitname(parkingRecord1.getEntranceandexitname()+","+name);
                        }else {
                            // 如果大于2就提取最后一次出口的名字
                            String name1 = parkingLotEquipment.getName();
                            // 获取入口的长度值
                            int aa = a[0].length();
                            // 提取入口名字
                            String b = allName.substring(0,aa);
                            // 把入口名字和提取出的最后一次出口名字进行拼接并赋值
                            parkingRecord1.setEntranceandexitname(b+","+name1);
                        }
                        parkingRecordService.updateParkingRecord(parkingRecord1);
                        ParkingRecordVo parkingRecordVo = new ParkingRecordVo();
                        //超时补费金额
                        parkingRecord1.setMoney(moneyVo.getMoney());
                        BeanUtils.copyProperties(parkingRecord1,parkingRecordVo);
                        parkingRecordVo.setParkingLotInformationName(parkingLotInformation.getName());
                        parkingRecordVo.setParkingLotEquipmentName(parkingLotEquipment.getName());
                        parkingRecordVo.setParkinglotequipmentid(parkingLotEquipment.getId());
                        redisTemplate.opsForValue().set(String.valueOf(parkingLotEquipment.getId()),parkingRecordVo,5, TimeUnit.MINUTES);
                        String data = SerialPortUtils.overtime(l,license,moneyVo.getMoney());
                        log.info("场内支付超过离场时间，等待支付 ");
                        return data;
                    }
               }
               //出场后修改停车场记录
               updateparkingRecord(parkingLotEquipment, parkingLotInformation,date,license,imagePath);
               //停车场车位数加一
               updateRemainingParkingSpace(parkingLotInformation);
               //进场时间  出场时间date
                Date admissiontime = parkingRecord1.getAdmissiontime();
                long l1 = (date.getTime()-admissiontime.getTime()) / (1000*60);
                String data1 = SerialPortUtils.Exit(license,l1);
                log.info("场内支付未超过离场时间 ");
                return data1;
           }
           //门闸口支付业务逻辑  
            else {
               //停车场免费时常
               Long freetime = parkingLotInformation.getFreetime();

               long time = DateTime.dateDiff(parkingRecord.getAdmissiontime(), date);

               //如果停车场免费时常大于实际停车时间  放行
               if(freetime>time){
                   //停车场车位数加一
                   updateRemainingParkingSpace(parkingLotInformation);
                   //获取出闸口设备名称PayOrder
                   String name = parkingLotEquipment.getName();

                   parkingRecord.setExittime(date);
                   //有bug 要改进
                   parkingRecord.setPaystate("1");
                   parkingRecord.setOrderstate("1");
                   parkingRecord.setMoney(0L);
                   parkingRecord.setNumberthree("data:image/jpg;base64,"+imagePath);
                   parkingRecord.setEntranceandexitname(parkingRecord.getEntranceandexitname()+","+name);
                   parkingRecord.setPaymentmethod("免费");
                   //支付时间
                   parkingRecord.setPayTime(new Date());
                   //实际支金额
                   parkingRecord.setMoney(0L);
                   //优惠金额
                   parkingRecord.setDiscountamount(0L);
                   //应收金额
                   parkingRecord.setAmountpayable(0L);
                   //出场后修改停车场记录
                   parkingRecordService.updateParkingRecord(parkingRecord);
                   List<ParkingRecord> list = new ArrayList<>();
                   list.add(parkingRecord);
                   String s = JSON.toJSONString(list);
                   //WebSocketService发送给前端消息
                   List<SysUser> list1 = sysUserMapper.findUserList(parkingLotInformation.getId());
                   for (SysUser user : list1) {
                       webSocketService.sendMessage(user.getUserName(),s);
                   }
                   Date admissiontime = parkingRecord.getAdmissiontime();
                   long l1 = (date.getTime()-admissiontime.getTime()) / (1000*60);
                   String data1 = SerialPortUtils.Exit(license,l1);
                   log.info("停车时间在免费时间段内");
                   return data1;
               }
               List<ParkingRecord> list = new ArrayList<>();
               //订单正在进行中
               parkingRecord.setOrderstate("2");
                //保存出场照片
                parkingRecord.setNumberthree("data:image/jpg;base64,"+imagePath);
                parkingRecord.setExittime(date);

                // 获取全部的进出口名称
                String allName = parkingRecord.getEntranceandexitname();
                // 用逗号分隔符分割开
                String [] a = allName.split(",");
                // 判断分割的长度是否小于2
                if (a.length<2){
                    // 如果小于2直接赋值进数据库
                    parkingRecord.setEntranceandexitname(parkingRecord.getEntranceandexitname()+","+parkingLotEquipment.getName());
                }else {
                    // 如果大于2就提取最后一次出口的名字
                    String name = parkingLotEquipment.getName();
                    // 获取入口的长度值
                    int aa = a[0].length();
                    // 提取入口名字
                    String b = allName.substring(0,aa);
                    // 把入口名字和提取出的最后一次出口名字进行拼接并赋值
                    parkingRecord.setEntranceandexitname(b+","+name);
                }
               //修改订单状态为订单正在进行支付中
               //  2022/12/11 调用计费规则显示总费用 优惠金额  实际支付金额  优惠方式
                ParkingChargingDto parkingChargingDto = new ParkingChargingDto(parkingLotInformation.getId(),parkingRecord.getAdmissiontime(),date,license);
                //金额
                MoneyVo moneyVo = parkingChargingService.calculatedAmount(parkingChargingDto);
                BeanUtils.copyProperties(moneyVo,parkingRecord);
               parkingRecordService.updateParkingRecord(parkingRecord);
               list.add(parkingRecord);
               String s = JSON.toJSONString(list);
               //WebSocketService发送给前端消息
               List<SysUser> list1 = sysUserMapper.findUserList(parkingLotInformation.getId());
               for (SysUser user : list1) {
                   webSocketService.sendMessage(user.getUserName(),s);
               }
                ParkingRecordVo parkingRecordVo = new ParkingRecordVo();
                BeanUtils.copyProperties(parkingRecord,parkingRecordVo);
                parkingRecordVo.setParkingLotInformationName(parkingLotInformation.getName());
                parkingRecordVo.setParkingLotEquipmentName(parkingLotEquipment.getName());
                parkingRecordVo.setParkinglotequipmentid(parkingLotEquipment.getId());
                redisTemplate.opsForValue().set(String.valueOf(parkingLotEquipment.getId()),parkingRecordVo,5, TimeUnit.MINUTES);
                //进场时间  出场时间date
                Date admissiontime = parkingRecord.getAdmissiontime();
                long l1 = (date.getTime()-admissiontime.getTime()) / (1000*60);
                String data1 = SerialPortUtils.ExitOne(license,l1,moneyVo.getMoney());
                log.info("门闸口支付业务逻辑 ");
                return data1;
           }
        }
        log.info("{\"Response_AlarmInfoPlate\":{\"info\":\"no\",\"content\":\"程序异常，请联系管理人员\",\"is_pay\":\"true\"}}");
        return "{\"Response_AlarmInfoPlate\":{\"info\":\"no\",\"content\":\"程序异常，请联系管理人员\",\"is_pay\":\"true\"}}";
    }





    //出场后修改停车场记录
    private void updateparkingRecord(ParkingLotEquipment parkingLotEquipment, ParkingLotInformation parkingLotInformation,Date date,String license,String imagePath) {
        //获取出闸口设备名称
        String name = parkingLotEquipment.getName();
        //获取停车场id
        Long id = parkingLotInformation.getId();
        //通过停车场id，车牌号,当前时间（通过时间排序获取最近时间）获取出场车停车记录
        ParkingRecord parkingRecord=parkingRecordService.findByParkingLotInformationLicense1(parkingLotInformation.getId(),license);
        parkingRecord.setExittime(date);
        //保存出场照片
        parkingRecord.setNumberthree("data:image/jpg;base64,"+imagePath);
        //有bug 要改进
        parkingRecord.setPaystate("1");
        parkingRecord.setOrderstate("1");
        // 获取全部的进出口名称
        String allName = parkingRecord.getEntranceandexitname();
        // 用逗号分隔符分割开
        String [] a = allName.split(",");
        // 判断分割的长度是否小于2
        if (a.length<2){
            // 如果小于2直接赋值进数据库
            parkingRecord.setEntranceandexitname(parkingRecord.getEntranceandexitname()+","+name);
        }else {
            // 如果大于2就提取最后一次出口的名字
            String name1 = parkingLotEquipment.getName();
            // 获取入口的长度值
            int aa = a[0].length();
            // 提取入口名字
            String b = allName.substring(0,aa);
            // 把入口名字和提取出的最后一次出口名字进行拼接并赋值
            parkingRecord.setEntranceandexitname(b+","+name1);
        }
        //出场后修改停车场记录
        parkingRecordService.updateParkingRecord(parkingRecord);
        List<ParkingRecord> list = new ArrayList<>();
        list.add(parkingRecord);
        String s = JSON.toJSONString(list);
        //WebSocketService发送给前端消息
        List<SysUser> list1 = sysUserMapper.findUserList(parkingLotInformation.getId());
        for (SysUser user : list1) {
            webSocketService.sendMessage(user.getUserName(),s);
        }


    }
    //出场后修改停车场记录
    private String update(ParkingLotEquipment parkingLotEquipment, ParkingLotInformation parkingLotInformation,Date date,String license,String imagePath) {
        //获取出闸口设备名称
        String name = parkingLotEquipment.getName();
        //获取停车场id
        Long id = parkingLotInformation.getId();
        //通过停车场id，车牌号,当前时间（通过时间排序获取最近时间）获取出场车停车记录
        ParkingRecord parkingRecord=parkingRecordService.findByParkingLotInformationLicenseAndPayOrder(parkingLotInformation.getId(),license);
        parkingRecord.setExittime(date);
        //保存出场照片
        parkingRecord.setNumberthree("data:image/jpg;base64,"+imagePath);
        //有bug 要改进
        parkingRecord.setPaystate("1");
        parkingRecord.setOrderstate("1");
        parkingRecord.setPaymentmethod("优惠卷（次卷）");
        parkingRecord.setPayTime(date);
        //实际支金额
        parkingRecord.setMoney(0L);
        //优惠金额
        parkingRecord.setDiscountamount(0L);
        //应收金额
        parkingRecord.setAmountpayable(0L);
        // 获取全部的进出口名称
        String allName = parkingRecord.getEntranceandexitname();
        // 用逗号分隔符分割开
        String [] a = allName.split(",");
        // 判断分割的长度是否小于2
        if (a.length<2){
            // 如果小于2直接赋值进数据库
            parkingRecord.setEntranceandexitname(parkingRecord.getEntranceandexitname()+","+name);
        }else {
            // 如果大于2就提取最后一次出口的名字
            String name1 = parkingLotEquipment.getName();
            // 获取入口的长度值
            int aa = a[0].length();
            // 提取入口名字
            String b = allName.substring(0,aa);
            // 把入口名字和提取出的最后一次出口名字进行拼接并赋值
            parkingRecord.setEntranceandexitname(b+","+name1);
        }
        //出场后修改停车场记录
        parkingRecordService.updateParkingRecord(parkingRecord);
        List<ParkingRecord> list = new ArrayList<>();
        list.add(parkingRecord);
        String s = JSON.toJSONString(list);
        //WebSocketService发送给前端消息
        List<SysUser> list1 = sysUserMapper.findUserList(parkingLotInformation.getId());
        for (SysUser user : list1) {
            webSocketService.sendMessage(user.getUserName(),s);
        }
        return parkingRecord.getOrdernumber();

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
    private void saveParkingRecord(Date date, ParkingLotEquipment parkingLotEquipment,int carColor, String s, String name,String license,Long parkingLotInformationId,String imagePath) {

        //添加进场信息
        ParkingRecord parkingRecord1 = new ParkingRecord();
        parkingRecord1.setLicense(license);
        parkingRecord1.setAdmissiontime(date);
        //进场照片
        parkingRecord1.setNumbertwo("data:image/jpg;base64,"+imagePath);
        parkingRecord1.setParkinglotinformationid(parkingLotInformationId);
        /** 车牌颜色 */
        parkingRecord1.setLicensepllatecolor(String.valueOf(carColor));
        /** 订单编号 */
        parkingRecord1.setOrdernumber(s);
        /** 订单状态0代表未支付1代表已支付 */
        parkingRecord1.setOrderstate("0");
        /** 支付状态0代表未支付1代表已支付 */
        parkingRecord1.setPaystate("0");
        /** 出入口名称     出口时要拼接出入口名称*/
        parkingRecord1.setEntranceandexitname(name);
        parkingRecordService.insertParkingRecord(parkingRecord1);
    }



}
