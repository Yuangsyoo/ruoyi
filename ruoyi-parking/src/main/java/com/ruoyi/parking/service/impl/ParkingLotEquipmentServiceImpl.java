package com.ruoyi.parking.service.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;


import com.ruoyi.common.core.domain.entity.ParkingLotInformation;
import com.ruoyi.common.exception.GlobalException;
import com.ruoyi.common.utils.QRCodeGenerator;
import com.ruoyi.parking.utils.SerialPortUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.ruoyi.parking.mapper.ParkingLotEquipmentMapper;
import com.ruoyi.common.core.domain.entity.ParkingLotEquipment;
import com.ruoyi.parking.service.IParkingLotEquipmentService;
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired
    private RedisTemplate<Object,Object>redisTemplate;

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
    @Transactional
    public int insertParkingLotEquipment(ParkingLotEquipment parkingLotEquipment) {
        parkingLotEquipmentMapper.insertParkingLotEquipment(parkingLotEquipment);
        try {
            //无牌车扫码进场
            //设备id
            Long parkinglotequipmentid = parkingLotEquipment.getId();
            //停车场id
            Long parkinglotinformationid = parkingLotEquipment.getParkinglotinformationid();
            if (parkingLotEquipment.getDirection().equals("0")){
                String text= "http://wp.ycwl.work?parkinglotequipmentid="+parkinglotequipmentid+"&type=0";
                String s = QRCodeGenerator.generateQRCodeImage(text, 360, 360);
                parkingLotEquipment.setQrcode(s);
            }else {
                //有牌
                String text= "http://wp.ycwl.work?parkinglotequipmentid="+parkinglotequipmentid+"&parkinglotinformationid="+parkinglotinformationid+"&type=1";
                String s = QRCodeGenerator.generateQRCodeImage(text, 360, 360);
                //无牌
                String text1=  "http://wp.ycwl.work?parkinglotequipmentid="+parkinglotequipmentid+"&type=2";
                String s1 = QRCodeGenerator.generateQRCodeImage(text1, 360, 360);
                parkingLotEquipment.setNoLicensePlateCode(s1);
                parkingLotEquipment.setQrcode(s);
            }
            String advertisement = SerialPortUtils.advertisement(parkingLotEquipment);
            redisTemplate.opsForValue().set(parkingLotEquipment.getCameraserialnumber()+"revise",advertisement,30, TimeUnit.SECONDS);

            return parkingLotEquipmentMapper.updateParkingLotEquipment(parkingLotEquipment);
        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException("添加失败");
        }

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
        String advertisement = SerialPortUtils.advertisement(parkingLotEquipment);
        redisTemplate.opsForValue().set(parkingLotEquipment.getCameraserialnumber()+"revise",advertisement,30, TimeUnit.SECONDS);

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

    @Override
    public List<ParkingLotEquipment> byParkinglotinformationid(Long parkinglotinformationid) {
        return parkingLotEquipmentMapper.byParkinglotinformationid(parkinglotinformationid);
    }

    @Override
    public ParkingLotEquipment selectParkingLotEquipmentFromId(Long parkinglotequipmentid) {
        return parkingLotEquipmentMapper.selectParkingLotEquipmentFromId(parkinglotequipmentid);
    }

    @Override
    public List<ParkingLotEquipment> byParkinglotinformationid1(Long parkinglotinformationid) {
        return parkingLotEquipmentMapper.byParkinglotinformationid1(parkinglotinformationid);
    }
}
