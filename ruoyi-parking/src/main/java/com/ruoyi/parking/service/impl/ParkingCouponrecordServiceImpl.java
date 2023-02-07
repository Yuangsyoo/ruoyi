package com.ruoyi.parking.service.impl;

import java.util.Date;
import java.util.List;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.parking.domain.ParkingCoupon;
import com.ruoyi.parking.service.IParkingCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.parking.mapper.ParkingCouponrecordMapper;
import com.ruoyi.parking.domain.ParkingCouponrecord;
import com.ruoyi.parking.service.IParkingCouponrecordService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 停车场优惠卷记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-12-12
 */
@Service
public class ParkingCouponrecordServiceImpl implements IParkingCouponrecordService 
{
    @Autowired
    private ParkingCouponrecordMapper parkingCouponrecordMapper;
    @Autowired
    private IParkingCouponService parkingCouponService;
    /**
     * 查询停车场优惠卷记录
     * 
     * @param id 停车场优惠卷记录主键
     * @return 停车场优惠卷记录
     */
    @Override
    public ParkingCouponrecord selectParkingCouponrecordById(Long id)
    {
        return parkingCouponrecordMapper.selectParkingCouponrecordById(id);
    }

    /**
     * 查询停车场优惠卷记录列表
     * 
     * @param parkingCouponrecord 停车场优惠卷记录
     * @return 停车场优惠卷记录
     */
    @Override
    public List<ParkingCouponrecord> selectParkingCouponrecordList(ParkingCouponrecord parkingCouponrecord)
    {
        return parkingCouponrecordMapper.selectParkingCouponrecordList(parkingCouponrecord);
    }

    /**
     * 新增停车场优惠卷记录
     * 
     * @param parkingCouponrecord 停车场优惠卷记录
     * @return 结果
     */
    @Override
    public int insertParkingCouponrecord(ParkingCouponrecord parkingCouponrecord)
    {
        return parkingCouponrecordMapper.insertParkingCouponrecord(parkingCouponrecord);
    }

    /**
     * 修改停车场优惠卷记录
     * 
     * @param parkingCouponrecord 停车场优惠卷记录
     * @return 结果
     */
    @Override
    public int updateParkingCouponrecord(ParkingCouponrecord parkingCouponrecord)
    {
        return parkingCouponrecordMapper.updateParkingCouponrecord(parkingCouponrecord);
    }

    /**
     * 批量删除停车场优惠卷记录
     * 
     * @param ids 需要删除的停车场优惠卷记录主键
     * @return 结果
     */
    @Override
    public int deleteParkingCouponrecordByIds(Long[] ids)
    {
        return parkingCouponrecordMapper.deleteParkingCouponrecordByIds(ids);
    }

    /**
     * 删除停车场优惠卷记录信息
     * 
     * @param id 停车场优惠卷记录主键
     * @return 结果
     */
    @Override
    public int deleteParkingCouponrecordById(Long id)
    {
        return parkingCouponrecordMapper.deleteParkingCouponrecordById(id);
    }

    //领取优惠卷  修改优惠卷
    @Override
    @Transactional
    public AjaxResult add(Long parkingLotInformationId, Long parkingCouponId, String license) {
        ParkingCoupon parkingCoupon = parkingCouponService.selectParkingCouponById(parkingCouponId);
        if (parkingCoupon==null){
            return AjaxResult.error("无效优惠卷");
        }
        if (parkingCoupon.getState()==1){
            return AjaxResult.error("停车优惠卷已过期");
        }
        if (parkingCoupon.getCount()==parkingCoupon.getReceiveccount()){
            return AjaxResult.error("优惠卷领取数量不足");
        }
        //修改优惠卷领取数量
        parkingCoupon.setReceiveccount(parkingCoupon.getReceiveccount()+1);
        parkingCouponService.updateParkingCoupon(parkingCoupon);
        //添加优惠卷记录
        ParkingCouponrecord parkingCouponrecord = new ParkingCouponrecord();
        parkingCouponrecord.setParkinglotinformationid(parkingLotInformationId);
        parkingCouponrecord.setParkingcouponid(parkingCouponId);
        parkingCouponrecord.setLicense(license);
        parkingCouponrecord.setTime(new Date());
        parkingCouponrecord.setState("0");
        parkingCouponrecordMapper.insertParkingCouponrecord(parkingCouponrecord);
        return AjaxResult.success("领取成功");
    }

    @Override
    public ParkingCouponrecord findByParkingLotInformationIdAndLicense(Long id, String license) {
        return parkingCouponrecordMapper.findByParkingLotInformationIdAndLicense(id,license);
    }
    @Override
    public ParkingCouponrecord findByParkingLotInformationIdAndLicenseAndState(Long id, String license) {
        return parkingCouponrecordMapper.findByParkingLotInformationIdAndLicenseAndState(id,license);
    }
}
