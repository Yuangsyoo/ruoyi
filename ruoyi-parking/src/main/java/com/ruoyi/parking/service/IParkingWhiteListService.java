package com.ruoyi.parking.service;

import java.util.List;
import com.ruoyi.parking.domain.ParkingWhiteList;

/**
 * 停车场白名单Service接口
 * 
 * @author ruoyi
 * @date 2022-11-29
 */
public interface IParkingWhiteListService 
{
    /**
     * 查询停车场白名单
     * 
     * @param id 停车场白名单主键
     * @return 停车场白名单
     */
    public ParkingWhiteList selectParkingWhiteListById(Long id);

    /**
     * 查询停车场白名单列表
     * 
     * @param parkingWhiteList 停车场白名单
     * @return 停车场白名单集合
     */
    public List<ParkingWhiteList> selectParkingWhiteListList(ParkingWhiteList parkingWhiteList);

    /**
     * 新增停车场白名单
     * 
     * @param parkingWhiteList 停车场白名单
     * @return 结果
     */
    public int insertParkingWhiteList(ParkingWhiteList parkingWhiteList);

    /**
     * 修改停车场白名单
     * 
     * @param parkingWhiteList 停车场白名单
     * @return 结果
     */
    public int updateParkingWhiteList1(ParkingWhiteList parkingWhiteList);
    public int updateParkingWhiteList(ParkingWhiteList parkingWhiteList);

    /**
     * 批量删除停车场白名单
     * 
     * @param ids 需要删除的停车场白名单主键集合
     * @return 结果
     */
    public int deleteParkingWhiteListByIds(Long[] ids);

    /**
     * 删除停车场白名单信息
     * 
     * @param id 停车场白名单主键
     * @return 结果
     */
    public int deleteParkingWhiteListById(Long id);

    /**
     * 求白名单数量总和
     * @return
     */
    public Long summation(Long id);

    /**
     * 查询过期白名单的数量
     * @return
     */
    public Long overdueWhiteList(Long id);
}
