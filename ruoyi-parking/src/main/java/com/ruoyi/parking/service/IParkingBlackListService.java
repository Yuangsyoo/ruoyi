package com.ruoyi.parking.service;

import java.util.List;
import com.ruoyi.parking.domain.ParkingBlackList;

/**
 * 黑名单管理Service接口
 * 
 * @author ruoyi
 * @date 2022-12-05
 */
public interface IParkingBlackListService 
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
     * 批量删除黑名单管理
     * 
     * @param ids 需要删除的黑名单管理主键集合
     * @return 结果
     */
    public int deleteParkingBlackListByIds(Long[] ids);

    /**
     * 删除黑名单管理信息
     * 
     * @param id 黑名单管理主键
     * @return 结果
     */
    public int deleteParkingBlackListById(Long id);
    /**
     * 求黑名单数量总和
     * @return
     */
    public Long sumAllBlack(Long id);
    /**
     * 导入用户数据
     *
     * @param stuList 用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
   public String importUser(List<ParkingBlackList> stuList, boolean isUpdateSupport, String operName, Long parkingLotInformationId);
}
