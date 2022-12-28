package com.ruoyi.parking.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.ParkingLotInformation;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.entity.SysUserRole;
import com.ruoyi.common.core.mapper.SysUserRoleMapper;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.parking.dto.ParkingLotEquipmentDto;
import com.ruoyi.common.core.service.ISysUserService;
import com.ruoyi.parking.vo.NumberOfCarParksVo;
import com.ruoyi.parking.vo.ParkingLots;
import com.ruoyi.parking.vo.ParkingLotsVo;
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


        if (parkingLotInformation.getId()==0){
            List<ParkingLotInformation>  parkingLotInformations = parkingLotInformationMapper.selectParkingLotInformationList(parkingLotInformation);
            return parkingLotInformations;
        }else {
            List<ParkingLotInformation> list = new ArrayList<>();
            ParkingLotInformation parkingLotInformation1 = parkingLotInformationMapper.selectParkingLotInformationById(parkingLotInformation.getId());
            list.add(parkingLotInformation1);
            return list;
        }

    }

    /**
     * 新增停车场管理
     * 
     * @param
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
        parkingLotInformationMapper.insertParkingLotInformation(parkingLotInformation);
        SysUser user = new SysUser();
        //保存user信息
        Long i1 = creatUser(parkingLotInformation, loginPassword, user);
        //未user添加角色默认高级角色
        creatUserRole(i1);
        return 1;
    }

    private void creatUserRole(Long i1) {
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setUserId(i1);
        //默认高级角色
        sysUserRole.setRoleId(3L);
        int i2 = sysUserRoleMapper.creatUserRoleInfo(sysUserRole);
    }

    private Long creatUser(ParkingLotInformation parkingLotInformation, String loginPassword,  SysUser user) {
        //用户登录密码
        user.setPassword(SecurityUtils.encryptPassword(loginPassword));
        user.setUserName(parkingLotInformation.getName());
        user.setNickName(parkingLotInformation.getName());
        user.setSex("0");
        user.setCreateBy("添加停车场创建主账号");
        user.setCreateTime(new Date());
        user.setStatus("0");
        user.setParkinglotinformationId(parkingLotInformation.getId());
         userService.insertUser(user);
        return user.getUserId();
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
    public List<ParkingLotInformation> findParkingLotInformationList(Long id) {


        if (id==0){
            List<ParkingLotInformation>  parkingLotInformations = parkingLotInformationMapper.selectParkingLotInformationList(null);
            return parkingLotInformations;
        }else {
            List<ParkingLotInformation> list = new ArrayList<>();
            ParkingLotInformation parkingLotInformation1 = parkingLotInformationMapper.selectParkingLotInformationById(id);
            list.add(parkingLotInformation1);
            return list;
        }

    }

    @Override
    public AjaxResult getParkingLots(Long id) {
        if (id==0){
            return AjaxResult.success();
        }
        ParkingLotInformation parkingLotInformation = parkingLotInformationMapper.selectParkingLotInformationById(id);
        //车位总数
        Long number = parkingLotInformation.getNumber();
        //剩余车位
        Long remainingParkingSpace = parkingLotInformation.getRemainingParkingSpace();

        ParkingLotsVo parkingLotsVo = new ParkingLotsVo();
        parkingLotsVo.getName().add("剩余车位数");
        parkingLotsVo.getName().add("已停车位数");
        List<ParkingLots> list = parkingLotsVo.getParkingLots();
        list.add(new ParkingLots(remainingParkingSpace,"剩余车位数"));
        list.add(new ParkingLots(number-remainingParkingSpace,"已停车位数"));

        return AjaxResult.success(parkingLotsVo);
    }
    /**
     * 查询所有不同的停车场停的车辆数
     * @param id 判断传进来的值是否为管理员
     * @return
     */
    @Override
    public AjaxResult getNumberOfCarParks(Long id){
        // 判断id是否是管理员id
        if (id==0) {
            NumberOfCarParksVo numberOfCarParksVo = new NumberOfCarParksVo();
            List<ParkingLotInformation> date = parkingLotInformationMapper.selectAllNumberOfCarParks();
            for (ParkingLotInformation parkingLotInformation : date) {
                //总数
                Long number = parkingLotInformation.getNumber();
                //剩余车位
                Long remainingParkingSpace = parkingLotInformation.getRemainingParkingSpace();
                //停车名字
                String name = parkingLotInformation.getName();
                //占用车位
                numberOfCarParksVo.getValue().add(number - remainingParkingSpace);
                numberOfCarParksVo.getName().add(name);
            }
            return AjaxResult.success(numberOfCarParksVo);
        }
        return AjaxResult.success();
    }
}
