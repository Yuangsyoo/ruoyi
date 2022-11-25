package com.ruoyi.parking.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.parking.mapper.ParkingLotInformationMapper;
import com.ruoyi.parking.domain.ParkingLotInformation;
import com.ruoyi.parking.service.IParkingLotInformationService;

/**
 * 停车场管理Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-11-23
 */
@Service
public class ParkingLotInformationServiceImpl implements IParkingLotInformationService 
{
    @Autowired
    private ParkingLotInformationMapper parkingLotInformationMapper;

    /**
     * 查询停车场管理
     * 
     * @param id 停车场管理主键
     * @return 停车场管理
     */
    @Override
    public ParkingLotInformation selectParkingLotInformationById(Long id)
    {
        return parkingLotInformationMapper.selectParkingLotInformationById(id);
    }

    /**
     * 查询停车场管理列表
     * 
     * @param parkingLotInformation 停车场管理
     * @return 停车场管理
     */
    @Override
    public List<ParkingLotInformation> selectParkingLotInformationList(ParkingLotInformation parkingLotInformation)
    {
        return parkingLotInformationMapper.selectParkingLotInformationList(parkingLotInformation);
    }

    /**
     * 新增停车场管理
     * 
     * @param parkingLotInformation 停车场管理
     * @return 结果
     */
    @Override
    public int insertParkingLotInformation(ParkingLotInformation parkingLotInformation)
    {
        return parkingLotInformationMapper.insertParkingLotInformation(parkingLotInformation);
    }

    /**
     * 修改停车场管理
     * 
     * @param parkingLotInformation 停车场管理
     * @return 结果
     */
    @Override
    public int updateParkingLotInformation(ParkingLotInformation parkingLotInformation)
    {
        return parkingLotInformationMapper.updateParkingLotInformation(parkingLotInformation);
    }

    /**
     * 批量删除停车场管理
     * 
     * @param ids 需要删除的停车场管理主键
     * @return 结果
     */
    @Override
    public int deleteParkingLotInformationByIds(Long[] ids)
    {
        return parkingLotInformationMapper.deleteParkingLotInformationByIds(ids);
    }

    /**
     * 删除停车场管理信息
     * 
     * @param id 停车场管理主键
     * @return 结果
     */
    @Override
    public int deleteParkingLotInformationById(Long id)
    {
        return parkingLotInformationMapper.deleteParkingLotInformationById(id);
    }
}
