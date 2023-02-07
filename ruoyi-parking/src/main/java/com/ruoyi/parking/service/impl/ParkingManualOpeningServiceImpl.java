package com.ruoyi.parking.service.impl;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.core.domain.entity.ParkingLotEquipment;

import com.ruoyi.parking.mapper.ParkingLotEquipmentMapper;
import com.ruoyi.parking.mapper.ParkingLotInformationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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
    @Autowired
    private RedisTemplate<Object,Object>redisTemplate;

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
        parkingManualOpening.setTime(new Date());
        String username = SecurityUtils.getUsername();
        parkingManualOpening.setOperator(username);
        ParkingLotEquipment parkingLotEquipment = parkingLotEquipmentMapper.selectParkingLotEquipmentById(parkingManualOpening.getParkinglotequipmentid());

        String instructions="{\"Response_AlarmInfoPlate\":{\"info\":\"ok\",\"content\":\"...\",\"is_pay\":\"true\"}}";
        redisTemplate.opsForValue().set(parkingLotEquipment.getCameraserialnumber(),instructions,30, TimeUnit.SECONDS);
      /*  LPRDemo lprDemo = new LPRDemo();
        int i = lprDemo.InitClient(parkingLotEquipment.getIpadress());
        lprDemo.switchOn(i,0,500);
        //关闭设备的控制句柄
        lprDemo.VzLPRClient_Close(i);
        //执行结束释放
        lprDemo.VzLPRClient_Cleanup();*/


        return parkingManualOpeningMapper.insertParkingManualOpening(parkingManualOpening);
    }

    /**
     * 修改停车场手动开杆管理
     * 
     * @param parkingManualOpening 停车场手动开杆管理
     * @return 结果
     */
    @Override
    public int updateParkingManualOpening(ParkingManualOpening parkingManualOpening) {

        return parkingManualOpeningMapper.updateParkingManualOpening(parkingManualOpening);
    }

    /**
     * 批量删除停车场手动开杆管理
     * 
     * @param ids 需要删除的停车场手动开杆管理主键
     * @return 结果
     */
    @Override
    public int deleteParkingManualOpeningByIds(Long[] ids) {
        return parkingManualOpeningMapper.deleteParkingManualOpeningByIds(ids);
    }

    /**
     * 删除停车场手动开杆管理信息
     * 
     * @param id 停车场手动开杆管理主键
     * @return 结果
     */
    @Override
    public int deleteParkingManualOpeningById(Long id) {
        return parkingManualOpeningMapper.deleteParkingManualOpeningById(id);
    }
}
