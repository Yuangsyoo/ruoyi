package com.ruoyi.parking.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.parking.mapper.ParkingRechargeRecordMapper;
import com.ruoyi.parking.domain.ParkingRechargeRecord;
import com.ruoyi.parking.service.IParkingRechargeRecordService;

/**
 * 充值车操作记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-02-01
 */
@Service
public class ParkingRechargeRecordServiceImpl implements IParkingRechargeRecordService 
{
    @Autowired
    private ParkingRechargeRecordMapper parkingRechargeRecordMapper;

    /**
     * 查询充值车操作记录
     * 
     * @param id 充值车操作记录主键
     * @return 充值车操作记录
     */
    @Override
    public ParkingRechargeRecord selectParkingRechargeRecordById(Long id)
    {
        return parkingRechargeRecordMapper.selectParkingRechargeRecordById(id);
    }

    /**
     * 查询充值车操作记录列表
     * 
     * @param parkingRechargeRecord 充值车操作记录
     * @return 充值车操作记录
     */
    @Override
    public List<ParkingRechargeRecord> selectParkingRechargeRecordList(ParkingRechargeRecord parkingRechargeRecord)
    {
        return parkingRechargeRecordMapper.selectParkingRechargeRecordList(parkingRechargeRecord);
    }

    /**
     * 新增充值车操作记录
     * 
     * @param parkingRechargeRecord 充值车操作记录
     * @return 结果
     */
    @Override
    public int insertParkingRechargeRecord(ParkingRechargeRecord parkingRechargeRecord)
    {
        return parkingRechargeRecordMapper.insertParkingRechargeRecord(parkingRechargeRecord);
    }

    /**
     * 修改充值车操作记录
     * 
     * @param parkingRechargeRecord 充值车操作记录
     * @return 结果
     */
    @Override
    public int updateParkingRechargeRecord(ParkingRechargeRecord parkingRechargeRecord)
    {
        return parkingRechargeRecordMapper.updateParkingRechargeRecord(parkingRechargeRecord);
    }

    /**
     * 批量删除充值车操作记录
     * 
     * @param ids 需要删除的充值车操作记录主键
     * @return 结果
     */
    @Override
    public int deleteParkingRechargeRecordByIds(Long[] ids)
    {
        return parkingRechargeRecordMapper.deleteParkingRechargeRecordByIds(ids);
    }

    /**
     * 删除充值车操作记录信息
     * 
     * @param id 充值车操作记录主键
     * @return 结果
     */
    @Override
    public int deleteParkingRechargeRecordById(Long id)
    {
        return parkingRechargeRecordMapper.deleteParkingRechargeRecordById(id);
    }
}
