package com.ruoyi.parking.mapper;

import java.util.List;
import com.ruoyi.parking.domain.ParkingWhiteOperationRecord;

/**
 * 白名单操作记录Mapper接口
 * 
 * @author ruoyi
 * @date 2022-11-30
 */
public interface ParkingWhiteOperationRecordMapper 
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
     * 删除白名单操作记录
     * 
     * @param id 白名单操作记录主键
     * @return 结果
     */
    public int deleteParkingWhiteOperationRecordById(Long id);

    /**
     * 批量删除白名单操作记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteParkingWhiteOperationRecordByIds(Long[] ids);
}
