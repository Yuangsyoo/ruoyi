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
import com.ruoyi.parking.mapper.ParkingBlackListMapper;
import com.ruoyi.parking.mapper.ParkingCouponrecordMapper;
import com.ruoyi.parking.mapper.ParkingWhiteListMapper;
import com.ruoyi.parking.service.*;
import com.ruoyi.parking.vo.ParkingRecordVo;
import com.ruoyi.system.mapper.SysUserMapper;
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
            ParkingLotEquipment parkingLotEquipment = parkingLotEquipmentService.selectParkingLotEquipmentById(parkinglotequipmentid);
            //开闸处理
            LPRDemo lprDemo = new LPRDemo();
            //初始化返回句柄
            int handle = lprDemo.InitClient(parkingLotEquipment.getIpadress());
            //开闸
            int i1 = lprDemo.switchOn(handle, 0, 500);
            //关闭设备的控制句柄
            lprDemo.VzLPRClient_Close(handle);
            //执行结束释放
            lprDemo.VzLPRClient_Cleanup();
            updateParkingRecord(parkingRecord);
            List<ParkingRecord> list = new ArrayList<>();
            list.add(parkingRecord);
            String s = JSON.toJSONString(list);
            //WebSocketService发送给前端消息
            List<SysUser> list1 = sysUserMapper.findUserList(parkinglotequipmentid);
            for (SysUser user : list1) {
                webSocketService.sendMessage(user.getUserName(),s);
            }
            updateRemainingParkingSpace(parkingLotInformation);
        }
    }
    //无牌车
    @Override
    public AjaxResult echoInformationToLicense(Long parkinglotequipmentid, String license) {
        Date date = new Date();
        ParkingLotEquipment parkingLotEquipment = parkingLotEquipmentService.selectParkingLotEquipmentById(parkinglotequipmentid);
        ParkingRecord parkingRecord = parkingRecordMapper.findByLicense(license, parkingLotEquipment.getParkinglotinformationid());
        if (parkingRecord==null){
            return AjaxResult.error("无停车记录");
        }
        ParkingLotInformation parkingLotInformation = parkingLotInformationService.selectParkingLotInformationById(parkingLotEquipment.getParkinglotinformationid());
        parkingRecord.setOrderstate("2");
        parkingRecord.setExittime(date);
            // TODO: 2022/12/11 调用计费规则显示总费用 优惠金额  实际支付金额  优惠方式
        parkingRecord.setEntranceandexitname(parkingRecord.getEntranceandexitname()+","+parkingLotEquipment.getName());
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
        // TODO: 2022/12/11 调用计费规则显示总费用 优惠金额  实际支付金额  优惠方式(支付方式后面加优惠卷)  计算方式时当前时间计算  回调接口传过来所有信息 保存
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
    public ParkingRecord findByParkingLotInformationLicense(Long id, String license) {
      List<ParkingRecord>list = parkingRecordMapper.findByParkingLotInformationLicense(id, license);
        ParkingRecord parkingRecord = list.get(0);
        return parkingRecord;
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
    public AjaxResult updateToRecord(Long id) {
        ParkingRecord parkingRecord = parkingRecordMapper.selectParkingRecordById(id);
        parkingRecord.setPaymentmethod("现金支付");
        parkingRecord.setOrderstate("1");
        parkingRecord.setPaystate("1");
        parkingRecord.setPayTime(new Date());
        parkingRecordMapper.updateParkingRecord(parkingRecord);
        //停车场id加车牌
        redisTemplate.opsForValue().set(parkingRecord.getParkinglotinformationid()+parkingRecord.getLicense(),parkingRecord.getLicense(),5, TimeUnit.MICROSECONDS);
        return AjaxResult.success();
    }

    @Override
    public AjaxResult updateToRecordFromCoupon(Long id) {
        ParkingRecord parkingRecord = parkingRecordMapper.selectParkingRecordById(id);
        parkingRecord.setPaymentmethod("优惠卷代扣");
        parkingRecord.setOrderstate("1");
        parkingRecord.setPaystate("1");
        parkingRecord.setPayTime(new Date());
        parkingRecordMapper.updateParkingRecord(parkingRecord);
        //停车场id加车牌
        redisTemplate.opsForValue().set(parkingRecord.getParkinglotinformationid()+parkingRecord.getLicense(),parkingRecord.getLicense(),5, TimeUnit.MICROSECONDS);
        return AjaxResult.success();
    }
    //添加无牌车接口
    @Override
    public AjaxResult noLicensePlate(Long parkinglotequipmentid, String license) {
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

        //停车场车位数加一
        updateRemainingParkingSpace(parkingLotInformation);
        //添加无牌车进场记录
        addParkingRecord(license, parkingLotEquipment);
        //关闭设备的控制句柄
        lprDemo.VzLPRClient_Close(handle);
        //执行结束释放
        lprDemo.VzLPRClient_Cleanup();
        return AjaxResult.success();
    }
    //停车场车位数加一
    private void updateRemainingParkingSpace(ParkingLotInformation parkingLotInformation) {
        Long remainingParkingSpace = parkingLotInformation.getRemainingParkingSpace();
        parkingLotInformation.setRemainingParkingSpace(remainingParkingSpace+1);
        parkingLotInformationService.updateParkingLotInformation(parkingLotInformation);
    }


    //添加无牌车进场记录
    private void addParkingRecord(String license, ParkingLotEquipment parkingLotEquipment) {
        ParkingRecord parkingRecord = new ParkingRecord();
        parkingRecord.setLicense(license);
        parkingRecord.setParkinglotinformationid(parkingLotEquipment.getParkinglotinformationid());
        parkingRecord.setAdmissiontime(new Date());
        parkingRecord.setLicensepllatecolor("0");
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
