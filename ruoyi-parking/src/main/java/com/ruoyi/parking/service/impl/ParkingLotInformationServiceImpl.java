package com.ruoyi.parking.service.impl;

import java.util.Date;
import java.util.List;

import com.ruoyi.common.core.domain.entity.ParkingLotInformation;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.entity.SysUserRole;
import com.ruoyi.common.core.mapper.SysUserRoleMapper;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.parking.dto.ParkingLotEquipmentDto;
import com.ruoyi.common.core.service.ISysUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.parking.mapper.ParkingLotInformationMapper;

import com.ruoyi.parking.service.IParkingLotInformationService;
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired
    private ISysUserService userService;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;


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
    @Transactional
    public int insertParkingLotInformation(ParkingLotEquipmentDto parkingLotEquipmentDto)
    {
        ParkingLotInformation parkingLotInformation = new ParkingLotInformation();
        //停车场信息
        BeanUtils.copyProperties(parkingLotEquipmentDto,parkingLotInformation);
        parkingLotInformation.setRemainingParkingSpace(parkingLotInformation.getNumber());

        String loginPassword = parkingLotEquipmentDto.getLoginPassword();
        int i = parkingLotInformationMapper.insertParkingLotInformation(parkingLotInformation);
        SysUser user = new SysUser();
        //保存user信息
        int i1 = creatUser(parkingLotInformation, loginPassword, i, user);
        //未user添加角色默认高级角色
        creatUserRole(i1);
        return 1;
    }

    private void creatUserRole(int i1) {
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setUserId(Long.valueOf(i1));
        //默认高级角色
        sysUserRole.setRoleId(3L);
        int i2 = sysUserRoleMapper.creatUserRoleInfo(sysUserRole);
    }

    private int creatUser(ParkingLotInformation parkingLotInformation, String loginPassword, int i, SysUser user) {
        //用户登录密码
        user.setPassword(SecurityUtils.encryptPassword(loginPassword));
        user.setUserName(parkingLotInformation.getName());
        user.setNickName(parkingLotInformation.getName());
        user.setSex("0");
        user.setCreateBy("添加停车场创建主账号");
        user.setCreateTime(new Date());
        user.setStatus("0");
        user.setParkinglotinformationId(Long.valueOf(i));
        int i1 = userService.insertUser(user);
        return i1;
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

    @Override
    public List<ParkingLotInformation> findParkingLotInformationList() {
        return parkingLotInformationMapper.findParkingLotInformationList();
    }
}
