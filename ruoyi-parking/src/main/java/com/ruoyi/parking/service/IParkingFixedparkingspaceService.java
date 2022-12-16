package com.ruoyi.parking.service;

import java.util.List;
import com.ruoyi.parking.domain.ParkingFixedparkingspace;

/**
 * 停车场固定车位Service接口
 * 
 * @author ruoyi
 * @date 2022-12-13
 */
public interface IParkingFixedparkingspaceService 
{
    /**
     * 查询停车场固定车位
     * 
     * @param id 停车场固定车位主键
     * @return 停车场固定车位
     */
    public ParkingFixedparkingspace selectParkingFixedparkingspaceById(Long id);

    /**
     * 查询停车场固定车位列表
     * 
     * @param parkingFixedparkingspace 停车场固定车位
     * @return 停车场固定车位集合
     */
    public List<ParkingFixedparkingspace> selectParkingFixedparkingspaceList(ParkingFixedparkingspace parkingFixedparkingspace);

    /**
     * 新增停车场固定车位
     * 
     * @param parkingFixedparkingspace 停车场固定车位
     * @return 结果
     */
    public int insertParkingFixedparkingspace(ParkingFixedparkingspace parkingFixedparkingspace);

    /**
     * 修改停车场固定车位
     * 
     * @param parkingFixedparkingspace 停车场固定车位
     * @return 结果
     */
    public int updateParkingFixedparkingspace(ParkingFixedparkingspace parkingFixedparkingspace);

    /**
     * 批量删除停车场固定车位
     * 
     * @param ids 需要删除的停车场固定车位主键集合
     * @return 结果
     */
    public int deleteParkingFixedparkingspaceByIds(Long[] ids);

    /**
     * 删除停车场固定车位信息
     * 
     * @param id 停车场固定车位主键
     * @return 结果
     */
    public int deleteParkingFixedparkingspaceById(Long id);

    ParkingFixedparkingspace findByParkingLotInformationIdAndLicense(Long id, String license);
}
