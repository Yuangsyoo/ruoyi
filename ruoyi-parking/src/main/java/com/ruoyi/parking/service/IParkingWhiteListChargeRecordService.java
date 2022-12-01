package com.ruoyi.parking.service;

import java.util.List;
import com.ruoyi.parking.domain.ParkingWhiteListChargeRecord;

/**
 * 白名单收费记录Service接口
 * 
 * @author ruoyi
 * @date 2022-11-30
 */
public interface IParkingWhiteListChargeRecordService 
{
    /**
     * 查询白名单收费记录
     * 
     * @param id 白名单收费记录主键
     * @return 白名单收费记录
     */
    public ParkingWhiteListChargeRecord selectParkingWhiteListChargeRecordById(Long id);

    /**
     * 查询白名单收费记录列表
     * 
     * @param parkingWhiteListChargeRecord 白名单收费记录
     * @return 白名单收费记录集合
     */
    public List<ParkingWhiteListChargeRecord> selectParkingWhiteListChargeRecordList(ParkingWhiteListChargeRecord parkingWhiteListChargeRecord);

    /**
     * 新增白名单收费记录
     * 
     * @param parkingWhiteListChargeRecord 白名单收费记录
     * @return 结果
     */
    public int insertParkingWhiteListChargeRecord(ParkingWhiteListChargeRecord parkingWhiteListChargeRecord);

    /**
     * 修改白名单收费记录
     * 
     * @param parkingWhiteListChargeRecord 白名单收费记录
     * @return 结果
     */
    public int updateParkingWhiteListChargeRecord(ParkingWhiteListChargeRecord parkingWhiteListChargeRecord);

    /**
     * 批量删除白名单收费记录
     * 
     * @param ids 需要删除的白名单收费记录主键集合
     * @return 结果
     */
    public int deleteParkingWhiteListChargeRecordByIds(Long[] ids);

    /**
     * 删除白名单收费记录信息
     * 
     * @param id 白名单收费记录主键
     * @return 结果
     */
    public int deleteParkingWhiteListChargeRecordById(Long id);
}
