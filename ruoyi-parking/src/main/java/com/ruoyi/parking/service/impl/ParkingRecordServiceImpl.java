package com.ruoyi.parking.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.ruoyi.parking.mapper.ParkingRecordMapper;
import com.ruoyi.parking.domain.ParkingRecord;
import com.ruoyi.parking.service.IParkingRecordService;

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
    public List<ParkingRecord> selectParkingRecordList(ParkingRecord parkingRecord)
    {
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

    //通过车牌号，未支付状态查询出来修改订单支付单状态，金额赋值，支付时间赋值
    @Override
    public void editPayState(Long parkingLotInformationId,String license,Long money) {
        ParkingRecord parkingRecord = parkingRecordMapper.findByLicense(license, parkingLotInformationId);
        if (parkingRecord!=null){
            parkingRecord.setPaystate("1");
            parkingRecord.setOrderstate("1");
            parkingRecord.setMoney(money);
            parkingRecord.setPayTime(new Date());
            updateParkingRecord(parkingRecord);
            redisTemplate.opsForValue().set(license,license,5, TimeUnit.MICROSECONDS);
        }
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


}
