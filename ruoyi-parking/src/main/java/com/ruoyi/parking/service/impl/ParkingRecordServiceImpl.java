package com.ruoyi.parking.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.ParkingLotInformation;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.sdk.LPRDemo;
import com.ruoyi.common.utils.CodeGenerateUtils;
import com.ruoyi.framework.websocket.WebSocketService;
import com.ruoyi.parking.domain.*;
import com.ruoyi.parking.dto.MoneyRes;
import com.ruoyi.parking.mapper.ParkingBlackListMapper;
import com.ruoyi.parking.mapper.ParkingCouponrecordMapper;
import com.ruoyi.parking.mapper.ParkingWhiteListMapper;
import com.ruoyi.parking.service.*;
import com.ruoyi.parking.dto.MoneyDto;
import com.ruoyi.parking.utils.SerialPortUtils;
import com.ruoyi.parking.vo.*;
import com.ruoyi.system.mapper.SysUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.ruoyi.parking.mapper.ParkingRecordMapper;
import org.springframework.transaction.annotation.Transactional;

/**
 * 停车记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-11-24
 */
@Service
@Slf4j
public class ParkingRecordServiceImpl implements IParkingRecordService 
{
    @Autowired
    private ParkingRecordMapper parkingRecordMapper;
    @Autowired
    private RedisTemplate<Object,Object>redisTemplate;
    @Autowired
    private IParkingLotEquipmentService parkingLotEquipmentService;
    @Autowired
    private IParkingLotInformationService parkingLotInformationService;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private WebSocketService webSocketService;
    @Autowired
    private ParkingBlackListMapper parkingBlackListMapper;
    @Autowired
    private IParkingCouponrecordService parkingCouponrecordService;
    @Autowired
    private ParkingCouponrecordMapper parkingCouponrecordMapper;
    @Autowired
    private ParkingWhiteListMapper parkingWhiteListMapper;
    @Autowired
    private IParkingFixedparkingspaceService parkingFixedparkingspaceService;
    @Autowired
    private ParkingChargingServiceImpl parkingChargingService;

    /**
     * 查询停车记录
     * 
     * @param id 停车记录主键
     * @return 停车记录
     */
    @Override
    public ParkingRecord selectParkingRecordById(Long id)
    {
        return parkingRecordMapper.selectParkingRecordById(id);
    }

    /**
     * 查询停车记录列表
     * 
     * @param parkingRecord 停车记录
     * @return 停车记录
     */
    @Override
    public List<ParkingRecord> selectParkingRecordList(ParkingRecord parkingRecord) {

        return parkingRecordMapper.selectParkingRecordList(parkingRecord);
    }

    /**
     * 新增停车记录
     * 
     * @param parkingRecord 停车记录
     * @return 结果
     */
    @Override
    public int insertParkingRecord(ParkingRecord parkingRecord)
    {
        return parkingRecordMapper.insertParkingRecord(parkingRecord);
    }

    /**
     * 修改停车记录
     * 
     * @param parkingRecord 停车记录
     * @return 结果
     */
    @Override
    public int updateParkingRecord(ParkingRecord parkingRecord)
    {
        return parkingRecordMapper.updateParkingRecord(parkingRecord);
    }

    /**
     * 批量删除停车记录
     * 
     * @param ids 需要删除的停车记录主键
     * @return 结果
     */
    @Override
    public int deleteParkingRecordByIds(Long[] ids)
    {
        return parkingRecordMapper.deleteParkingRecordByIds(ids);
    }

    /**
     * 删除停车记录信息
     * 
     * @param id 停车记录主键
     * @return 结果
     */
    @Override
    public int deleteParkingRecordById(Long id)
    {
        return parkingRecordMapper.deleteParkingRecordById(id);
    }
    //通过车牌号查询有无未支付订单
    @Override
    public ParkingRecord findByLicense(String license,Long ParkingLotInformationId) {
        return parkingRecordMapper.findByLicense(license,ParkingLotInformationId);
    }

    //通过车牌号，未支付状态查询出来修改订单支付单状态  有牌无牌车公用开闸接口  支付方式
    @Override
    @Transactional
    public void editPayState(Long parkingLotInformationId,String license ,Long parkinglotequipmentid,String paymentMethod) {
        ParkingLotEquipment parkingLotEquipment = parkingLotEquipmentService.selectParkingLotEquipmentById(parkinglotequipmentid);
        ParkingRecord parkingRecord = parkingRecordMapper.findByLicense(license, parkingLotInformationId);
        ParkingLotInformation parkingLotInformation = parkingLotInformationService.selectParkingLotInformationById(parkingLotInformationId);
        if (parkingRecord!=null){
            parkingRecord.setPaystate("1");
            parkingRecord.setOrderstate("1");
            parkingRecord.setPayTime(new Date());
            parkingRecord.setPaymentmethod(paymentMethod);
            //  2022/12/15  修改优惠卷状态 一个人只有一种优惠
            ParkingCouponrecord byParkingLotInformationIdAndLicense = parkingCouponrecordMapper.findByParkingLotInformationIdAndLicenseTo(parkingLotInformationId, license);
            if (byParkingLotInformationIdAndLicense!=null){
                byParkingLotInformationIdAndLicense.setState("1");
                parkingCouponrecordMapper.updateParkingCouponrecord(byParkingLotInformationIdAndLicense);
            }
            //开闸处理
            extracted(license, parkingLotEquipment);
            updateParkingRecord(parkingRecord);
            List<ParkingRecord> list = new ArrayList<>();
            list.add(parkingRecord);
            send(parkinglotequipmentid, list);
            updateRemainingParkingSpace(parkingLotInformation);
            log.info("无超时补费信息，门口处缴费回调开闸");
        }
        else {
            //超时补费
            ParkingRecord findbypaystateandlicense = findbypaystateandlicense(parkingLotInformationId, license);
            if (findbypaystateandlicense!=null){
                //从redis获取需要补费金额
                Long money = (Long)redisTemplate.opsForValue().get(parkinglotequipmentid + "Overtimefee");
                if(money==null){
                    log.info("redis无补费金额信息");
                    return;
                }
                log.info("redis有补费金额信息开闸");
                //改变状态
                updateState(license, parkingLotEquipment, findbypaystateandlicense, money);
                List<ParkingRecord> list = new ArrayList<>();
                list.add(findbypaystateandlicense);
                send(parkinglotequipmentid, list);
                updateRemainingParkingSpace(parkingLotInformation);
                redisTemplate.delete(parkinglotequipmentid + "Overtimefee");
            }
        }

    }
    //修改状态并开闸
    private void updateState(String license, ParkingLotEquipment parkingLotEquipment, ParkingRecord findbypaystateandlicense, Long money) {
        findbypaystateandlicense.setPayTime(new Date());
        findbypaystateandlicense.setPaystate("1");
        findbypaystateandlicense.setOrderstate("1");
        findbypaystateandlicense.setMoney(findbypaystateandlicense.getMoney()+ money);
        findbypaystateandlicense.setAmountpayable(findbypaystateandlicense.getAmountpayable()+ money);
        //开闸处理
        extracted(license, parkingLotEquipment);
        updateParkingRecord(findbypaystateandlicense);
    }
    //websocket发送
    private void send(Long parkinglotequipmentid, List<ParkingRecord> list) {
        String s = JSON.toJSONString(list);
        //WebSocketService发送给前端消息
        List<SysUser> list1 = sysUserMapper.findUserList(parkinglotequipmentid);
        for (SysUser user : list1) {
            webSocketService.sendMessage(user.getUserName(), s);
        }
    }
    //开闸
    private void extracted(String license, ParkingLotEquipment parkingLotEquipment) {
        LPRDemo lprDemo = new LPRDemo();
        //初始化返回句柄
        int handle = lprDemo.InitClient(parkingLotEquipment.getIpadress());
        //开闸
        int i1 = lprDemo.switchOn(handle, 0, 500);
        List<byte[]> list2 = SerialPortUtils.payAfter(license);
        //485串口发送数据
        lprDemo.SendSerialData(handle,list2);
        //关闭设备的控制句柄
        lprDemo.VzLPRClient_Close(handle);
        //执行结束释放
        lprDemo.VzLPRClient_Cleanup();
    }

    //无牌车
    @Override
    public AjaxResult echoInformationToLicense(Long parkinglotequipmentid, String openid) {
        Date date = new Date();
        ParkingLotEquipment parkingLotEquipment = parkingLotEquipmentService.selectParkingLotEquipmentById(parkinglotequipmentid);
        ParkingRecord parkingRecord = parkingRecordMapper.findByOpenid(openid, parkingLotEquipment.getParkinglotinformationid());
        if (parkingRecord==null){
            log.info("无牌车无停车记录");
            return AjaxResult.error("无停车记录");
        }
        ParkingLotInformation parkingLotInformation = parkingLotInformationService.selectParkingLotInformationById(parkingLotEquipment.getParkinglotinformationid());
        parkingRecord.setOrderstate("2");
        parkingRecord.setExittime(date);
        String license = parkingRecord.getLicense();
        ParkingChargingDto parkingChargingDto = new ParkingChargingDto(parkingLotInformation.getId(),parkingRecord.getAdmissiontime(),new Date(),license);
        //金额
        MoneyVo moneyVo = parkingChargingService.calculatedAmount(parkingChargingDto);
        parkingRecord.setEntranceandexitname(parkingRecord.getEntranceandexitname()+","+parkingLotEquipment.getName());
        //修改停车记录
        BeanUtils.copyProperties(moneyVo,parkingRecord);
        parkingRecordMapper.updateParkingRecord(parkingRecord);

        ParkingRecordVo parkingRecordVo = new ParkingRecordVo();
        BeanUtils.copyProperties(parkingRecord,parkingRecordVo);

        parkingRecordVo.setParkingLotInformationName(parkingLotInformation.getName());
        parkingRecordVo.setParkingLotEquipmentName(parkingLotEquipment.getName());
        parkingRecordVo.setParkinglotequipmentid(parkingLotEquipment.getId());
        return AjaxResult.success(parkingRecordVo);
    }

    //室内扫码回显支付信息
    @Override
    public AjaxResult indoor(Long parkingLotInformationId, String license) {
        //  2022/12/15 判断是否白名单  固定车位  是否拥有次卷   如果是提示用户可以直接出场
        ParkingCouponrecord parkingCouponrecord=parkingCouponrecordService.findByParkingLotInformationIdAndLicense(parkingLotInformationId,license);
        ParkingWhiteList byLicense = parkingWhiteListMapper.findByLicense(license,parkingLotInformationId);
        ParkingFixedparkingspace parkingFixedparkingspace=parkingFixedparkingspaceService.findByParkingLotInformationIdAndLicense(parkingLotInformationId,license);
        if (byLicense!=null){
            return AjaxResult.error("您是白名单车主");
        }
        if (parkingFixedparkingspace!=null){
            return AjaxResult.error("您是固定车位车主");
        }
        if (parkingCouponrecord!=null){
            return AjaxResult.error("拥有次数优惠卷，请直接离场");
        }
        ParkingRecord parkingRecord = parkingRecordMapper.findByLicense(license, parkingLotInformationId);
        if (parkingRecord==null){
            return AjaxResult.error("无停车记录");
        }
        ParkingLotInformation parkingLotInformation = parkingLotInformationService.selectParkingLotInformationById(parkingLotInformationId);
        ParkingRecordVo parkingRecordVo = new ParkingRecordVo();
        BeanUtils.copyProperties(parkingRecord,parkingRecordVo);
        parkingRecordVo.setParkingLotInformationName(parkingLotInformation.getName());
        // 调用计费规则显示总费用 优惠金额  实际支付金额  优惠方式(支付方式后面加优惠卷)  计算方式时当前时间计算  回调接口传过来所有信息 保存
        ParkingChargingDto parkingChargingDto = new ParkingChargingDto(parkingLotInformationId,parkingRecord.getAdmissiontime(),new Date(),license);
        //金额
        MoneyVo moneyVo = parkingChargingService.calculatedAmount(parkingChargingDto);
        BeanUtils.copyProperties(moneyVo,parkingRecordVo);
        return AjaxResult.success(parkingRecordVo);
    }




    //室内扫码回调
    @Override
    public AjaxResult indoorCallback(ParkingRecordVo parkingRecordVo) {
        ParkingRecord parkingRecord = parkingRecordMapper.findByLicense(parkingRecordVo.getLicense(), parkingRecordVo.getParkinglotinformationid());
        BeanUtils.copyProperties(parkingRecordVo,parkingRecord);
        parkingRecord.setPaystate("1");
        parkingRecord.setOrderstate("1");
        parkingRecord.setPayTime(new Date());
        updateParkingRecord(parkingRecord);
        ParkingCouponrecord byParkingLotInformationIdAndLicense = parkingCouponrecordMapper.findByParkingLotInformationIdAndLicenseTo(parkingRecordVo.getParkinglotinformationid(), parkingRecordVo.getLicense());
        if (byParkingLotInformationIdAndLicense!=null){
            byParkingLotInformationIdAndLicense.setState("1");
            parkingCouponrecordMapper.updateParkingCouponrecord(byParkingLotInformationIdAndLicense);
        }
        return AjaxResult.success("请五分钟内离场");
    }
    @Override
    public ParkingRecord findByLicense1(String license, Long ParkingLotInformationId) {
        return  parkingRecordMapper.findByLicense1(license,ParkingLotInformationId);
    }
    //通过停车场id，车牌号,当前时间（通过时间排序获取最近时间）获取出场车停车记录
    @Override
    public ParkingRecord findByParkingLotInformationLicense1(Long id, String license) {
      List<ParkingRecord>list = parkingRecordMapper.findByParkingLotInformationLicense1(id, license);
      if (list.size()!=0){
          ParkingRecord parkingRecord = list.get(0);
          return parkingRecord;
      }
        return null;
    }

    @Override
    public ParkingRecord findByParkingLotInformationLicenseAndPayOrder(Long id, String license) {
        return  parkingRecordMapper.findByParkingLotInformationLicenseAndPayOrder(id, license);
    }

    //查询指定停车场最近离场记录
    @Override
    public List<ParkingRecord> getPayRecord(Long id) {
        if (id!=0){
            List<ParkingRecord> payRecord = parkingRecordMapper.getPayRecord(id);
            ParkingRecord parkingRecord = payRecord.get(0);
            List<ParkingRecord> list = new ArrayList<>();
            list.add(parkingRecord);
            return list;
        }
        //等于o代表是超级管理员 不要求查看
      return null;
    }

    @Override
    @Transactional
    public AjaxResult updateToRecord(Long id) {
        ParkingRecord parkingRecord = parkingRecordMapper.selectParkingRecordById(id);
        parkingRecord.setPaymentmethod("现金支付");
        parkingRecord.setOrderstate("1");
        parkingRecord.setPaystate("1");
        parkingRecord.setPayTime(new Date());
        parkingRecordMapper.updateParkingRecord(parkingRecord);
        ParkingCouponrecord byParkingLotInformationIdAndLicense = parkingCouponrecordMapper.findByParkingLotInformationIdAndLicenseTo(parkingRecord.getParkinglotinformationid(), parkingRecord.getLicense());
        if (byParkingLotInformationIdAndLicense!=null){
            byParkingLotInformationIdAndLicense.setState("1");
            parkingCouponrecordMapper.updateParkingCouponrecord(byParkingLotInformationIdAndLicense);
        }
       /* List<ParkingRecord> list = new ArrayList<>();
        list.add(parkingRecord);
        String s = JSON.toJSONString(list);
        List<SysUser> list1 = sysUserMapper.findUserList(parkingRecord.getId());
        for (SysUser user : list1) {
            webSocketService.sendMessage(user.getUserName(),s);
        }*/
        //停车场id加车牌
        redisTemplate.opsForValue().set(parkingRecord.getParkinglotinformationid()+parkingRecord.getLicense(),parkingRecord.getLicense(),5, TimeUnit.MICROSECONDS);
        return AjaxResult.success();
    }

    @Override
    @Transactional
    public AjaxResult updateToRecordFromCoupon(Long id) {
        ParkingRecord parkingRecord = parkingRecordMapper.selectParkingRecordById(id);
        parkingRecord.setPaymentmethod("优惠卷代扣");
        parkingRecord.setOrderstate("1");
        parkingRecord.setPaystate("1");
        parkingRecord.setPayTime(new Date());
        ParkingCouponrecord byParkingLotInformationIdAndLicense = parkingCouponrecordMapper.findByParkingLotInformationIdAndLicenseTo(parkingRecord.getParkinglotinformationid(), parkingRecord.getLicense());
        if (byParkingLotInformationIdAndLicense!=null){
            byParkingLotInformationIdAndLicense.setState("1");
            parkingCouponrecordMapper.updateParkingCouponrecord(byParkingLotInformationIdAndLicense);
        }
        parkingRecordMapper.updateParkingRecord(parkingRecord);
        //停车场id加车牌
        redisTemplate.opsForValue().set(parkingRecord.getParkinglotinformationid()+parkingRecord.getLicense(),parkingRecord.getLicense(),5, TimeUnit.MICROSECONDS);
        return AjaxResult.success();
    }
    //添加无牌车接口
    @Override
    public AjaxResult noLicensePlate(Long parkinglotequipmentid, String license,String openid) {
        if (parkinglotequipmentid==null){
            return AjaxResult.error("网络异常，稍后重试");
        }
        if (license==null){
            return AjaxResult.error("未输入车牌");
        }
        //获取停车场设备信息
        ParkingLotEquipment parkingLotEquipment = parkingLotEquipmentService.selectParkingLotEquipmentById(parkinglotequipmentid);
        ParkingLotInformation parkingLotInformation = parkingLotInformationService.selectParkingLotInformationById(parkingLotEquipment.getParkinglotinformationid());
        //黑名单拒绝放行
        ParkingBlackList parkingBlackList= parkingBlackListMapper.selectParkingBlackListByIdAndLicense(parkingLotInformation.getId(),license);
        if (parkingBlackList!=null){

            return AjaxResult.error("黑名单拒绝进入");
        }
        //通过停车场id，车牌号查询有无未支付订单
        ParkingRecord parkingRecord= parkingRecordMapper.findByLicense(license,parkingLotInformation.getId());
        if (parkingRecord!=null){
            //如果有就删除掉
            parkingRecordMapper.deleteParkingRecordById(parkingRecord.getId());
        }

        LPRDemo lprDemo = new LPRDemo();
        //初始化返回句柄
        int handle = lprDemo.InitClient(parkingLotEquipment.getIpadress());
        //开闸
        int i1 = lprDemo.switchOn(handle, 0, 500);
        List<byte[]> wupaiche = SerialPortUtils.wupaiche(license);
        //485串口发送数据
        lprDemo.SendSerialData(handle,wupaiche);
        //停车场车位数加一
        updateRemainingParkingSpace(parkingLotInformation);
        //添加无牌车进场记录
        addParkingRecord(license, parkingLotEquipment,openid);
        //关闭设备的控制句柄
        lprDemo.VzLPRClient_Close(handle);
        //执行结束释放
        lprDemo.VzLPRClient_Cleanup();
        return AjaxResult.success();
    }

    @Override
    //@Cacheable(cacheNames = "money",key = "#id")
    public AjaxResult getMoney(Long id) {
        if (id !=0) {
            String year = String.valueOf(new Date().getYear()+1900);
            List<MoneyDto> list = parkingRecordMapper.getMoney(id, year);
            MoneyRes moneyRes = new MoneyRes();
            for (MoneyDto moneyDto : list) {
                moneyRes.getMoney().add(moneyDto.getMoney());
                moneyRes.getMonth().add(moneyDto.getMonth());
            }
            return AjaxResult.success(moneyRes);
        }
        return null;
    }

    @Override
    public AjaxResult getDailyInformation(Long id) {
        DailyInformationVo dailyInformationVo = new DailyInformationVo();
        if (id !=0) {
            dailyInformationVo.getName().add("今日停车未出场数");
            Long count=parkingRecordMapper.getDailyInformation(id);
            dailyInformationVo.getParkingLots().add(new ParkingLots(count,"今日停车未出场数"));
            dailyInformationVo.getName().add("今日停车已出场数");
            Long count1=parkingRecordMapper.getDailyInformations(id);
            dailyInformationVo.getParkingLots().add(new ParkingLots(count1,"今日停车已出场数"));
            return AjaxResult.success(dailyInformationVo);
        }
        return AjaxResult.success(dailyInformationVo);
    }

    @Override
    public ParkingRecord findbypaystateandlicense(Long parkinglotinformationid, String license) {
        return  parkingRecordMapper.findbypaystateandlicense( parkinglotinformationid,license);

    }

    //停车场车位数加一
    private void updateRemainingParkingSpace(ParkingLotInformation parkingLotInformation) {
        Long remainingParkingSpace = parkingLotInformation.getRemainingParkingSpace();
        parkingLotInformation.setRemainingParkingSpace(remainingParkingSpace+1);
        parkingLotInformationService.updateParkingLotInformation(parkingLotInformation);

    }

    //添加无牌车进场记录
    private void addParkingRecord(String license, ParkingLotEquipment parkingLotEquipment,String openid) {
        ParkingRecord parkingRecord = new ParkingRecord();
        parkingRecord.setLicense(license);
        parkingRecord.setParkinglotinformationid(parkingLotEquipment.getParkinglotinformationid());
        parkingRecord.setAdmissiontime(new Date());
        parkingRecord.setLicensepllatecolor("0");
        parkingRecord.setOpenid(openid);
        //生成订单编号
        parkingRecord.setOrdernumber(CodeGenerateUtils.generateUnionPaySn());
        parkingRecord.setPaystate("0");
        parkingRecord.setOrderstate("0");
        parkingRecord.setEntranceandexitname(parkingLotEquipment.getName());
        parkingRecordMapper.insertParkingRecord(parkingRecord);
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



}
