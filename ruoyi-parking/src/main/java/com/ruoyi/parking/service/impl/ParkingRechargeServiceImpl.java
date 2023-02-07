package com.ruoyi.parking.service.impl;

import java.util.Date;
import java.util.List;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.parking.domain.ParkingRechargeRecord;
import com.ruoyi.parking.mapper.ParkingRechargeRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import com.ruoyi.parking.mapper.ParkingRechargeMapper;
import com.ruoyi.parking.domain.ParkingRecharge;
import com.ruoyi.parking.service.IParkingRechargeService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 充值车管理Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-02-01
 */
@Service
public class ParkingRechargeServiceImpl implements IParkingRechargeService 
{
    @Autowired
    private ParkingRechargeMapper parkingRechargeMapper;
    @Autowired
    private ParkingRechargeRecordMapper parkingRechargeRecordMapper;

    /**
     * 查询充值车管理
     * 
     * @param id 充值车管理主键
     * @return 充值车管理
     */
    @Override
    public ParkingRecharge selectParkingRechargeById(Long id)
    {
        return parkingRechargeMapper.selectParkingRechargeById(id);
    }

    /**
     * 查询充值车管理列表
     * 
     * @param parkingRecharge 充值车管理
     * @return 充值车管理
     */
    @Override
    public List<ParkingRecharge> selectParkingRechargeList(ParkingRecharge parkingRecharge)
    {
        return parkingRechargeMapper.selectParkingRechargeList(parkingRecharge);
    }

    /**
     * 新增充值车管理
     * 
     * @param parkingRecharge 充值车管理
     * @return 结果
     */
    @Override
    @Transactional
    public int insertParkingRecharge(ParkingRecharge parkingRecharge)
    {
        Date date = new Date();
        parkingRecharge.setAddtime(date);

        ParkingRechargeRecord parkingRechargeRecord = new ParkingRechargeRecord();
        parkingRechargeRecord.setLicense(parkingRecharge.getLicense());
        parkingRechargeRecord.setMoney(parkingRecharge.getMoney().toString());
        parkingRechargeRecord.setParkinglotinformationid(parkingRecharge.getParkinglotinformationid());
        parkingRechargeRecord.setOperation( SecurityUtils.getUsername());
        parkingRechargeRecord.setOperationtype("新增");
        parkingRechargeRecord.setOperationtime(date);

        parkingRechargeRecordMapper.insertParkingRechargeRecord(parkingRechargeRecord);
        parkingRecharge.setBalance(parkingRecharge.getMoney());
        return parkingRechargeMapper.insertParkingRecharge(parkingRecharge);
    }

    /**
     * 修改充值车管理
     * 
     * @param parkingRecharge 充值车管理
     * @return 结果
     */
    @Override
    @Transactional
    public int updateParkingRecharge(ParkingRecharge parkingRecharge)
    {
        return parkingRechargeMapper.updateParkingRecharge(parkingRecharge);
    }
    @Override
    @Transactional
    public int updateParkingRecharge1(ParkingRecharge parkingRecharge)
    {
        ParkingRechargeRecord parkingRechargeRecord = new ParkingRechargeRecord();
        parkingRechargeRecord.setLicense(parkingRecharge.getLicense());
        parkingRechargeRecord.setMoney(parkingRecharge.getMoney().toString());
        parkingRechargeRecord.setParkinglotinformationid(parkingRecharge.getParkinglotinformationid());
        parkingRechargeRecord.setOperation( SecurityUtils.getUsername());
        parkingRechargeRecord.setOperationtype("修改");
        parkingRechargeRecord.setOperationtime(new Date());
        parkingRechargeRecordMapper.insertParkingRechargeRecord(parkingRechargeRecord);

        parkingRecharge.setBalance(parkingRecharge.getBalance()+parkingRecharge.getMoney());
        return parkingRechargeMapper.updateParkingRecharge(parkingRecharge);
    }

    /**
     * 批量删除充值车管理
     * 
     * @param ids 需要删除的充值车管理主键
     * @return 结果
     */
    @Override
    public int deleteParkingRechargeByIds(Long[] ids)
    {
        return parkingRechargeMapper.deleteParkingRechargeByIds(ids);
    }

    /**
     * 删除充值车管理信息
     * 
     * @param id 充值车管理主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteParkingRechargeById(Long id)
    {
        ParkingRecharge parkingRecharge = selectParkingRechargeById(id);
        ParkingRechargeRecord parkingRechargeRecord = new ParkingRechargeRecord();
        parkingRechargeRecord.setLicense(parkingRecharge.getLicense());
        parkingRechargeRecord.setMoney("0");
        parkingRechargeRecord.setParkinglotinformationid(parkingRecharge.getParkinglotinformationid());
        parkingRechargeRecord.setOperation( SecurityUtils.getUsername());
        parkingRechargeRecord.setOperationtype("删除");
        parkingRechargeRecord.setOperationtime(new Date());

        parkingRechargeRecordMapper.insertParkingRechargeRecord(parkingRechargeRecord);

        return parkingRechargeMapper.deleteParkingRechargeById(id);
    }

    @Override
    public ParkingRecharge findBy(Long id, String license) {
        return parkingRechargeMapper.findBy(id,license);
    }
}
