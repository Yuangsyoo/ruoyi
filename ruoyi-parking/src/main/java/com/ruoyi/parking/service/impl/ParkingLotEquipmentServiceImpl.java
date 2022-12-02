package com.ruoyi.parking.service.impl;

import java.util.List;


import com.ruoyi.common.core.domain.entity.ParkingLotInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.parking.mapper.ParkingLotEquipmentMapper;
import com.ruoyi.parking.domain.ParkingLotEquipment;
import com.ruoyi.parking.service.IParkingLotEquipmentService;

/**
 * 停车场设备管理Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-11-24
 */
@Service
public class ParkingLotEquipmentServiceImpl implements IParkingLotEquipmentService 
{
    @Autowired
    private ParkingLotEquipmentMapper parkingLotEquipmentMapper;

    /**
     * 查询停车场设备管理
     * 
     * @param id 停车场设备管理主键
     * @return 停车场设备管理
     */
    @Override
    public ParkingLotEquipment selectParkingLotEquipmentById(Long id)
    {
        return parkingLotEquipmentMapper.selectParkingLotEquipmentById(id);
    }

    /**
     * 查询停车场设备管理列表
     * 
     * @param parkingLotEquipment 停车场设备管理
     * @return 停车场设备管理
     */
    @Override
    public List<ParkingLotEquipment> selectParkingLotEquipmentList(ParkingLotEquipment parkingLotEquipment)
    {
        return parkingLotEquipmentMapper.selectParkingLotEquipmentList(parkingLotEquipment);
    }

    /**
     * 新增停车场设备管理
     * 
     * @param parkingLotEquipment 停车场设备管理
     * @return 结果
     */
    @Override
    public int insertParkingLotEquipment(ParkingLotEquipment parkingLotEquipment)
    {
        return parkingLotEquipmentMapper.insertParkingLotEquipment(parkingLotEquipment);
    }

    /**
     * 修改停车场设备管理
     * 
     * @param parkingLotEquipment 停车场设备管理
     * @return 结果
     */
    @Override
    public int updateParkingLotEquipment(ParkingLotEquipment parkingLotEquipment)
    {
        return parkingLotEquipmentMapper.updateParkingLotEquipment(parkingLotEquipment);
    }

    /**
     * 批量删除停车场设备管理
     * 
     * @param ids 需要删除的停车场设备管理主键
     * @return 结果
     */
    @Override
    public int deleteParkingLotEquipmentByIds(Long[] ids)
    {
        return parkingLotEquipmentMapper.deleteParkingLotEquipmentByIds(ids);
    }

    /**
     * 删除停车场设备管理信息
     * 
     * @param id 停车场设备管理主键
     * @return 结果
     */
    @Override
    public int deleteParkingLotEquipmentById(Long id)
    {
        return parkingLotEquipmentMapper.deleteParkingLotEquipmentById(id);
    }
    //通过序列号获取推送数据设备信息
    @Override
    public ParkingLotEquipment findParkingLotEquipmentBySerialno(String serialno) {
        return parkingLotEquipmentMapper.findParkingLotEquipmentBySerialno(serialno);
    }
    //查询停车场名称通过序列号
    @Override
    public ParkingLotInformation findBySerialno(String serialno) {
        return parkingLotEquipmentMapper.findBySerialno(serialno);
    }
}
