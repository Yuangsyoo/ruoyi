package com.ruoyi.parking.mapper;

import java.util.List;
import com.ruoyi.parking.domain.ParkingManualOpening;

/**
 * 停车场手动开杆管理Mapper接口
 * 
 * @author ruoyi
 * @date 2022-12-01
 */
public interface ParkingManualOpeningMapper 
{
    /**
     * 查询停车场手动开杆管理
     * 
     * @param id 停车场手动开杆管理主键
     * @return 停车场手动开杆管理
     */
    public ParkingManualOpening selectParkingManualOpeningById(Long id);

    /**
     * 查询停车场手动开杆管理列表
     * 
     * @param parkingManualOpening 停车场手动开杆管理
     * @return 停车场手动开杆管理集合
     */
    public List<ParkingManualOpening> selectParkingManualOpeningList(ParkingManualOpening parkingManualOpening);

    /**
     * 新增停车场手动开杆管理
     * 
     * @param parkingManualOpening 停车场手动开杆管理
     * @return 结果
     */
    public int insertParkingManualOpening(ParkingManualOpening parkingManualOpening);

    /**
     * 修改停车场手动开杆管理
     * 
     * @param parkingManualOpening 停车场手动开杆管理
     * @return 结果
     */
    public int updateParkingManualOpening(ParkingManualOpening parkingManualOpening);

    /**
     * 删除停车场手动开杆管理
     * 
     * @param id 停车场手动开杆管理主键
     * @return 结果
     */
    public int deleteParkingManualOpeningById(Long id);

    /**
     * 批量删除停车场手动开杆管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteParkingManualOpeningByIds(Long[] ids);
}
