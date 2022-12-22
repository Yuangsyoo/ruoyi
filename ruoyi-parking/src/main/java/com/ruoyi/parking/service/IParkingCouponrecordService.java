package com.ruoyi.parking.service;

import java.util.List;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.parking.domain.ParkingCouponrecord;

/**
 * 停车场优惠卷记录Service接口
 * 
 * @author ruoyi
 * @date 2022-12-12
 */
public interface IParkingCouponrecordService 
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
     * 批量删除停车场优惠卷记录
     * 
     * @param ids 需要删除的停车场优惠卷记录主键集合
     * @return 结果
     */
    public int deleteParkingCouponrecordByIds(Long[] ids);

    /**
     * 删除停车场优惠卷记录信息
     * 
     * @param id 停车场优惠卷记录主键
     * @return 结果
     */
    public int deleteParkingCouponrecordById(Long id);

    AjaxResult add(Long parkingLotInformationId, Long parkingCouponId, String license);
    // TODO: 2022/12/13  判断有无优惠卷次卷 停车场id 车牌号  次卷  状态
    ParkingCouponrecord findByParkingLotInformationIdAndLicense(Long id, String license);

    ParkingCouponrecord findByParkingLotInformationIdAndLicenseAndState(Long id, String license);
}
