package com.ruoyi.parking.mapper;

import java.util.List;
import com.ruoyi.parking.domain.ParkingCouponrecord;
import org.apache.ibatis.annotations.Param;

/**
 * 停车场优惠卷记录Mapper接口
 * 
 * @author ruoyi
 * @date 2022-12-12
 */
public interface ParkingCouponrecordMapper 
{
    /**
     * 查询停车场优惠卷记录
     * 
     * @param id 停车场优惠卷记录主键
     * @return 停车场优惠卷记录
     */
    public ParkingCouponrecord selectParkingCouponrecordById(Long id);

    /**
     * 查询停车场优惠卷记录列表
     * 
     * @param parkingCouponrecord 停车场优惠卷记录
     * @return 停车场优惠卷记录集合
     */
    public List<ParkingCouponrecord> selectParkingCouponrecordList(ParkingCouponrecord parkingCouponrecord);

    /**
     * 新增停车场优惠卷记录
     * 
     * @param parkingCouponrecord 停车场优惠卷记录
     * @return 结果
     */
    public int insertParkingCouponrecord(ParkingCouponrecord parkingCouponrecord);

    /**
     * 修改停车场优惠卷记录
     * 
     * @param parkingCouponrecord 停车场优惠卷记录
     * @return 结果
     */
    public int updateParkingCouponrecord(ParkingCouponrecord parkingCouponrecord);

    /**
     * 删除停车场优惠卷记录
     * 
     * @param id 停车场优惠卷记录主键
     * @return 结果
     */
    public int deleteParkingCouponrecordById(Long id);

    /**
     * 批量删除停车场优惠卷记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteParkingCouponrecordByIds(Long[] ids);

    ParkingCouponrecord findParkingCouponrecord(@Param("parkingLotInformationId") Long parkingLotInformationId,
                                                @Param("parkingCouponId")Long parkingCouponId,
                                                @Param("license")String license);

    //<!--   判断有无优惠卷次卷 停车场id 车牌号  次卷  状态
    ParkingCouponrecord findByParkingLotInformationIdAndLicense(@Param("parkingLotInformationId")Long id, @Param("license")String license);

    //<!--   判断有无优惠卷次卷 停车场id 车牌号  次卷  状态
    ParkingCouponrecord findByParkingLotInformationIdAndLicenseTo(@Param("parkingLotInformationId")Long id, @Param("license")String license);

    List<ParkingCouponrecord> findByParkingLotInformationIdAndLicenseAndState(@Param("parkingLotInformationId")Long id,@Param("license") String license);
}
