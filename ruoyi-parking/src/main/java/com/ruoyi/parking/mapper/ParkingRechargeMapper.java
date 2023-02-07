package com.ruoyi.parking.mapper;

import java.util.List;
import com.ruoyi.parking.domain.ParkingRecharge;
import org.apache.ibatis.annotations.Param;

/**
 * 充值车管理Mapper接口
 * 
 * @author ruoyi
 * @date 2023-02-01
 */
public interface ParkingRechargeMapper 
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

    /**
     * 删除充值车管理
     * 
     * @param id 充值车管理主键
     * @return 结果
     */
    public int deleteParkingRechargeById(Long id);

    /**
     * 批量删除充值车管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteParkingRechargeByIds(Long[] ids);

    ParkingRecharge findBy(@Param("parkinglotinformationid") Long id,@Param("license") String license);
}
