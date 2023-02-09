package com.ruoyi.parking.service;

import java.util.List;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.parking.domain.ParkingRecord;
import com.ruoyi.parking.vo.ParkingRecordVo;

/**
 * 停车记录Service接口
 * 
 * @author ruoyi
 * @date 2022-11-24
 */
public interface IParkingRecordService 
{
    /**
     * 查询停车记录
     * 
     * @param id 停车记录主键
     * @return 停车记录
     */
    public ParkingRecord selectParkingRecordById(Long id);
    public ParkingRecord selectParkingRecordById1(Long id);
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
     * 批量删除停车记录
     * 
     * @param ids 需要删除的停车记录主键集合
     * @return 结果
     */
    public int deleteParkingRecordByIds(Long[] ids);

    /**
     * 删除停车记录信息
     * 
     * @param id 停车记录主键
     * @return 结果
     */
    public int deleteParkingRecordById(Long id);
    //通过停车场id车牌号查询有无未支付订单
    ParkingRecord findByLicense(String license,Long parkingLotInformationId);

    //通过停车场id,车牌号，未支付状态查询出来修改状态


    ParkingRecord findByLicense1(String license, Long id);

    ParkingRecord findByParkingLotInformationLicense1(Long id, String license);

    ParkingRecord findByParkingLotInformationLicenseAndPayOrder(Long id, String license);

    //查询指定停车场最近离场记录
    List<ParkingRecord> getPayRecord(Long id);

    AjaxResult updateToRecord(Long id);

    AjaxResult updateToRecordFromCoupon(Long id);

    AjaxResult noLicensePlate(Long parkinglotequipmentid, String license,String openid);

    AjaxResult editPayState(ParkingRecordVo parkingRecordVo);

    AjaxResult echoInformationToLicense(Long parkinglotequipmentid, String openid,String license);

    AjaxResult indoor(Long parkingLotInformationId, String license,String openid);


    AjaxResult indoorCallback(ParkingRecordVo parkingRecordVo);

    AjaxResult getMoney(Long id);

    AjaxResult getDailyInformation(Long id);

    ParkingRecord findbypaystateandlicense(Long parkinglotinformationid, String license);

    AjaxResult getByOpend(String openid,Long parkingLotInformationId);

    ParkingRecord todo(Long parkinglotinformationid, String license);

    List<ParkingRecord> findByFlee();

    ParkingRecord findByLicenseAndIndoorPayment(Long parkinglotinformationid,String license1);

    AjaxResult updateToRecordOne(Long id);

    AjaxResult updateToRecordFromCouponOne(Long id);

    AjaxResult editPayStateOne(ParkingRecordVo parkingRecordVo);

    Long selectParkingRecordList1(ParkingRecord parkingRecord);

    List<ParkingRecord> selectParkingRecordListOne(Long parkinglotinformationid);

    List<ParkingRecord> getPayRecord1(ParkingRecord parkingRecord);

    AjaxResult refreshstate(Long id);
}
