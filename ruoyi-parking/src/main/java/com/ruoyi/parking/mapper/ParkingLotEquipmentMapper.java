package com.ruoyi.parking.mapper;

import java.util.List;
import com.ruoyi.parking.domain.ParkingLotEquipment;

/**
 * 停车场设备管理Mapper接口
 * 
 * @author ruoyi
 * @date 2022-11-24
 */
public interface ParkingLotEquipmentMapper 
{
    /**
     * 查询停车场设备管理
     * 
     * @param id 停车场设备管理主键
     * @return 停车场设备管理
     */
    public ParkingLotEquipment selectParkingLotEquipmentById(Long id);

    /**
     * 查询停车场设备管理列表
     * 
     * @param parkingLotEquipment 停车场设备管理
     * @return 停车场设备管理集合
     */
    public List<ParkingLotEquipment> selectParkingLotEquipmentList(ParkingLotEquipment parkingLotEquipment);

    /**
     * 新增停车场设备管理
     * 
     * @param parkingLotEquipment 停车场设备管理
     * @return 结果
     */
    public int insertParkingLotEquipment(ParkingLotEquipment parkingLotEquipment);

    /**
     * 修改停车场设备管理
     * 
     * @param parkingLotEquipment 停车场设备管理
     * @return 结果
     */
    public int updateParkingLotEquipment(ParkingLotEquipment parkingLotEquipment);

    /**
     * 删除停车场设备管理
     * 
     * @param id 停车场设备管理主键
     * @return 结果
     */
    public int deleteParkingLotEquipmentById(Long id);

    /**
     * 批量删除停车场设备管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteParkingLotEquipmentByIds(Long[] ids);
    //通过序列号获取推送数据设备信息
    ParkingLotEquipment findParkingLotEquipmentBySerialno(String cameraSerialNumber);
    //查询停车场id通过序列号
    Long findBySerialno(String serialno);
}
