package com.ruoyi.parking.mapper;

import java.util.List;
import com.ruoyi.parking.domain.ParkingBlackList;
import org.apache.ibatis.annotations.Param;

/**
 * 黑名单管理Mapper接口
 * 
 * @author ruoyi
 * @date 2022-12-05
 */
public interface ParkingBlackListMapper 
{
    /**
     * 查询黑名单管理
     * 
     * @param id 黑名单管理主键
     * @return 黑名单管理
     */
    public ParkingBlackList selectParkingBlackListById(Long id);

    /**
     * 查询黑名单管理列表
     * 
     * @param parkingBlackList 黑名单管理
     * @return 黑名单管理集合
     */
    public List<ParkingBlackList> selectParkingBlackListList(ParkingBlackList parkingBlackList);

    /**
     * 新增黑名单管理
     * 
     * @param parkingBlackList 黑名单管理
     * @return 结果
     */
    public int insertParkingBlackList(ParkingBlackList parkingBlackList);

    /**
     * 修改黑名单管理
     * 
     * @param parkingBlackList 黑名单管理
     * @return 结果
     */
    public int updateParkingBlackList(ParkingBlackList parkingBlackList);

    /**
     * 删除黑名单管理
     * 
     * @param id 黑名单管理主键
     * @return 结果
     */
    public int deleteParkingBlackListById(Long id);

    /**
     * 批量删除黑名单管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteParkingBlackListByIds(Long[] ids);

    ParkingBlackList selectParkingBlackListByIdAndLicense(@Param("parkingLotInformationId") Long id, @Param("license")String license);
}
