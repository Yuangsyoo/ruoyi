package com.ruoyi.parking.mapper;

import java.util.List;
import com.ruoyi.parking.domain.ParkingLotInformation;

/**
 * 停车场管理Mapper接口
 * 
 * @author ruoyi
 * @date 2022-11-23
 */
public interface ParkingLotInformationMapper 
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
    public int insertParkingLotInformation(ParkingLotInformation parkingLotInformation);

    /**
     * 修改停车场管理
     * 
     * @param parkingLotInformation 停车场管理
     * @return 结果
     */
    public int updateParkingLotInformation(ParkingLotInformation parkingLotInformation);

    /**
     * 删除停车场管理
     * 
     * @param id 停车场管理主键
     * @return 结果
     */
    public int deleteParkingLotInformationById(Long id);

    /**
     * 批量删除停车场管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteParkingLotInformationByIds(Long[] ids);
}
