package com.ruoyi.parking.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.ParkingLotEquipment;
import com.ruoyi.common.core.domain.entity.ParkingLotInformation;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.sdk.LPRDemo;
import com.ruoyi.common.utils.CodeGenerateUtils;
import com.ruoyi.parking.dto.*;
import com.ruoyi.parking.utils.HttpRequest;
import com.ruoyi.framework.websocket.WebSocketService;
import com.ruoyi.parking.domain.*;
import com.ruoyi.parking.mapper.ParkingBlackListMapper;
import com.ruoyi.parking.mapper.ParkingCouponrecordMapper;
import com.ruoyi.parking.mapper.ParkingWhiteListMapper;
import com.ruoyi.parking.service.*;
import com.ruoyi.parking.utils.SerialPortUtils;
import com.ruoyi.parking.vo.*;
import com.ruoyi.system.mapper.SysUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ParkingRecordServiceImpl implements IParkingRecordService {
    @Autowired
    private ParkingRecordMapper parkingRecordMapper;
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;
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
    @Autowired
    private IParkingRechargeService parkingRechargeService;

    /**
     * 查询停车记录
     *
     * @param id 停车记录主键
     * @return 停车记录
     */
    @Override
    public ParkingRecord selectParkingRecordById(Long id) {
        ParkingRecord parkingRecord = parkingRecordMapper.selectParkingRecordById(id);

        return parkingRecord;
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
    public int insertParkingRecord(ParkingRecord parkingRecord) {
        return parkingRecordMapper.insertParkingRecord(parkingRecord);
    }

    /**
     * 修改停车记录
     *
     * @param parkingRecord 停车记录
     * @return 结果
     */
    @Override
    public int updateParkingRecord(ParkingRecord parkingRecord) {
        return parkingRecordMapper.updateParkingRecord(parkingRecord);
    }

    /**
     * 批量删除停车记录
     *
     * @param ids 需要删除的停车记录主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteParkingRecordByIds(Long[] ids) {
        for (Long id : ids) {
            ParkingRecord parkingRecord = parkingRecordMapper.selectParkingRecordById(id);
            if (!parkingRecord.getPaystate().equals("1")){
                ParkingLotInformation parkingLotInformation = parkingLotInformationService.selectParkingLotInformationById(parkingRecord.getParkinglotinformationid());
                parkingLotInformation.setRemainingParkingSpace(parkingLotInformation.getRemainingParkingSpace()+1);
                parkingLotInformationService.updateParkingLotInformation(parkingLotInformation);
            }
        }
        return parkingRecordMapper.deleteParkingRecordByIds(ids);
    }

    /**
     * 删除停车记录信息
     *
     * @param id 停车记录主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteParkingRecordById(Long id) {
        ParkingRecord parkingRecord = parkingRecordMapper.selectParkingRecordById(id);
        if (!parkingRecord.getPaystate().equals("1")){
            ParkingLotInformation parkingLotInformation = parkingLotInformationService.selectParkingLotInformationById(parkingRecord.getParkinglotinformationid());
            parkingLotInformation.setRemainingParkingSpace(parkingLotInformation.getRemainingParkingSpace()+1);
            parkingLotInformationService.updateParkingLotInformation(parkingLotInformation);
        }
        return parkingRecordMapper.deleteParkingRecordById(id);
    }

    //通过车牌号查询有无未支付订单
    @Override
    public ParkingRecord findByLicense(String license, Long ParkingLotInformationId) {
        return parkingRecordMapper.findByLicense(license, ParkingLotInformationId);
    }

    //通过车牌号，未支付状态查询出来修改订单支付单状态  有牌无牌车公用开闸接口  支付方式
    // TODO: 2023/1/30 后期修改需要判断使用的支付渠道 
    @Override
    @Transactional
    public AjaxResult editPayState(ParkingRecordVo parkingRecordVo) {
        log.info(parkingRecordVo.getPaymentmethod());
        if (parkingRecordVo.getFlag().equals("0")){
            return AjaxResult.success();
        }else {
            OrderResDto orderResDto = queryPay(parkingRecordVo);
            if (orderResDto.getRes()) {
                ParkingLotEquipment parkingLotEquipment = parkingLotEquipmentService.selectParkingLotEquipmentById(parkingRecordVo.getParkinglotequipmentid());
                ParkingRecord parkingRecord = parkingRecordMapper.findByLicense(parkingRecordVo.getLicense(), parkingRecordVo.getParkinglotinformationid());
                if (parkingRecord==null){
                        parkingRecord=parkingRecordMapper.findByOpenid(parkingRecordVo.getOpenid(),parkingRecordVo.getParkinglotinformationid());
                }
                ParkingLotInformation parkingLotInformation = parkingLotInformationService.selectParkingLotInformationById(parkingRecordVo.getParkinglotinformationid());
                if (parkingRecord != null) {
                    parkingRecord.setWxorder(orderResDto.getMsg());
                    parkingRecord.setPaystate("1");
                    parkingRecord.setOrderstate("1");
                    parkingRecord.setParkingeqid(parkingLotEquipment.getId());
                    parkingRecord.setIndoorPayment("0");
                    parkingRecord.setPayTime(new Date());
                    parkingRecord.setPaymentmethod(parkingRecordVo.getPaymentmethod());
                    parkingRecord.setOrdernumber(parkingRecordVo.getOrdernumber());
                    //  2022/12/15  修改优惠卷状态 一个人只有一种优惠
                    ParkingCouponrecord byParkingLotInformationIdAndLicense = parkingCouponrecordMapper.findByParkingLotInformationIdAndLicenseTo(parkingRecordVo.getParkinglotinformationid(), parkingRecordVo.getLicense());
                    if (byParkingLotInformationIdAndLicense != null) {
                        byParkingLotInformationIdAndLicense.setState("1");
                        byParkingLotInformationIdAndLicense.setOrdernumber(parkingRecord.getOrdernumber());
                        parkingCouponrecordMapper.updateParkingCouponrecord(byParkingLotInformationIdAndLicense);
                    }
                    //开闸处理
                    extracted(parkingRecordVo.getLicense(), parkingLotEquipment);
                    updateParkingRecord(parkingRecord);
                    List<ParkingRecord> list = new ArrayList<>();
                    list.add(parkingRecord);
                    send(parkingRecordVo.getParkinglotequipmentid(), list);
                    updateRemainingParkingSpace(parkingLotInformation);
                    log.info(parkingRecordVo.getLicense()+"无超时补费信息"+parkingLotInformation.getName()+"门口处缴费回调开闸");
                    redisTemplate.delete(String.valueOf(parkingLotEquipment.getId()));
                    return AjaxResult.success();
                } else {
                    //超时补费
                    ParkingRecord findbypaystateandlicense = findbypaystateandlicense(parkingRecordVo.getParkinglotinformationid(), parkingRecordVo.getLicense());
                    if (findbypaystateandlicense != null) {
                        //从redis获取需要补费金额
                        Long money = (Long) redisTemplate.opsForValue().get(parkingRecordVo.getParkinglotequipmentid() + "Overtimefee");
                        if (money == null) {
                            log.info(parkingRecordVo.getLicense()+"在"+parkingLotInformation.getName()+"redis无补费金额信息");
                            return AjaxResult.error("redis无补费金额信息");
                        }
                        log.info(parkingRecordVo.getLicense()+"在"+parkingLotInformation.getName()+"redis有补费金额信息开闸");
                        //改变状态
                        updateState(parkingRecordVo.getLicense(), parkingLotEquipment, findbypaystateandlicense, money);
                        List<ParkingRecord> list = new ArrayList<>();
                        list.add(findbypaystateandlicense);
                        send(parkingRecordVo.getParkinglotequipmentid(), list);
                        updateRemainingParkingSpace(parkingLotInformation);
                        redisTemplate.delete(parkingRecordVo.getParkinglotequipmentid() + "Overtimefee");
                        return AjaxResult.success();
                    }
                }
            }
            return AjaxResult.error("【订单号" + parkingRecordVo.getOrdernumber() + "】" + "支付异常");
        }
    }

    @Override
    @Transactional
    public AjaxResult editPayStateOne(ParkingRecordVo parkingRecordVo) {
        ParkingLotEquipment parkingLotEquipment = parkingLotEquipmentService.selectParkingLotEquipmentById(parkingRecordVo.getParkinglotequipmentid());
        ParkingRecord parkingRecord = parkingRecordMapper.findByLicense(parkingRecordVo.getLicense(), parkingRecordVo.getParkinglotinformationid());
        if (parkingRecord==null){
            parkingRecord=parkingRecordMapper.findByOpenid(parkingRecordVo.getOpenid(),parkingRecordVo.getParkinglotinformationid());
        }
        ParkingLotInformation parkingLotInformation = parkingLotInformationService.selectParkingLotInformationById(parkingRecordVo.getParkinglotinformationid());
        if (parkingRecord != null) {
            parkingRecord.setPaystate("1");
            parkingRecord.setOrderstate("1");
            parkingRecord.setIndoorPayment("0");
            parkingRecord.setPayTime(new Date());
            parkingRecord.setParkingeqid(parkingLotEquipment.getId());
            parkingRecord.setPaymentmethod("免费");
            parkingRecord.setOrdernumber(parkingRecordVo.getOrdernumber());
            //  2022/12/15  修改优惠卷状态 一个人只有一种优惠
            ParkingCouponrecord byParkingLotInformationIdAndLicense = parkingCouponrecordMapper.findByParkingLotInformationIdAndLicenseTo(parkingRecordVo.getParkinglotinformationid(), parkingRecordVo.getLicense());
            if (byParkingLotInformationIdAndLicense != null) {
                byParkingLotInformationIdAndLicense.setState("1");
                byParkingLotInformationIdAndLicense.setOrdernumber(parkingRecord.getOrdernumber());
                parkingCouponrecordMapper.updateParkingCouponrecord(byParkingLotInformationIdAndLicense);
            }
            //开闸处理
            extracted(parkingRecordVo.getLicense(), parkingLotEquipment);
            updateParkingRecord(parkingRecord);
            List<ParkingRecord> list = new ArrayList<>();
            list.add(parkingRecord);
            send(parkingRecordVo.getParkinglotequipmentid(), list);
            updateRemainingParkingSpace(parkingLotInformation);
            log.info(parkingRecordVo.getLicense() + "无牌车" + parkingLotInformation.getName() + "门口处缴费金额为0回调开闸");
            redisTemplate.delete(String.valueOf(parkingLotEquipment.getId()));
            return AjaxResult.success();
        }
        return AjaxResult.error();
    }

    @Override
    public Long selectParkingRecordList1(ParkingRecord parkingRecord) {
        return parkingRecordMapper.selectParkingRecordList1(parkingRecord);
    }

    @Override
    public List<ParkingRecord> selectParkingRecordListOne(Long parkinglotinformationid) {
        return parkingRecordMapper.selectParkingRecordListOne(parkinglotinformationid);
    }

    @Override
    public List<ParkingRecord> getPayRecord1(ParkingRecord parkingRecord) {
        if (parkingRecord.getParkinglotinformationid()!=0){
            List<ParkingRecord> payRecord = parkingRecordMapper.getPayRecord1(parkingRecord.getParkinglotinformationid(),parkingRecord.getParkingeqid());
            if (payRecord.size()!=0){
                ParkingRecord parkingRecord1 = payRecord.get(0);
                List<ParkingRecord> list = new ArrayList<>();
                list.add(parkingRecord1);
                return list;
            }
        }
        //等于o代表是超级管理员 不要求查看
        return new ArrayList<ParkingRecord>();
    }


    //修改状态并开闸
    private void updateState(String license, ParkingLotEquipment parkingLotEquipment, ParkingRecord findbypaystateandlicense, Long money) {
        findbypaystateandlicense.setPayTime(new Date());
        findbypaystateandlicense.setPaystate("1");
        findbypaystateandlicense.setOrderstate("1");
        findbypaystateandlicense.setIndoorPayment("0");
        findbypaystateandlicense.setParkingeqid(parkingLotEquipment.getId());
        findbypaystateandlicense.setMoney(findbypaystateandlicense.getMoney()+ money);
        findbypaystateandlicense.setSupplementaryfee(money);
        findbypaystateandlicense.setAmountpayable(findbypaystateandlicense.getAmountpayable()+ money);
        //开闸处理
        String s = SerialPortUtils.payAfter(license);
        updateParkingRecord(findbypaystateandlicense);
        //等待心跳那调用
       redisTemplate.opsForValue().set(parkingLotEquipment.getCameraserialnumber(),s,30,TimeUnit.SECONDS);
    }
    //websocket发送
    private void send(Long parkinglotequipmentid, List<ParkingRecord> list) {
        String s = JSON.toJSONString(list);
        //WebSocketService发送给前端消息

            webSocketService.sendMessage(String.valueOf(parkinglotequipmentid), s);

    }
    //开闸
    private void extracted(String license, ParkingLotEquipment parkingLotEquipment) {

        String s = SerialPortUtils.payAfter(license);
        redisTemplate.opsForValue().set(parkingLotEquipment.getCameraserialnumber(),s,30,TimeUnit.SECONDS);

    }
    //无牌车
    @Override
    public AjaxResult echoInformationToLicense(Long parkinglotequipmentid, String openid,String license1) {
        Date date = new Date();
        ParkingLotEquipment parkingLotEquipment = parkingLotEquipmentService.selectParkingLotEquipmentById(parkinglotequipmentid);

        ParkingRecord parkingRecord=null;
            //通过车牌查看停车记录
            if(null== license1){
                 parkingRecord = parkingRecordMapper.findByOpenid(openid, parkingLotEquipment.getParkinglotinformationid());
                if (null==parkingRecord){
                    return AjaxResult.error("无停车记录");
                }
            }
        if(null!= license1) {
                parkingRecord = parkingRecordMapper.findByLicense(license1, parkingLotEquipment.getParkinglotinformationid());
                if (null==parkingRecord){
                    return AjaxResult.error("无停车记录");
                }
            }

        ParkingLotInformation parkingLotInformation = parkingLotInformationService.selectParkingLotInformationById(parkingLotEquipment.getParkinglotinformationid());
        parkingRecord.setOrderstate("2");
        parkingRecord.setExittime(date);
        String license = parkingRecord.getLicense();
        // 需要判断是否是充值车
        ParkingChargingDto parkingChargingDto =null;
        ParkingRecharge parkingRecharge=parkingRechargeService.findBy(parkingLotInformation.getId(),license);
        //null 则不是
        if (parkingRecharge==null){
            parkingChargingDto = new ParkingChargingDto(parkingLotInformation.getId(),parkingRecord.getAdmissiontime(),new Date(),license,0L);
        }else {
            parkingChargingDto = new ParkingChargingDto(parkingLotInformation.getId(),parkingRecord.getAdmissiontime(),new Date(),license,1L);
        }

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
       BeanUtils.copyProperties(parkingLotInformation,parkingRecordVo);

        return AjaxResult.success(parkingRecordVo);
    }


    @Override
    public ParkingRecord selectParkingRecordById1(Long id) {
        ParkingRecord parkingRecord = parkingRecordMapper.selectParkingRecordById(id);
        if (!parkingRecord.getPaystate().equals("1")){
            ParkingChargingDto parkingChargingDto =null;
            ParkingRecharge parkingRecharge=parkingRechargeService.findBy(parkingRecord.getParkinglotinformationid(),parkingRecord.getLicense());
            //null 则不是
            if (parkingRecharge==null){
                parkingChargingDto = new ParkingChargingDto(parkingRecord.getParkinglotinformationid(),parkingRecord.getAdmissiontime(),new Date(),parkingRecord.getLicense(),0L);
            }else {
                parkingChargingDto = new ParkingChargingDto(parkingRecord.getParkinglotinformationid(),parkingRecord.getAdmissiontime(),new Date(),parkingRecord.getLicense(),1L);
            }
            //金额
            MoneyVo moneyVo = parkingChargingService.calculatedAmount(parkingChargingDto);
            parkingRecord.setMoney(moneyVo.getMoney());
        }
        return parkingRecord;
    }

    //室内扫码回显支付信息
    @Override
    public AjaxResult indoor(Long parkingLotInformationId, String license,String openid) {
        String replace = license.replace(" ", "");
        if (replace.length()<7){
            return AjaxResult.error("请检查车牌");
        }
        String trim = license.trim();
        //  2022/12/15 判断是否白名单  固定车位  是否拥有次卷   如果是提示用户可以直接出场
        ParkingCouponrecord parkingCouponrecord=parkingCouponrecordService.findByParkingLotInformationIdAndLicense(parkingLotInformationId,trim);
        ParkingWhiteList byLicense = parkingWhiteListMapper.findByLicense(trim,parkingLotInformationId);
        ParkingFixedparkingspace parkingFixedparkingspace=parkingFixedparkingspaceService.findByParkingLotInformationIdAndLicense(parkingLotInformationId,license);
        if (byLicense!=null){
            log.info("室内扫码支付您是白名单车主,请直接离场"+trim);
            return AjaxResult.error("您是白名单车主");
        }
        if (parkingFixedparkingspace!=null){
            log.info("室内扫码支付您是固定车位车主,请直接离场"+trim);
            return AjaxResult.error("您是固定车位车主");
        }
        if (parkingCouponrecord!=null){
            log.info("室内扫码支付拥有次数优惠卷,请直接离场"+trim);
            return AjaxResult.error("拥有次数优惠卷，请直接离场");
        }
        ParkingRecord parkingRecord = parkingRecordMapper.findByLicense(trim, parkingLotInformationId);
        if (parkingRecord==null){
            return AjaxResult.error("无停车记录");
        }

        ParkingLotInformation parkingLotInformation = parkingLotInformationService.selectParkingLotInformationById(parkingLotInformationId);
        ParkingRecordVo parkingRecordVo = new ParkingRecordVo();
        parkingRecord.setOpenid(openid);
        updateParkingRecord(parkingRecord);
        BeanUtils.copyProperties(parkingRecord,parkingRecordVo);


        parkingRecordVo.setParkingLotInformationName(parkingLotInformation.getName());
        Date date = new Date();
        parkingRecordVo.setExittime(date);
        //  需要判断是否是充值车
        ParkingChargingDto parkingChargingDto =null;
        ParkingRecharge parkingRecharge=parkingRechargeService.findBy(parkingLotInformation.getId(),license);
        //null 则不是
        if (parkingRecharge==null){
             parkingChargingDto = new ParkingChargingDto(parkingLotInformationId,parkingRecord.getAdmissiontime(),date,license,0L);
        }else {
            log.info(parkingRecordVo.getLicense()+"充值车无需场内支付");
           return AjaxResult.error("充值车无需场内支付");
        }
        // 调用计费规则显示总费用 优惠金额  实际支付金额  优惠方式(支付方式后面加优惠卷)  计算方式时当前时间计算  回调接口传过来所有信息 保存

        //金额
        MoneyVo moneyVo = parkingChargingService.calculatedAmount(parkingChargingDto);
        if (moneyVo.getMoney()==0){
            AjaxResult.error("无需缴费");
        }
        //赋值金额
        BeanUtils.copyProperties(moneyVo,parkingRecordVo);
        //赋值支付商户号和启用状态
        BeanUtils.copyProperties(parkingLotInformation,parkingRecordVo);

        return AjaxResult.success(parkingRecordVo);
    }

    //室内扫码回调
    @Override
    public AjaxResult indoorCallback(ParkingRecordVo parkingRecordVo) {
        if (parkingRecordVo.getFlag().equals("0")){
            return AjaxResult.success();
        }else {
            OrderResDto orderResDto = queryPay(parkingRecordVo);
            if (orderResDto.getRes()) {
                ParkingRecord parkingRecord = parkingRecordMapper.findByLicense(parkingRecordVo.getLicense(), parkingRecordVo.getParkinglotinformationid());
                BeanUtils.copyProperties(parkingRecordVo, parkingRecord);
                // parkingRecord.setPaystate("1");
                parkingRecord.setOrderstate("1");
                parkingRecord.setIndoorPayment("1");
                parkingRecord.setWxorder(orderResDto.getMsg());
                parkingRecord.setPayTime(new Date());
                updateParkingRecord(parkingRecord);
                ParkingCouponrecord byParkingLotInformationIdAndLicense = parkingCouponrecordMapper.findByParkingLotInformationIdAndLicenseTo(parkingRecordVo.getParkinglotinformationid(), parkingRecordVo.getLicense());
                if (byParkingLotInformationIdAndLicense != null) {
                    byParkingLotInformationIdAndLicense.setState("1");
                    parkingCouponrecordMapper.updateParkingCouponrecord(byParkingLotInformationIdAndLicense);
                }
                log.info(parkingRecordVo.getLicense()+"请五分钟内离场");
                return AjaxResult.success("请五分钟内离场");
            } else {
                return AjaxResult.error("【订单号" + parkingRecordVo.getOrdernumber() + "】" + "支付异常");
            }
        }
    }
    //查询订单是否支付成功
    private OrderResDto queryPay(ParkingRecordVo parkingRecordVo) {
        PayState payState = new PayState();
        payState.setPayType("3");
        payState.setTradeCode("1005");
        InPayData inPayData = new InPayData();
        inPayData.setOrder_out_no(parkingRecordVo.getOrdernumber());
        inPayData.setSub_mchid(parkingRecordVo.getRuralcreditpaymentId());
        payState.setInPayData(inPayData);
        int time=0;
        while (true) {
            try {
            String s = HttpRequest.doPostTestTwo(payState);
                System.out.println(s);
            if (s!=null){
                JSONObject jsonObject = JSONObject.parseObject(s);
                String orderStat = (String) jsonObject.get("orderStat");
                if (orderStat!=null&&orderStat!="") {
                    if (orderStat.equals("02")) {
                        log.info("【订单号" + parkingRecordVo.getOrdernumber() + "】" + "订单支付成功");
                        String msg =(String) jsonObject.get("tpamTxnSsn");
                        return new OrderResDto(true,msg);
                    }
                }
            }
                Thread.sleep(3000);
                time++;
                if (time == 60) {
                log.info("【订单号" + parkingRecordVo.getOrdernumber() + "】"+"订单未支付成功");
                    return new OrderResDto(false,null);
                }
                log.info("正在查询订单状态【订单号" + parkingRecordVo.getOrdernumber() + "】" + time);

            } catch (InterruptedException e) {
                log.info("场内修改状态接口错误"+e);
                e.printStackTrace();
            }
        }
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
        //停车位加一
        ParkingLotInformation parkingLotInformation = parkingLotInformationService.selectParkingLotInformationById(parkingRecord.getParkinglotinformationid());
        parkingLotInformation.setRemainingParkingSpace(parkingLotInformation.getRemainingParkingSpace()+1);
        parkingLotInformationService.updateParkingLotInformation(parkingLotInformation);

        parkingRecord.setPaymentmethod("现金支付");
        parkingRecord.setOrderstate("1");
        parkingRecord.setPaystate("1");

        parkingRecord.setPayTime(new Date());
        parkingRecord.setFlee(0L);
        parkingRecord.setParkingeqid(parkingRecord.getParkinglotequipmentid());
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
        ParkingLotEquipment parkingLotEquipment = parkingLotEquipmentService.selectParkingLotEquipmentById(parkingRecord.getParkinglotequipmentid());
        Date admissiontime = parkingRecord.getAdmissiontime();
        Date exittime = parkingRecord.getExittime();
        long l1 = (exittime.getTime()-admissiontime.getTime()) / (1000*60);
        String data1 = SerialPortUtils.Exit(parkingRecord.getLicense(),l1);
        //心跳开闸指令
        redisTemplate.opsForValue().set(parkingLotEquipment.getCameraserialnumber(),data1,30,TimeUnit.SECONDS);

        redisTemplate.opsForValue().set(parkingRecord.getParkinglotinformationid()+parkingRecord.getLicense(),parkingRecord.getLicense(),5, TimeUnit.MICROSECONDS);
        return AjaxResult.success();
    }

    @Override
    @Transactional
    public AjaxResult updateToRecordFromCoupon(Long id) {
        ParkingRecord parkingRecord = parkingRecordMapper.selectParkingRecordById(id);
        //停车位加一
        ParkingLotInformation parkingLotInformation = parkingLotInformationService.selectParkingLotInformationById(parkingRecord.getParkinglotinformationid());
        parkingLotInformation.setRemainingParkingSpace(parkingLotInformation.getRemainingParkingSpace()+1);
        parkingLotInformationService.updateParkingLotInformation(parkingLotInformation);

        parkingRecord.setPaymentmethod("优惠卷代扣");
        parkingRecord.setOrderstate("1");
        parkingRecord.setPaystate("1");
        parkingRecord.setFlee(0L);
        parkingRecord.setParkingeqid(parkingRecord.getParkinglotequipmentid());
        parkingRecord.setPayTime(new Date());
        ParkingCouponrecord byParkingLotInformationIdAndLicense = parkingCouponrecordMapper.findByParkingLotInformationIdAndLicenseTo(parkingRecord.getParkinglotinformationid(), parkingRecord.getLicense());
        if (byParkingLotInformationIdAndLicense!=null){
            byParkingLotInformationIdAndLicense.setState("1");
            parkingCouponrecordMapper.updateParkingCouponrecord(byParkingLotInformationIdAndLicense);
        }
        parkingRecordMapper.updateParkingRecord(parkingRecord);

        ParkingLotEquipment parkingLotEquipment = parkingLotEquipmentService.selectParkingLotEquipmentById(parkingRecord.getParkinglotequipmentid());
        Date admissiontime = parkingRecord.getAdmissiontime();
        Date exittime = parkingRecord.getExittime();
        long l1 = (exittime.getTime()-admissiontime.getTime()) / (1000*60);
        String data1 = SerialPortUtils.Exit(parkingRecord.getLicense(),l1);
        //心跳开闸指令
        redisTemplate.opsForValue().set(parkingLotEquipment.getCameraserialnumber(),data1,30,TimeUnit.SECONDS);
        //停车场id加车牌
        redisTemplate.opsForValue().set(parkingRecord.getParkinglotinformationid()+parkingRecord.getLicense(),parkingRecord.getLicense(),5, TimeUnit.MICROSECONDS);
        return AjaxResult.success();
    }
    @Override
    public AjaxResult updateToRecordFromCouponOne(Long id) {
        ParkingRecord parkingRecord = parkingRecordMapper.selectParkingRecordById(id);
        //停车位加一
        ParkingLotInformation parkingLotInformation = parkingLotInformationService.selectParkingLotInformationById(parkingRecord.getParkinglotinformationid());
        parkingLotInformation.setRemainingParkingSpace(parkingLotInformation.getRemainingParkingSpace()+1);
        parkingLotInformationService.updateParkingLotInformation(parkingLotInformation);

        parkingRecord.setPaymentmethod("优惠卷代扣");
        parkingRecord.setOrderstate("1");
        parkingRecord.setPaystate("1");
        parkingRecord.setFlee(0L);
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



    @Override
    public AjaxResult updateToRecordOne(Long id) {
        ParkingRecord parkingRecord = parkingRecordMapper.selectParkingRecordById(id);
        //停车位加一
        ParkingLotInformation parkingLotInformation = parkingLotInformationService.selectParkingLotInformationById(parkingRecord.getParkinglotinformationid());
        parkingLotInformation.setRemainingParkingSpace(parkingLotInformation.getRemainingParkingSpace()+1);
        parkingLotInformationService.updateParkingLotInformation(parkingLotInformation);

        parkingRecord.setPaymentmethod("现金支付");
        parkingRecord.setOrderstate("1");
        parkingRecord.setPaystate("1");
        parkingRecord.setPayTime(new Date());
        parkingRecord.setFlee(0L);
        parkingRecordMapper.updateParkingRecord(parkingRecord);
        ParkingCouponrecord byParkingLotInformationIdAndLicense = parkingCouponrecordMapper.findByParkingLotInformationIdAndLicenseTo(parkingRecord.getParkinglotinformationid(), parkingRecord.getLicense());
        if (byParkingLotInformationIdAndLicense!=null){
            byParkingLotInformationIdAndLicense.setState("1");
            parkingCouponrecordMapper.updateParkingCouponrecord(byParkingLotInformationIdAndLicense);
        }
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
        if (parkingLotInformation.getRemainingParkingSpace()==0){
            return AjaxResult.error("停车位不足");
        }
        //通过停车场id，车牌号查询有无未支付订单
        ParkingRecord parkingRecord= parkingRecordMapper.findByLicense(license,parkingLotInformation.getId());
        if (parkingRecord!=null){
            //如果有就删除掉
            parkingRecordMapper.deleteParkingRecordById(parkingRecord.getId());
            String wupaiche = SerialPortUtils.wupaiche(license);
            //发送指令等待心跳取值
            redisTemplate.opsForValue().set(parkingLotEquipment.getCameraserialnumber(),wupaiche,30,TimeUnit.SECONDS);

            //停车场车位数
            updateRemainingParkingSpace2(parkingLotInformation);
            //添加无牌车进场记录
            addParkingRecord(license, parkingLotEquipment,openid);

            return AjaxResult.success();
        }else {
            String wupaiche = SerialPortUtils.wupaiche(license);
            //发送指令等待心跳取值
            redisTemplate.opsForValue().set(parkingLotEquipment.getCameraserialnumber(),wupaiche,30,TimeUnit.SECONDS);

            //停车场车位数加一
            updateRemainingParkingSpace1(parkingLotInformation);
            //添加无牌车进场记录
            addParkingRecord(license, parkingLotEquipment,openid);

            return AjaxResult.success();
        }

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
            dailyInformationVo.getName().add("今日进场数");
            Long count=parkingRecordMapper.getDailyInformation(id);
            dailyInformationVo.getParkingLots().add(new ParkingLots(count,"今日进场数"));
            dailyInformationVo.getName().add("今日出场数");
            Long count1=parkingRecordMapper.getDailyInformations(id);
            dailyInformationVo.getParkingLots().add(new ParkingLots(count1,"今日出场数"));
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
    //无牌车停车场车位数减一
    private void updateRemainingParkingSpace1(ParkingLotInformation parkingLotInformation) {
        Long remainingParkingSpace = parkingLotInformation.getRemainingParkingSpace();
        parkingLotInformation.setRemainingParkingSpace(remainingParkingSpace-1);
        parkingLotInformationService.updateParkingLotInformation(parkingLotInformation);

    }
    //无牌车停车场车位数减一
    private void updateRemainingParkingSpace2(ParkingLotInformation parkingLotInformation) {
        Long remainingParkingSpace = parkingLotInformation.getRemainingParkingSpace();
        parkingLotInformation.setRemainingParkingSpace(remainingParkingSpace);
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

    @Override
    public AjaxResult getByOpend(String openid,Long  parkingLotInformationId) {

        List<ParkingRecord> byOpend = parkingRecordMapper.getByOpend(openid, parkingLotInformationId);
        return AjaxResult.success(byOpend);
    }
//查询室内支付记录
    @Override
    public ParkingRecord todo(Long parkinglotinformationid, String license) {
        return parkingRecordMapper.todo( parkinglotinformationid,  license);
    }

    @Override
    public List<ParkingRecord> findByFlee() {
        return parkingRecordMapper.findByFlee();
    }

    @Override
    public ParkingRecord findByLicenseAndIndoorPayment(Long parkinglotinformationid ,String license1) {
        return parkingRecordMapper.findByLicenseAndIndoorPayment(parkinglotinformationid,license1);
    }

    @Override
    @Transactional
    public AjaxResult refreshstate(Long id) {
        ParkingRecord parkingRecord = new ParkingRecord();
        parkingRecord.setParkinglotinformationid(id);
        parkingRecord.setOrderstate("2");
        List<ParkingRecord> list = selectParkingRecordList(parkingRecord);
        if (list.size()!=0){
            for (ParkingRecord parkingRecord1 : list) {
                parkingRecord1.setPaystate("1");
                parkingRecord1.setFlee(1L);
                parkingRecord1.setOrderstate("1");
                updateParkingRecord(parkingRecord1);
            }
            ParkingLotInformation parkingLotInformation = parkingLotInformationService.selectParkingLotInformationById(id);
            parkingLotInformation.setRemainingParkingSpace( parkingLotInformation.getRemainingParkingSpace()+list.size());
            parkingLotInformationService.updateParkingLotInformation(parkingLotInformation);
        return AjaxResult.success("刷新成功");
        }else {
            return AjaxResult.error("无记录");
        }

    }



}
