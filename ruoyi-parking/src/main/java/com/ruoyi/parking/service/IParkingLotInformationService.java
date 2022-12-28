package com.ruoyi.parking.service;

import java.util.List;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.ParkingLotInformation;
import com.ruoyi.parking.dto.ParkingLotEquipmentDto;


/**
 * 停车场管理Service接口
 * 
 * @author ruoyi
 * @date 2022-11-23
 */
public interface IParkingLotInformationService 
{
    /**
     * 查询停车场管理
     * 
     * @param id 停车场管理主键
     * @return 停车场管理
     */
    public ParkingLotInformation selectParkingLotInformationById(Long id);

    /**
     * 查询停车场管理列表
     * 
     * @param parkingLotInformation 停车场管理
     * @return 停车场管理集合
     */
    public List<ParkingLotInformation> selectParkingLotInformationList(ParkingLotInformation parkingLotInformation);

    /**
     * 新增停车场管理
     * 
     * @param parkingLotInformation 停车场管理
     * @return 结果
     */
    public int insertParkingLotInformation(ParkingLotEquipmentDto parkingLotEquipmentDto);

    /**
     * 修改停车场管理
     * 
     * @param parkingLotInformation 停车场管理
     * @return 结果
     */
    public int updateParkingLotInformation(ParkingLotInformation parkingLotInformation);

    /**
     * 批量删除停车场管理
     * 
     * @param ids 需要删除的停车场管理主键集合
     * @return 结果
     */
    public int deleteParkingLotInformationByIds(Long[] ids);

    /**
     * 删除停车场管理信息
     * 
     * @param id 停车场管理主键
     * @return 结果
     */
    public int deleteParkingLotInformationById(Long id);

    List<ParkingLotInformation> findParkingLotInformationList(Long id);

    AjaxResult getParkingLots(Long id);
    // 查询所有不同的停车场停的车辆数
    public AjaxResult getNumberOfCarParks(Long id);
}
