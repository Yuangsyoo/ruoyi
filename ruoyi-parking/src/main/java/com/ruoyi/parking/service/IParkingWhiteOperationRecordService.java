package com.ruoyi.parking.service;

import java.util.List;
import com.ruoyi.parking.domain.ParkingWhiteOperationRecord;

/**
 * 白名单操作记录Service接口
 * 
 * @author ruoyi
 * @date 2022-11-30
 */
public interface IParkingWhiteOperationRecordService 
{
    /**
     * 查询白名单操作记录
     * 
     * @param id 白名单操作记录主键
     * @return 白名单操作记录
     */
    public ParkingWhiteOperationRecord selectParkingWhiteOperationRecordById(Long id);

    /**
     * 查询白名单操作记录列表
     * 
     * @param parkingWhiteOperationRecord 白名单操作记录
     * @return 白名单操作记录集合
     */
    public List<ParkingWhiteOperationRecord> selectParkingWhiteOperationRecordList(ParkingWhiteOperationRecord parkingWhiteOperationRecord);

    /**
     * 新增白名单操作记录
     * 
     * @param parkingWhiteOperationRecord 白名单操作记录
     * @return 结果
     */
    public int insertParkingWhiteOperationRecord(ParkingWhiteOperationRecord parkingWhiteOperationRecord);

    /**
     * 修改白名单操作记录
     * 
     * @param parkingWhiteOperationRecord 白名单操作记录
     * @return 结果
     */
    public int updateParkingWhiteOperationRecord(ParkingWhiteOperationRecord parkingWhiteOperationRecord);

    /**
     * 批量删除白名单操作记录
     * 
     * @param ids 需要删除的白名单操作记录主键集合
     * @return 结果
     */
    public int deleteParkingWhiteOperationRecordByIds(Long[] ids);

    /**
     * 删除白名单操作记录信息
     * 
     * @param id 白名单操作记录主键
     * @return 结果
     */
    public int deleteParkingWhiteOperationRecordById(Long id);
}
