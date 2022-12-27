package com.ruoyi.parking.mapper;

import java.util.List;
import com.ruoyi.parking.domain.ParkingCoupon;
import org.apache.ibatis.annotations.Param;

/**
 * 优惠卷Mapper接口
 * 
 * @author ruoyi
 * @date 2022-12-12
 */
public interface ParkingCouponMapper 
{
    /**
     * 查询优惠卷
     * 
     * @param id 优惠卷主键
     * @return 优惠卷
     */
    public ParkingCoupon selectParkingCouponById(Long id);

    /**
     * 查询优惠卷列表
     * 
     * @param parkingCoupon 优惠卷
     * @return 优惠卷集合
     */
    public List<ParkingCoupon> selectParkingCouponList(ParkingCoupon parkingCoupon);

    /**
     * 新增优惠卷
     * 
     * @param parkingCoupon 优惠卷
     * @return 结果
     */
    public int insertParkingCoupon(ParkingCoupon parkingCoupon);

    /**
     * 修改优惠卷
     * 
     * @param parkingCoupon 优惠卷
     * @return 结果
     */
    public int updateParkingCoupon(ParkingCoupon parkingCoupon);

    /**
     * 删除优惠卷
     * 
     * @param id 优惠卷主键
     * @return 结果
     */
    public int deleteParkingCouponById(Long id);

    /**
     * 批量删除优惠卷
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteParkingCouponByIds(Long[] ids);
    /**
     * 查询全部发放的优惠卷总和
     * @param
     * @return
     */
    public Long selectAllCouponSum(@Param("parkingLotInformationId")Long parkingLotInformationId);

    /**
     * 查询全部过期的优惠卷数量
     * @param
     * @return
     */
    public Long selectAllExpiredCoupon(@Param("parkingLotInformationId")Long parkingLotInformationId);
}
