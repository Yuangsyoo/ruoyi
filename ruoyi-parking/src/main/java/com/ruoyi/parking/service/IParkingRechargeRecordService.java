package com.ruoyi.parking.service;

import java.util.List;
import com.ruoyi.parking.domain.ParkingRechargeRecord;

/**
 * 充值车操作记录Service接口
 * 
 * @author ruoyi
 * @date 2023-02-01
 */
public interface IParkingRechargeRecordService 
{
    /**
     * 查询充值车操作记录
     * 
     * @param id 充值车操作记录主键
     * @return 充值车操作记录
     */
    public ParkingRechargeRecord selectParkingRechargeRecordById(Long id);

    /**
     * 查询充值车操作记录列表
     * 
     * @param parkingRechargeRecord 充值车操作记录
     * @return 充值车操作记录集合
     */
    public List<ParkingRechargeRecord> selectParkingRechargeRecordList(ParkingRechargeRecord parkingRechargeRecord);

    /**
     * 新增充值车操作记录
     * 
     * @param parkingRechargeRecord 充值车操作记录
     * @return 结果
     */
    public int insertParkingRechargeRecord(ParkingRechargeRecord parkingRechargeRecord);

    /**
     * 修改充值车操作记录
     * 
     * @param parkingRechargeRecord 充值车操作记录
     * @return 结果
     */
    public int updateParkingRechargeRecord(ParkingRechargeRecord parkingRechargeRecord);

    /**
     * 批量删除充值车操作记录
     * 
     * @param ids 需要删除的充值车操作记录主键集合
     * @return 结果
     */
    public int deleteParkingRechargeRecordByIds(Long[] ids);

    /**
     * 删除充值车操作记录信息
     * 
     * @param id 充值车操作记录主键
     * @return 结果
     */
    public int deleteParkingRechargeRecordById(Long id);
}
