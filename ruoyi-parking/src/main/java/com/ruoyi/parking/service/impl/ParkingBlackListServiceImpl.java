package com.ruoyi.parking.service.impl;

import java.util.Date;
import java.util.List;

import com.ruoyi.common.core.domain.entity.ParkingLotInformation;
import com.ruoyi.common.exception.GlobalException;
import com.ruoyi.common.sdk.LPRDemo;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.parking.domain.ParkingLotEquipment;
import com.ruoyi.parking.mapper.ParkingLotEquipmentMapper;
import com.ruoyi.parking.mapper.ParkingLotInformationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.parking.mapper.ParkingBlackListMapper;
import com.ruoyi.parking.domain.ParkingBlackList;
import com.ruoyi.parking.service.IParkingBlackListService;

/**
 * 黑名单管理Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-12-05
 */
@Service
public class ParkingBlackListServiceImpl implements IParkingBlackListService 
{
    @Autowired
    private ParkingBlackListMapper parkingBlackListMapper;
    @Autowired
    private ParkingLotInformationMapper parkingLotInformationMapper;
    @Autowired
    private ParkingLotEquipmentMapper parkingLotEquipmentMapper;


    /**
     * 查询黑名单管理
     * 
     * @param id 黑名单管理主键
     * @return 黑名单管理
     */
    @Override
    public ParkingBlackList selectParkingBlackListById(Long id)
    {
        return parkingBlackListMapper.selectParkingBlackListById(id);
    }

    /**
     * 查询黑名单管理列表
     * 
     * @param parkingBlackList 黑名单管理
     * @return 黑名单管理
     */
    @Override
    public List<ParkingBlackList> selectParkingBlackListList(ParkingBlackList parkingBlackList)
    {
        return parkingBlackListMapper.selectParkingBlackListList(parkingBlackList);
    }

    /**
     * 新增黑名单管理
     * 
     * @param parkingBlackList 黑名单管理
     * @return 结果
     */
    @Override
    public int insertParkingBlackList(ParkingBlackList parkingBlackList)
    {
        //判断是否重复添加黑名单
        ParkingBlackList parkingBlackList1 = parkingBlackListMapper.selectParkingBlackListByIdAndLicense(parkingBlackList.getParkinglotinformationid(), parkingBlackList.getLicense());
        if (parkingBlackList1!=null){

            throw new GlobalException("黑名单已存在");
        }
        parkingBlackList.setTime(new Date());
        String username = SecurityUtils.getUsername();
        parkingBlackList.setOperator(username);
        return parkingBlackListMapper.insertParkingBlackList(parkingBlackList);
    }

    /**
     * 修改黑名单管理
     * 
     * @param parkingBlackList 黑名单管理
     * @return 结果
     */
    @Override
    public int updateParkingBlackList(ParkingBlackList parkingBlackList)
    {
        return parkingBlackListMapper.updateParkingBlackList(parkingBlackList);
    }

    /**
     * 批量删除黑名单管理
     * 
     * @param ids 需要删除的黑名单管理主键
     * @return 结果
     */
    @Override
    public int deleteParkingBlackListByIds(Long[] ids)
    {
        return parkingBlackListMapper.deleteParkingBlackListByIds(ids);
    }

    /**
     * 删除黑名单管理信息
     * 
     * @param id 黑名单管理主键
     * @return 结果
     */
    @Override
    public int deleteParkingBlackListById(Long id)
    {
        return parkingBlackListMapper.deleteParkingBlackListById(id);
    }
}
