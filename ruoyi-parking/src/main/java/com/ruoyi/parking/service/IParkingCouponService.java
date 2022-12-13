package com.ruoyi.parking.service;

import java.util.List;
import com.ruoyi.parking.domain.ParkingCoupon;

/**
 * 优惠卷Service接口
 * 
 * @author ruoyi
 * @date 2022-12-12
 */
public interface IParkingCouponService 
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
     * 批量删除优惠卷
     * 
     * @param ids 需要删除的优惠卷主键集合
     * @return 结果
     */
    public int deleteParkingCouponByIds(Long[] ids);

    /**
     * 删除优惠卷信息
     * 
     * @param id 优惠卷主键
     * @return 结果
     */
    public int deleteParkingCouponById(Long id);
}
