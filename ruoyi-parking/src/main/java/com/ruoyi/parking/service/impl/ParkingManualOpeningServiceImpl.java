package com.ruoyi.parking.service.impl;

import java.util.Date;
import java.util.List;

import com.ruoyi.common.sdk.LPRDemo;
import com.ruoyi.parking.domain.ParkingLotEquipment;
import com.ruoyi.parking.domain.ParkingLotInformation;
import com.ruoyi.parking.mapper.ParkingLotEquipmentMapper;
import com.ruoyi.parking.mapper.ParkingLotInformationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.parking.mapper.ParkingManualOpeningMapper;
import com.ruoyi.parking.domain.ParkingManualOpening;
import com.ruoyi.parking.service.IParkingManualOpeningService;

/**
 * 停车场手动开杆管理Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-12-01
 */
@Service
public class ParkingManualOpeningServiceImpl implements IParkingManualOpeningService 
{
    @Autowired
    private ParkingManualOpeningMapper parkingManualOpeningMapper;
    @Autowired
    private ParkingLotInformationMapper parkingLotInformationMapper;
    @Autowired
    private ParkingLotEquipmentMapper parkingLotEquipmentMapper;

    /**
     * 查询停车场手动开杆管理
     * 
     * @param id 停车场手动开杆管理主键
     * @return 停车场手动开杆管理
     */
    @Override
    public ParkingManualOpening selectParkingManualOpeningById(Long id)
    {
        return parkingManualOpeningMapper.selectParkingManualOpeningById(id);
    }

    /**
     * 查询停车场手动开杆管理列表
     * 
     * @param parkingManualOpening 停车场手动开杆管理
     * @return 停车场手动开杆管理
     */
    @Override
    public List<ParkingManualOpening> selectParkingManualOpeningList(ParkingManualOpening parkingManualOpening)
    {
        return parkingManualOpeningMapper.selectParkingManualOpeningList(parkingManualOpening);
    }

    /**
     * 新增停车场手动开杆管理
     * 
     * @param parkingManualOpening 停车场手动开杆管理
     * @return 结果
     */
    @Override
    public int insertParkingManualOpening(ParkingManualOpening parkingManualOpening)
    {
        return parkingManualOpeningMapper.insertParkingManualOpening(parkingManualOpening);
    }

    /**
     * 修改停车场手动开杆管理
     * 
     * @param parkingManualOpening 停车场手动开杆管理
     * @return 结果
     */
    @Override
    public int updateParkingManualOpening(ParkingManualOpening parkingManualOpening)
    {
        parkingManualOpening.setTime(new Date());
        ParkingLotEquipment parkingLotEquipment = parkingLotEquipmentMapper.selectParkingLotEquipmentById(parkingManualOpening.getParkinglotequipmentid());


        LPRDemo lprDemo = new LPRDemo();
        int i = lprDemo.InitClient(parkingLotEquipment.getIpadress());
        lprDemo.switchOn(i,0,500);
        //关闭设备的控制句柄
        lprDemo.VzLPRClient_Close(i);
        //执行结束释放
        lprDemo.VzLPRClient_Cleanup();
        return parkingManualOpeningMapper.updateParkingManualOpening(parkingManualOpening);
    }

    /**
     * 批量删除停车场手动开杆管理
     * 
     * @param ids 需要删除的停车场手动开杆管理主键
     * @return 结果
     */
    @Override
    public int deleteParkingManualOpeningByIds(Long[] ids)
    {
        return parkingManualOpeningMapper.deleteParkingManualOpeningByIds(ids);
    }

    /**
     * 删除停车场手动开杆管理信息
     * 
     * @param id 停车场手动开杆管理主键
     * @return 结果
     */
    @Override
    public int deleteParkingManualOpeningById(Long id)
    {
        return parkingManualOpeningMapper.deleteParkingManualOpeningById(id);
    }
}
