package com.ruoyi.parking.mapper;

import java.util.List;
import com.ruoyi.parking.domain.ParkingRecord;
import org.apache.ibatis.annotations.Param;

/**
 * 停车记录Mapper接口
 * 
 * @author ruoyi
 * @date 2022-11-24
 */
public interface ParkingRecordMapper {

    //通过 停车场id，车牌号，和支付状态查询是否有无未支付订单
    public ParkingRecord findByLicenseAndPayState(@Param("license") String license, @Param("parkingLotInformationId") Long parkingLotInformationId);

    /**
     * 查询停车记录
     * 
     * @param id 停车记录主键
     * @return 停车记录
     */
    public ParkingRecord selectParkingRecordById(Long id);

    /**
     * 查询停车记录列表
     * 
     * @param parkingRecord 停车记录
     * @return 停车记录集合
     */
    public List<ParkingRecord> selectParkingRecordList(ParkingRecord parkingRecord);

    /**
     * 新增停车记录
     * 
     * @param parkingRecord 停车记录
     * @return 结果
     */
    public int insertParkingRecord(ParkingRecord parkingRecord);

    /**
     * 修改停车记录
     * 
     * @param parkingRecord 停车记录
     * @return 结果
     */
    public int updateParkingRecord(ParkingRecord parkingRecord);

    /**
     * 删除停车记录
     * 
     * @param id 停车记录主键
     * @return 结果
     */
    public int deleteParkingRecordById(Long id);

    /**
     * 批量删除停车记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteParkingRecordByIds(Long[] ids);

    //通过停车场id，车牌号查询有无未支付订单
    ParkingRecord findByLicense(@Param("license") String license,@Param("parkingLotInformationId") Long parkingLotInformationId);
}
