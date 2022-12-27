package com.ruoyi.parking.mapper;

import java.util.List;
import com.ruoyi.parking.domain.ParkingWhiteList;
import org.apache.ibatis.annotations.Param;

/**
 * 停车场白名单Mapper接口
 * 
 * @author ruoyi
 * @date 2022-11-29
 */
public interface ParkingWhiteListMapper 
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
    public int updateParkingWhiteList(ParkingWhiteList parkingWhiteList);

    /**
     * 删除停车场白名单
     * 
     * @param id 停车场白名单主键
     * @return 结果
     */
    public int deleteParkingWhiteListById(Long id);

    /**
     * 批量删除停车场白名单
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteParkingWhiteListByIds(Long[] ids);

    ParkingWhiteList findByLicense(@Param("license") String license,@Param("parkingLotInformationId") Long parkingLotInformationId);
    /**
     * 查询总的白名单数量
     * @return
     */
    public Long selectAllWhiteListSum(@Param("parkingLotInformationId")Long parkingLotInformationId);

    /**
     * 查询总的过期的白名单数量
     * @return
     */
    public Long selectAllOverdueWhite(@Param("parkingLotInformationId")Long parkingLotInformationId);
}
