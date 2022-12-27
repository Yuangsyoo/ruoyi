package com.ruoyi.parking.service.impl;

import java.util.List;

import com.ruoyi.common.exception.GlobalException;
import com.ruoyi.common.utils.QRCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.parking.mapper.ParkingCouponMapper;
import com.ruoyi.parking.domain.ParkingCoupon;
import com.ruoyi.parking.service.IParkingCouponService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 优惠卷Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-12-12
 */
@Service
public class ParkingCouponServiceImpl implements IParkingCouponService 
{
    @Autowired
    private ParkingCouponMapper parkingCouponMapper;

    /**
     * 查询优惠卷
     * 
     * @param id 优惠卷主键
     * @return 优惠卷
     */
    @Override
    public ParkingCoupon selectParkingCouponById(Long id)
    {
        return parkingCouponMapper.selectParkingCouponById(id);
    }

    /**
     * 查询优惠卷列表
     * 
     * @param parkingCoupon 优惠卷
     * @return 优惠卷
     */
    @Override
    public List<ParkingCoupon> selectParkingCouponList(ParkingCoupon parkingCoupon)
    {
        return parkingCouponMapper.selectParkingCouponList(parkingCoupon);
    }

    /**
     * 新增优惠卷
     * 
     * @param parkingCoupon 优惠卷
     * @return 结果
     */
    @Override
    @Transactional
    public int insertParkingCoupon(ParkingCoupon parkingCoupon)
    {

        try {
            parkingCoupon.setState(0);
            int i = parkingCouponMapper.insertParkingCoupon(parkingCoupon);
            // TODO: 2022/12/12 待生成二维码 （停车场id，优惠卷id）QRCodeGenerator

            Long parkinglotinformationid = parkingCoupon.getParkinglotinformationid();
            Long parkingCouponId = parkingCoupon.getId();
            //    http://192.168.63.125/ui/institution/pageQueryForAssign?name='xxx'&sex='男'
            String text="https://www.baidu.com";//待修改
            String s = QRCodeGenerator.generateQRCodeImage(text, 360, 360);
            parkingCoupon.setQrcode(s);
            updateParkingCoupon(parkingCoupon);
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException("添加失败");
        }


    }

    /**
     * 修改优惠卷
     * 
     * @param parkingCoupon 优惠卷
     * @return 结果
     */
    @Override
    public int updateParkingCoupon(ParkingCoupon parkingCoupon)
    {
        return parkingCouponMapper.updateParkingCoupon(parkingCoupon);
    }

    /**
     * 批量删除优惠卷
     * 
     * @param ids 需要删除的优惠卷主键
     * @return 结果
     */
    @Override
    public int deleteParkingCouponByIds(Long[] ids)
    {
        return parkingCouponMapper.deleteParkingCouponByIds(ids);
    }

    /**
     * 删除优惠卷信息
     * 
     * @param id 优惠卷主键
     * @return 结果
     */
    @Override
    public int deleteParkingCouponById(Long id)
    {
        return parkingCouponMapper.deleteParkingCouponById(id);
    }

    /**
     * 求出发放优惠卷的总和数量
     * @return
     */
    @Override
    public Long sumAllCoupon(Long id){
        if (id!=0){
            return parkingCouponMapper.selectAllCouponSum(id);
        }
        return null;
    }



    /**
     * 查询所有过期的优惠卷的总和数量
     * @return
     */
    @Override
    public Long sumAllExpiredCoupon(Long id){
        if (id!=0){
            return parkingCouponMapper.selectAllExpiredCoupon(id);
        }
        return null;
    }

}
