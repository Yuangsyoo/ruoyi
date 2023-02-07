package com.ruoyi.parking.mapper;

import java.util.List;
import com.ruoyi.parking.domain.ParkingCharging;
import com.ruoyi.parking.domain.ParkingBillingPeriod;
import org.apache.ibatis.annotations.Param;

/**
 * 计费规则Mapper接口
 * 
 * @author ruoyi
 * @date 2022-12-19
 */
public interface ParkingChargingMapper 
{
    /**
     * 查询计费规则
     * 
     * @param id 计费规则主键
     * @return 计费规则
     */
    public ParkingCharging selectParkingChargingById(Long id);

    /**
     * 查询计费规则列表
     * 
     * @param parkingCharging 计费规则
     * @return 计费规则集合
     */
    public List<ParkingCharging> selectParkingChargingList(ParkingCharging parkingCharging);

    /**
     * 新增计费规则
     * 
     * @param parkingCharging 计费规则
     * @return 结果
     */
    public int insertParkingCharging(ParkingCharging parkingCharging);

    /**
     * 修改计费规则
     * 
     * @param parkingCharging 计费规则
     * @return 结果
     */
    public int updateParkingCharging(ParkingCharging parkingCharging);

    /**
     * 删除计费规则
     * 
     * @param id 计费规则主键
     * @return 结果
     */
    public int deleteParkingChargingById(Long id);

    /**
     * 批量删除计费规则
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteParkingChargingByIds(Long[] ids);

    /**
     * 批量删除计费时间段
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteParkingBillingPeriodByParkingChargingIds(Long[] ids);
    
    /**
     * 批量新增计费时间段
     * 
     * @param parkingBillingPeriodList 计费时间段列表
     * @return 结果
     */
    public int batchParkingBillingPeriod(List<ParkingBillingPeriod> parkingBillingPeriodList);
    

    /**
     * 通过计费规则主键删除计费时间段信息
     * 
     * @param id 计费规则ID
     * @return 结果
     */
    public int deleteParkingBillingPeriodByParkingChargingId(Long id);

    ParkingCharging findByParkinglotinformationid(@Param("parkinglotinformationid") Long parkinglotinformationid, @Param("distinguish") Long distinguish);
}
