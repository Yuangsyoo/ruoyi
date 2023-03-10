package com.ruoyi.parking.mapper;

import java.util.List;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.parking.domain.ParkingRecord;
import com.ruoyi.parking.dto.MoneyDto;
import org.apache.ibatis.annotations.Param;

/**
 * 停车记录Mapper接口
 * 
 * @author ruoyi
 * @date 2022-11-24
 */
public interface ParkingRecordMapper {


   public  Long selectParkingRecordList1(ParkingRecord parkingRecord) ;

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


    ParkingRecord findByLicense1(@Param("license") String license,@Param("parkingLotInformationId") Long parkingLotInformationId);
   /* //通过车牌号，未支付状态查询出来修改状态
    ParkingRecord editPayState(String license);*/



    //通过停车场id，车牌号,当前时间（通过时间排序获取最近时间）获取出场车停车记录
    List<ParkingRecord> findByParkingLotInformationLicense1(@Param("parkingLotInformationId")Long id,@Param("license") String license);

    ParkingRecord findByParkingLotInformationLicenseAndPayOrder(@Param("parkingLotInformationId")Long id,@Param("license")String license);

    //查询指定停车场最近离场记录
    List<ParkingRecord> getPayRecord(Long id);

    List<MoneyDto> getMoney(@Param("parkingLotInformationId")Long id, @Param("year")String year);

    ParkingRecord findByOpenid(@Param("openid")String openid,@Param("parkingLotInformationId") Long parkinglotinformationid);

    Long getDailyInformation(@Param("parkingLotInformationId")Long id);

    Long getDailyInformations(@Param("parkingLotInformationId")Long id);

    ParkingRecord findbypaystateandlicense(@Param("parkingLotInformationId")Long parkinglotinformationid, @Param("license")String license);

    List<ParkingRecord> getByOpend(@Param("openid")String openid,@Param("parkingLotInformationId") Long parkinglotinformationid);

    ParkingRecord todo(@Param("parkingLotInformationId")Long parkinglotinformationid,  @Param("license")String license);

    List<ParkingRecord> findByFlee();

    ParkingRecord findByLicenseAndIndoorPayment(@Param("parkingLotInformationId")Long parkinglotinformationid,@Param("license")String license1);

    List<ParkingRecord> selectParkingRecordListOne(Long parkinglotinformationid);

    List<ParkingRecord> getPayRecord1(@Param("parkingLotInformationId")Long parkinglotinformationid, @Param("parkingeqid")Long parkingeqid);

}
