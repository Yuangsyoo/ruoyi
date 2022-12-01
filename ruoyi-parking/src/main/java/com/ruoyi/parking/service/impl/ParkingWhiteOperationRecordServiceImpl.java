package com.ruoyi.parking.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.parking.mapper.ParkingWhiteOperationRecordMapper;
import com.ruoyi.parking.domain.ParkingWhiteOperationRecord;
import com.ruoyi.parking.service.IParkingWhiteOperationRecordService;

/**
 * 白名单操作记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-11-30
 */
@Service
public class ParkingWhiteOperationRecordServiceImpl implements IParkingWhiteOperationRecordService 
{
    @Autowired
    private ParkingWhiteOperationRecordMapper parkingWhiteOperationRecordMapper;

    /**
     * 查询白名单操作记录
     * 
     * @param id 白名单操作记录主键
     * @return 白名单操作记录
     */
    @Override
    public ParkingWhiteOperationRecord selectParkingWhiteOperationRecordById(Long id)
    {
        return parkingWhiteOperationRecordMapper.selectParkingWhiteOperationRecordById(id);
    }

    /**
     * 查询白名单操作记录列表
     * 
     * @param parkingWhiteOperationRecord 白名单操作记录
     * @return 白名单操作记录
     */
    @Override
    public List<ParkingWhiteOperationRecord> selectParkingWhiteOperationRecordList(ParkingWhiteOperationRecord parkingWhiteOperationRecord)
    {
        return parkingWhiteOperationRecordMapper.selectParkingWhiteOperationRecordList(parkingWhiteOperationRecord);
    }

    /**
     * 新增白名单操作记录
     * 
     * @param parkingWhiteOperationRecord 白名单操作记录
     * @return 结果
     */
    @Override
    public int insertParkingWhiteOperationRecord(ParkingWhiteOperationRecord parkingWhiteOperationRecord)
    {
        return parkingWhiteOperationRecordMapper.insertParkingWhiteOperationRecord(parkingWhiteOperationRecord);
    }

    /**
     * 修改白名单操作记录
     * 
     * @param parkingWhiteOperationRecord 白名单操作记录
     * @return 结果
     */
    @Override
    public int updateParkingWhiteOperationRecord(ParkingWhiteOperationRecord parkingWhiteOperationRecord)
    {
        return parkingWhiteOperationRecordMapper.updateParkingWhiteOperationRecord(parkingWhiteOperationRecord);
    }

    /**
     * 批量删除白名单操作记录
     * 
     * @param ids 需要删除的白名单操作记录主键
     * @return 结果
     */
    @Override
    public int deleteParkingWhiteOperationRecordByIds(Long[] ids)
    {
        return parkingWhiteOperationRecordMapper.deleteParkingWhiteOperationRecordByIds(ids);
    }

    /**
     * 删除白名单操作记录信息
     * 
     * @param id 白名单操作记录主键
     * @return 结果
     */
    @Override
    public int deleteParkingWhiteOperationRecordById(Long id)
    {
        return parkingWhiteOperationRecordMapper.deleteParkingWhiteOperationRecordById(id);
    }
}
