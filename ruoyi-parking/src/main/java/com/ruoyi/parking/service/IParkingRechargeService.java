package com.ruoyi.parking.service;

import java.util.List;
import com.ruoyi.parking.domain.ParkingRecharge;

/**
 * 充值车管理Service接口
 * 
 * @author ruoyi
 * @date 2023-02-01
 */
public interface IParkingRechargeService 
{
    /**
     * 查询充值车管理
     * 
     * @param id 充值车管理主键
     * @return 充值车管理
     */
    public ParkingRecharge selectParkingRechargeById(Long id);

    /**
     * 查询充值车管理列表
     * 
     * @param parkingRecharge 充值车管理
     * @return 充值车管理集合
     */
    public List<ParkingRecharge> selectParkingRechargeList(ParkingRecharge parkingRecharge);

    /**
     * 新增充值车管理
     * 
     * @param parkingRecharge 充值车管理
     * @return 结果
     */
    public int insertParkingRecharge(ParkingRecharge parkingRecharge);

    /**
     * 修改充值车管理
     * 
     * @param parkingRecharge 充值车管理
     * @return 结果
     */
    public int updateParkingRecharge(ParkingRecharge parkingRecharge);
    public int updateParkingRecharge1(ParkingRecharge parkingRecharge);
    /**
     * 批量删除充值车管理
     * 
     * @param ids 需要删除的充值车管理主键集合
     * @return 结果
     */
    public int deleteParkingRechargeByIds(Long[] ids);

    /**
     * 删除充值车管理信息
     * 
     * @param id 充值车管理主键
     * @return 结果
     */
    public int deleteParkingRechargeById(Long id);

    ParkingRecharge findBy(Long id, String license);
}
