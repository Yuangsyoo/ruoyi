package com.ruoyi.parking.service;

import java.util.List;
import com.ruoyi.parking.domain.ParkingCharging;

/**
 * 计费规则Service接口
 * 
 * @author ruoyi
 * @date 2022-12-19
 */
public interface IParkingChargingService 
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
     * 批量删除计费规则
     * 
     * @param ids 需要删除的计费规则主键集合
     * @return 结果
     */
    public int deleteParkingChargingByIds(Long[] ids);

    /**
     * 删除计费规则信息
     * 
     * @param id 计费规则主键
     * @return 结果
     */
    public int deleteParkingChargingById(Long id);
}
