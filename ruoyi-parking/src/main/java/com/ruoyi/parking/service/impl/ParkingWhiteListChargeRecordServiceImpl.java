package com.ruoyi.parking.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.parking.mapper.ParkingWhiteListChargeRecordMapper;
import com.ruoyi.parking.domain.ParkingWhiteListChargeRecord;
import com.ruoyi.parking.service.IParkingWhiteListChargeRecordService;

/**
 * 白名单收费记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-11-30
 */
@Service
public class ParkingWhiteListChargeRecordServiceImpl implements IParkingWhiteListChargeRecordService 
{
    @Autowired
    private ParkingWhiteListChargeRecordMapper parkingWhiteListChargeRecordMapper;

    /**
     * 查询白名单收费记录
     * 
     * @param id 白名单收费记录主键
     * @return 白名单收费记录
     */
    @Override
    public ParkingWhiteListChargeRecord selectParkingWhiteListChargeRecordById(Long id)
    {
        return parkingWhiteListChargeRecordMapper.selectParkingWhiteListChargeRecordById(id);
    }

    /**
     * 查询白名单收费记录列表
     * 
     * @param parkingWhiteListChargeRecord 白名单收费记录
     * @return 白名单收费记录
     */
    @Override
    public List<ParkingWhiteListChargeRecord> selectParkingWhiteListChargeRecordList(ParkingWhiteListChargeRecord parkingWhiteListChargeRecord)
    {
        return parkingWhiteListChargeRecordMapper.selectParkingWhiteListChargeRecordList(parkingWhiteListChargeRecord);
    }

    /**
     * 新增白名单收费记录
     * 
     * @param parkingWhiteListChargeRecord 白名单收费记录
     * @return 结果
     */
    @Override
    public int insertParkingWhiteListChargeRecord(ParkingWhiteListChargeRecord parkingWhiteListChargeRecord)
    {
        return parkingWhiteListChargeRecordMapper.insertParkingWhiteListChargeRecord(parkingWhiteListChargeRecord);
    }

    /**
     * 修改白名单收费记录
     * 
     * @param parkingWhiteListChargeRecord 白名单收费记录
     * @return 结果
     */
    @Override
    public int updateParkingWhiteListChargeRecord(ParkingWhiteListChargeRecord parkingWhiteListChargeRecord)
    {
        return parkingWhiteListChargeRecordMapper.updateParkingWhiteListChargeRecord(parkingWhiteListChargeRecord);
    }

    /**
     * 批量删除白名单收费记录
     * 
     * @param ids 需要删除的白名单收费记录主键
     * @return 结果
     */
    @Override
    public int deleteParkingWhiteListChargeRecordByIds(Long[] ids)
    {
        return parkingWhiteListChargeRecordMapper.deleteParkingWhiteListChargeRecordByIds(ids);
    }

    /**
     * 删除白名单收费记录信息
     * 
     * @param id 白名单收费记录主键
     * @return 结果
     */
    @Override
    public int deleteParkingWhiteListChargeRecordById(Long id)
    {
        return parkingWhiteListChargeRecordMapper.deleteParkingWhiteListChargeRecordById(id);
    }
}
