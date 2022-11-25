package com.ruoyi.parking.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
 //通过 停车场id，车牌号，和支付状态查询是否有无未支付订单
    @Override
    public ParkingRecord findByLicenseAndPayState(String license, Long parkingLotInformationId) {
        return parkingRecordMapper.findByLicenseAndPayState(license,parkingLotInformationId);
    }
}
