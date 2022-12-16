package com.ruoyi.parking.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.parking.mapper.ParkingFixedparkingspaceMapper;
import com.ruoyi.parking.domain.ParkingFixedparkingspace;
import com.ruoyi.parking.service.IParkingFixedparkingspaceService;

/**
 * 停车场固定车位Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-12-13
 */
@Service
public class ParkingFixedparkingspaceServiceImpl implements IParkingFixedparkingspaceService 
{
    @Autowired
    private ParkingFixedparkingspaceMapper parkingFixedparkingspaceMapper;

    /**
     * 查询停车场固定车位
     * 
     * @param id 停车场固定车位主键
     * @return 停车场固定车位
     */
    @Override
    public ParkingFixedparkingspace selectParkingFixedparkingspaceById(Long id)
    {
        return parkingFixedparkingspaceMapper.selectParkingFixedparkingspaceById(id);
    }

    /**
     * 查询停车场固定车位列表
     * 
     * @param parkingFixedparkingspace 停车场固定车位
     * @return 停车场固定车位
     */
    @Override
    public List<ParkingFixedparkingspace> selectParkingFixedparkingspaceList(ParkingFixedparkingspace parkingFixedparkingspace) {
        if (parkingFixedparkingspace.getParkinglotinformationid()==0){
            return parkingFixedparkingspaceMapper.selectParkingFixedparkingspaceList(null);
        }
        return parkingFixedparkingspaceMapper.selectParkingFixedparkingspaceList(parkingFixedparkingspace);
    }

    /**
     * 新增停车场固定车位
     * 
     * @param parkingFixedparkingspace 停车场固定车位
     * @return 结果
     */
    @Override
    public int insertParkingFixedparkingspace(ParkingFixedparkingspace parkingFixedparkingspace)
    {
        return parkingFixedparkingspaceMapper.insertParkingFixedparkingspace(parkingFixedparkingspace);
    }

    /**
     * 修改停车场固定车位
     * 
     * @param parkingFixedparkingspace 停车场固定车位
     * @return 结果
     */
    @Override
    public int updateParkingFixedparkingspace(ParkingFixedparkingspace parkingFixedparkingspace)
    {
        return parkingFixedparkingspaceMapper.updateParkingFixedparkingspace(parkingFixedparkingspace);
    }

    /**
     * 批量删除停车场固定车位
     * 
     * @param ids 需要删除的停车场固定车位主键
     * @return 结果
     */
    @Override
    public int deleteParkingFixedparkingspaceByIds(Long[] ids)
    {
        return parkingFixedparkingspaceMapper.deleteParkingFixedparkingspaceByIds(ids);
    }

    /**
     * 删除停车场固定车位信息
     * 
     * @param id 停车场固定车位主键
     * @return 结果
     */
    @Override
    public int deleteParkingFixedparkingspaceById(Long id)
    {
        return parkingFixedparkingspaceMapper.deleteParkingFixedparkingspaceById(id);
    }

    @Override
    public ParkingFixedparkingspace findByParkingLotInformationIdAndLicense(Long id, String license) {
        return parkingFixedparkingspaceMapper.findByParkingLotInformationIdAndLicense(id,license);
    }
}
