package com.ruoyi.parking.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.ruoyi.common.core.domain.entity.ParkingLotInformation;
import com.ruoyi.common.utils.DateTime.DateTime;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.parking.domain.ParkingCoupon;
import com.ruoyi.parking.domain.ParkingCouponrecord;
import com.ruoyi.parking.vo.MoneyVo;
import com.ruoyi.parking.vo.ParkingChargingDto;
import com.ruoyi.parking.service.IParkingCouponService;
import com.ruoyi.parking.service.IParkingCouponrecordService;
import com.ruoyi.parking.service.IParkingLotInformationService;
import com.ruoyi.parking.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.parking.domain.ParkingBillingPeriod;
import com.ruoyi.parking.mapper.ParkingChargingMapper;
import com.ruoyi.parking.domain.ParkingCharging;
import com.ruoyi.parking.service.IParkingChargingService;

/**
 * 计费规则Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-12-19
 */
@Slf4j
@Service
public class ParkingChargingServiceImpl implements IParkingChargingService 
{
    @Autowired
    private ParkingChargingMapper parkingChargingMapper;
    @Autowired
    private IParkingLotInformationService parkingLotInformationService;
    @Autowired
    private IParkingCouponService parkingCouponService;
    @Autowired
    private IParkingCouponrecordService parkingCouponrecordService;

    /**
     * 查询计费规则
     * 
     * @param id 计费规则主键
     * @return 计费规则
     */
    @Override
    public ParkingCharging selectParkingChargingById(Long id)
    {
        return parkingChargingMapper.selectParkingChargingById(id);
    }

    /**
     * 查询计费规则列表
     * 
     * @param parkingCharging 计费规则
     * @return 计费规则
     */
    @Override
    public List<ParkingCharging> selectParkingChargingList(ParkingCharging parkingCharging)
    {
        return parkingChargingMapper.selectParkingChargingList(parkingCharging);
    }

    /**
     * 新增计费规则
     * 
     * @param parkingCharging 计费规则
     * @return 结果
     */
    @Transactional
    @Override
    public int insertParkingCharging(ParkingCharging parkingCharging) {
        ParkingCharging parkingCharging1=findByParkinglotinformationid(parkingCharging.getParkinglotinformationid(),parkingCharging.getDistinguish());
        if (parkingCharging1!=null){
            throw  new RuntimeException("停车场已添加计费规则,请勿重复添加");
        }
        //parkingCharging.setStartingpriceduration(60L);
        int rows = parkingChargingMapper.insertParkingCharging(parkingCharging);
        insertParkingBillingPeriod(parkingCharging);
        return rows;
    }

    private ParkingCharging findByParkinglotinformationid(Long parkinglotinformationid,Long distinguish) {
     return parkingChargingMapper.findByParkinglotinformationid(parkinglotinformationid,distinguish);
    }

    /**
     * 修改计费规则
     * 
     * @param parkingCharging 计费规则
     * @return 结果
     */
    @Transactional
    @Override
    public int updateParkingCharging(ParkingCharging parkingCharging) {

        parkingChargingMapper.deleteParkingBillingPeriodByParkingChargingId(parkingCharging.getId());
        insertParkingBillingPeriod(parkingCharging);

        return parkingChargingMapper.updateParkingCharging(parkingCharging);
    }

    /**
     * 批量删除计费规则
     * 
     * @param ids 需要删除的计费规则主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteParkingChargingByIds(Long[] ids) {
        parkingChargingMapper.deleteParkingBillingPeriodByParkingChargingIds(ids);
        return parkingChargingMapper.deleteParkingChargingByIds(ids);
    }

    /**
     * 删除计费规则信息
     * 
     * @param id 计费规则主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteParkingChargingById(Long id) {
        parkingChargingMapper.deleteParkingBillingPeriodByParkingChargingId(id);
        return parkingChargingMapper.deleteParkingChargingById(id);
    }
    //超时补费
    @Override
    public MoneyVo overtimeCompensation(ParkingChargingDto parkingChargingDto) {
        //离场时间
        Date endTime = parkingChargingDto.getEndTime();
        //支付时间
        Date startTime = parkingChargingDto.getStartTime();
        //停车场id
        Long id = parkingChargingDto.getParkinglotinformationid();
        //车牌
        String license = parkingChargingDto.getLicense();
        //超时好多分钟
        long l = DateTime.dateDiff(startTime, endTime);
        //停车超时几小时
        Long aLong = totalDuration(l);
        //停车场计费规则
        ParkingCharging parkingCharging = parkingChargingMapper.findByParkinglotinformationid(id,parkingChargingDto.getDistinguish());
        //没超过一小时加收好多
        Long increaseincome = parkingCharging.getIncreaseincome();
        //超时总费用
        long l1 = aLong * increaseincome;
        MoneyVo moneyVo = new MoneyVo(0L,l1,l1,null);
       return moneyVo;

    }

    /**
     * 新增计费时间段信息
     * 
     * @param parkingCharging 计费规则对象
     */
    public void insertParkingBillingPeriod(ParkingCharging parkingCharging) {
        List<ParkingBillingPeriod> parkingBillingPeriodList = parkingCharging.getParkingBillingPeriodList();
        if (parkingCharging.getType()==0 || parkingCharging.getType()==1){
           if (parkingBillingPeriodList.size()>24){
               throw  new RuntimeException("计费时间段最多添加24条");
           }
        }
        if (parkingCharging.getType()==2){
            if (parkingBillingPeriodList.size()==0){
                throw new RuntimeException("请添加计费时间段");
            }
            if (parkingBillingPeriodList.size()>5){
                throw  new RuntimeException("计费时间段最多添加5条");
            }
        }

        Long id = parkingCharging.getId();
        if (StringUtils.isNotNull(parkingBillingPeriodList)) {
            List<ParkingBillingPeriod> list = new ArrayList<ParkingBillingPeriod>();
            for (ParkingBillingPeriod parkingBillingPeriod : parkingBillingPeriodList) {
                    parkingBillingPeriod.setParkingchargingid(id);
                    list.add(parkingBillingPeriod);
            }
            if (list.size() > 0) {
                parkingChargingMapper.batchParkingBillingPeriod(list);
            }
        }
    }

    /**
     * 计算金额根据计算规则
     */
    public MoneyVo calculatedAmount(ParkingChargingDto parkingChargingDto) {

        ParkingCharging parkingCharging = parkingChargingMapper.findByParkinglotinformationid(parkingChargingDto.getParkinglotinformationid(),parkingChargingDto.getDistinguish());
        //常规+时段
        if (parkingCharging.getType()==0){
            MoneyVo moneyVo = ruleOne(parkingChargingDto);
            log.info("计费规则一 车牌【"+parkingChargingDto.getLicense()+"】支付金额"+moneyVo.getMoney()+"元");
            return moneyVo;
        }
        //时长叠加
        if (parkingCharging.getType()==1){
            MoneyVo moneyVo = MoneyVoTwo(parkingChargingDto);
            log.info("计费规则二 车牌【"+parkingChargingDto.getLicense()+"】支付金额"+moneyVo.getMoney()+"元");
            return moneyVo;
        }
        //分时段不同计费规则
        if (parkingCharging.getType()==2){
            MoneyVo moneyVo = ruleThree(parkingChargingDto);
            log.info("计费规则三 车牌【"+parkingChargingDto.getLicense()+"】支付金额"+moneyVo.getMoney()+"元");
            return moneyVo;
        }
            return null;
    }
    //计费规则一%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    public MoneyVo ruleOne(ParkingChargingDto parkingChargingDto) {
        Long parkinglotinformationid = parkingChargingDto.getParkinglotinformationid();
        ParkingLotInformation parkingLotInformation = parkingLotInformationService.selectParkingLotInformationById(parkinglotinformationid);
        //计费规则
        ParkingCharging parkingCharging = parkingChargingMapper.findByParkinglotinformationid(parkingChargingDto.getParkinglotinformationid(),parkingChargingDto.getDistinguish());
        List<ParkingBillingPeriod> list = parkingCharging.getParkingBillingPeriodList();
        //查询优惠卷 停车场id,车牌号码，状态为0
        ParkingCouponrecord parkingCouponrecord = parkingCouponrecordService.findByParkingLotInformationIdAndLicenseAndState(parkinglotinformationid, parkingChargingDto.getLicense());

        //免费时长
        Long freetime = parkingCharging.getFreetime();
        //停车时长分钟
        long time = DateTime.dateDiff(parkingChargingDto.getStartTime(), parkingChargingDto.getEndTime());
        //起步时长（单位分钟）
        Long startingpriceduration = parkingCharging.getStartingpriceduration();
        //起步价
        Long startingprice = parkingCharging.getStartingprice();
        //单日上限金额
        Long superiorlimit = parkingCharging.getSuperiorlimit();
        //每小时加收
        Long increaseincome = parkingCharging.getIncreaseincome();
        //停车总时长小时
        Long aLong = totalDuration(time);


        //判断时长
        if(freetime>time){
            //金额为0
            MoneyVo moneyVo = getMoneyVo(0L,0L,0L,"免费");
            return moneyVo;
        }
        //如果有计费时间段
      /*  if (list.size()>0){
        }*/
        //如果没有计费时间段
    /*    if (list.size()==0){*/

            //判断停车时长小于等于起步时间 小于等于直接返回起步价
            if (time<=startingpriceduration){

                //有优惠卷
                if (parkingCouponrecord!=null){
                    //判断是优惠卷 0是次卷
                    if ( parkingCouponrecord.getParkingCoupon()!=null){
                        if ( parkingCouponrecord.getParkingCoupon().getType()==0){
                            return getMoneyVo(0L,parkingCharging.getStartingprice(),parkingCharging.getStartingprice(),"次卷抵扣");
                        }
                    }
                    //判断是优惠卷 1是小时卷  如果停车时间和优惠卷时间相等或者小于 直接免费
                    if ( parkingCouponrecord.getParkingCoupon().getType()==1){
                        //优惠卷优惠时间
                        long l =  parkingCouponrecord.getParkingCoupon().getPreferentialfacevalue()*60;
                        // 起步时长（60倍数）小于优惠时长
                        if (time<=l){
                            return getMoneyVo(0L,parkingCharging.getStartingprice(),parkingCharging.getStartingprice(),"小时卷抵扣");
                        }
                        // 起步时长（60倍数）大于优惠时长 直接算起步价
                        else {
                            //起步价
                            Long price = parkingCharging.getStartingprice();
                            return getMoneyVo(price,price,0L,"小时卷抵扣");
                        }
                    }
                    // 2是金额卷
                    if ( parkingCouponrecord.getParkingCoupon().getType()==2){
                        //起步价和金额优惠卷面值差值
                        long l = parkingCharging.getStartingprice() -  parkingCouponrecord.getParkingCoupon().getPreferentialfacevalue();
                        if(l<=0){
                            return getMoneyVo(0L,parkingCharging.getStartingprice(),parkingCharging.getStartingprice(),"金额卷抵扣");
                        }
                        //为0就是金额卷抵扣完 不为0就是比如：微信加金额卷
                        return getMoneyVo(l,parkingCharging.getStartingprice(),Long.valueOf( parkingCouponrecord.getParkingCoupon().getPreferentialfacevalue()),"金额卷抵扣");
                    }
                }
                return getMoneyVo(parkingCharging.getStartingprice(),0L,0L,null);
            }

            //判断停车时长大于起步时间
            //停车时长小于24小时
            if (aLong<24){
                //总金额
                MoneyVo moneyVo = addMoneyVo(parkingCharging, startingpriceduration, startingprice, superiorlimit, aLong);
                if (parkingCouponrecord!=null){
                    //判断是优惠卷 0是次卷
                    if (parkingCouponrecord.getParkingCoupon()!=null){
                        if (parkingCouponrecord.getParkingCoupon().getType()==0){
                            return getMoneyVo(0L,moneyVo.getMoney(),moneyVo.getMoney(),"次卷抵扣");
                        }
                    }
                    //判断是优惠卷 1是小时卷
                    if (parkingCouponrecord.getParkingCoupon().getType()==1){
                        //优惠卷优惠时间  判断优惠后时长小于等于停车时长 （小时）
                        long l = parkingCouponrecord.getParkingCoupon().getPreferentialfacevalue();
                        //减去优惠后的时间 负数或者0  大于0小于等于起步时间  大于起步时间
                        long l1 = aLong - l;

                        if (l1<=0){
                            return getMoneyVo(0L, moneyVo.getAmountpayable(), moneyVo.getAmountpayable(),"小时卷");
                        }
                        // 大于0小于等于起步时间
                        long l2 = startingpriceduration / 60;
                        if (0 < l1 && l1<= l2){
                            return getMoneyVo(startingprice, moneyVo.getAmountpayable(),moneyVo.getAmountpayable()-startingprice,"小时卷");
                        }
                        if (l1>l2){
                            return addMoneyVo(parkingCharging, startingpriceduration, startingprice, superiorlimit, aLong-l);
                        }
                    }
                    // 2是金额卷
                    if (parkingCouponrecord.getParkingCoupon().getType()==2){
                        long l =moneyVo.getMoney()- parkingCouponrecord.getParkingCoupon().getPreferentialfacevalue();
                        if(l<=0){
                            return getMoneyVo(0L,moneyVo.getMoney(),parkingCharging.getStartingprice(),"金额卷抵扣");
                        }
                        //为0就是金额卷抵扣完 不为0就是比如：微信加金额卷
                        return getMoneyVo(l,moneyVo.getMoney(),Long.valueOf(parkingCouponrecord.getParkingCoupon().getPreferentialfacevalue()),"金额卷抵扣");
                    }
                }
                return moneyVo;

            }
            if (aLong==24){
                if (parkingCouponrecord!=null) {
                    //判断是优惠卷 0是次卷
                    if (parkingCouponrecord.getParkingCoupon()!=null){
                        if (parkingCouponrecord.getParkingCoupon().getType()==0){
                            return getMoneyVo(0L,parkingCharging.getStartingprice(),parkingCharging.getStartingprice(),"次卷抵扣");
                        }
                    }
                    //有小时卷
                    if (parkingCouponrecord.getParkingCoupon().getType() == 1) {
                        //优惠卷优惠时间  判断优惠后时长小于等于停车时长 （小时）
                        long l = parkingCouponrecord.getParkingCoupon().getPreferentialfacevalue();
                        //不算优惠的总金额  单日收费上限
                        Long money = parkingCharging.getSuperiorlimit();
                        //减去优惠后的时间 负数或者0  大于0小于等于起步时间  大于起步时间
                        long l1 = aLong - l;
                        if (l1 <= 0) {
                            return getMoneyVo(0L, money, money, "小时卷");
                        }
                        // 大于0小于等于起步时间
                        long l2 = startingpriceduration / 60;
                        if (0 < l1 && l1 <= l2) {
                            return getMoneyVo(startingprice, money, money - startingprice, "小时卷");
                        }
                        if (l1 > l2) {
                            return addMoneyVo(parkingCharging, startingpriceduration, startingprice, superiorlimit, aLong - l);
                        }
                    }
                    //有金额卷
                    if (parkingCouponrecord.getParkingCoupon().getType() == 2) {
                        //单日收费上限与优惠金额差<=0,大于0
                        long l = superiorlimit - parkingCouponrecord.getParkingCoupon().getPreferentialfacevalue();
                        //优惠卷面值
                        Long c = Long.valueOf(parkingCouponrecord.getParkingCoupon().getPreferentialfacevalue());
                        if (l <= 0) {
                            return getMoneyVo(0L, superiorlimit, c, "金额卷抵扣");
                        }
                        if (l > 0) {
                            return getMoneyVo(l, superiorlimit, c, "金额卷抵扣");
                        }
                    }
                }
                //刚好24小时 无优惠卷 直接单日收费上限
                return getMoneyVo(superiorlimit,superiorlimit,0L,null);
            }
            if (aLong>24){
                //取余
                long l = aLong % 24;
                Long l2=0L;
                //判断余数
                if (l==0){
                    long l1 = aLong / 24;
                    //总金额
                    l2 = l1 * superiorlimit;
                }else {
                    long l1 = aLong / 24;
                    //每小时加收
                    long l22 = increaseincome * l;
                    if (l22>=superiorlimit){
                        l22=superiorlimit;
                    }
                    //总金额
                    l2 = l1 * superiorlimit + l22;
                }
                if (parkingCouponrecord!=null) {
                    //判断是优惠卷 0是次卷
                    if (parkingCouponrecord.getParkingCoupon()!=null){
                        if (parkingCouponrecord.getParkingCoupon().getType()==0){
                            return getMoneyVo(0L,l2,l2,"次卷抵扣");
                        }
                    }
                    //优惠面值
                    Long preferentialfacevalue = Long.valueOf(parkingCouponrecord.getParkingCoupon().getPreferentialfacevalue());
                    //有小时卷
                    if (parkingCouponrecord.getParkingCoupon().getType() == 1) {
                        //优惠后时长
                        long l3 = aLong - preferentialfacevalue;
                        if (l3 <= 0) {
                            return getMoneyVo(0L, l2, l2, "小时卷抵扣");
                        }
                        //判断停车时长小于等于起步时间 小于等于直接返回起步价
                        if (l3 > 0 && l3 <= startingpriceduration/60) {
                            return getMoneyVo(parkingCharging.getStartingprice(), l2,  l2-parkingCharging.getStartingprice(), "小时卷抵扣");
                        }
                        if (startingpriceduration/60<l3 &&l3 < 24) {
                            //减去起步时长
                            long a = l3 - startingpriceduration / 60; //(小时)
                            //加收金额
                            long l1 = parkingCharging.getIncreaseincome() * a;
                            //总金额
                            long a2= startingprice + l1;
                            //判断单日收费上限
                            if (a2< superiorlimit){
                                return getMoneyVo(a2,l2,l2-a2,"小时卷抵扣");
                            }else {
                                return getMoneyVo(superiorlimit, l2,l2-superiorlimit,"小时卷抵扣");
                            }
                        }
                        if (l3 == 24) {
                            //刚好24小时 无优惠卷 直接单日收费上限
                            return getMoneyVo(superiorlimit, l2, l2 - superiorlimit, "小时卷抵扣");
                        }
                        if (l3 > 24) {
                            long l6 = 0L;
                            //取余
                            long l4 = l3 % 24;
                            //判断余数
                            if (l4 == 0) {
                                long l5 = aLong / 24;
                                //总金额
                                l6 = l5 * superiorlimit;
                            }
                            if (l4 != 0) {
                                long l7 = l3 / 24;

                                //每小时加收
                                long l5 = increaseincome * l4;
                                if (l5 >= superiorlimit) {
                                    l5 = superiorlimit;
                                }
                                //总金额
                                l6 = l7 * superiorlimit + l5;
                            }
                            return getMoneyVo(l6, l2, l2 - l6, "小时卷抵扣");
                        }
                    }
                    //有金额卷
                    if (parkingCouponrecord.getParkingCoupon().getType() == 2) {
                        //优惠后金额
                        long l3 = l2 - preferentialfacevalue;
                        if (l3 <= 0) {
                            return getMoneyVo(0L, l2, preferentialfacevalue, "金额卷抵扣");
                        }
                        return getMoneyVo(l3, l2, preferentialfacevalue, "金额卷抵扣");
                    }
                }
                //没有优惠总金额
                return getMoneyVo(l2,l2,0L,null);
            }

      // }

        return new MoneyVo();
    }
    //计费方式一时长小于24小时 金额
    private MoneyVo addMoneyVo(ParkingCharging parkingCharging, Long startingpriceduration, Long startingprice, Long superiorlimit, Long aLong) {
        //减去起步时长
        long l = aLong - startingpriceduration / 60; //(小时)
        //加收金额
        long l1 = parkingCharging.getIncreaseincome() * l;
        //总金额
        long l2 = startingprice + l1;
        //判断单日收费上限
        if (l2< superiorlimit){
            return getMoneyVo(l2,l2,0L,null);
        }else {
            return getMoneyVo(superiorlimit, superiorlimit,0L,null);
        }
    }

    //计费规则三%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    private MoneyVo ruleThree(ParkingChargingDto parkingChargingDto) {
        long time = DateTime.dateDiff(parkingChargingDto.getStartTime(), parkingChargingDto.getEndTime());
        MoneyVo three = Three(parkingChargingDto);
        Long parkinglotinformationid = parkingChargingDto.getParkinglotinformationid();
        //查询优惠卷 停车场id,车牌号码，状态不为1且不为2
        ParkingCouponrecord parkingCouponrecord = parkingCouponrecordService.findByParkingLotInformationIdAndLicenseAndState(parkinglotinformationid, parkingChargingDto.getLicense());
        if (parkingCouponrecord!=null){
            //判断是优惠卷 0是次卷
        if (parkingCouponrecord.getParkingCoupon().getType()==0){
            return getMoneyVo(0L,three.getMoney(),three.getMoney(),"次卷抵扣");
        }
        //判断是优惠卷 1是小时卷  如果停车时间和优惠卷时间相等或者小于 直接免费
        if (parkingCouponrecord.getParkingCoupon().getType()==1){
            //优惠卷优惠时间
            long l = parkingCouponrecord.getParkingCoupon().getPreferentialfacevalue()*60;
            // 起步时长（60倍数）小于优惠时长
            if (time<=l){
                return getMoneyVo(0L,three.getMoney(),three.getMoney(),"小时卷抵扣");
            }
            else {
                Date endTime = parkingChargingDto.getEndTime();
                long l1 = endTime.getTime() - l * 60 * 1000;
                Date date = new Date(l1);
                parkingChargingDto.setEndTime(date);
                MoneyVo three1 = Three(parkingChargingDto);
                return getMoneyVo(three1.getMoney(),three.getMoney(),three.getMoney()-three1.getMoney(),"小时卷抵扣");
            }
        }

        // 2是金额卷
        if (parkingCouponrecord.getParkingCoupon().getType()==2){
            long l =three.getMoney()- parkingCouponrecord.getParkingCoupon().getPreferentialfacevalue();
            if(l<=0){
                return getMoneyVo(0L,three.getMoney(),three.getMoney(),"金额卷抵扣");
            }
            //为0就是金额卷抵扣完 不为0就是比如：微信加金额卷
            return getMoneyVo(l,three.getMoney(),Long.valueOf(parkingCouponrecord.getParkingCoupon().getPreferentialfacevalue()),"金额卷抵扣");
        }
        }
        return three;
    }
    private MoneyVo Three(ParkingChargingDto parkingChargingDto) {
        Long parkinglotinformationid = parkingChargingDto.getParkinglotinformationid();
        ParkingLotInformation parkingLotInformation = parkingLotInformationService.selectParkingLotInformationById(parkinglotinformationid);
        //计费规则
        ParkingCharging parkingCharging = parkingChargingMapper.findByParkinglotinformationid(parkingChargingDto.getParkinglotinformationid(),parkingChargingDto.getDistinguish());
        List<ParkingBillingPeriod> list = parkingCharging.getParkingBillingPeriodList();

        //免费时长
        Long freetime = parkingCharging.getFreetime();

        //停车时长分钟
        long time = DateTime.dateDiff(parkingChargingDto.getStartTime(), parkingChargingDto.getEndTime());
        //停车总时长小时
        Long aLong = totalDuration(time);
        //循环计费时间段
        if (time<freetime ) {
            //金额为0
            MoneyVo moneyVo = getMoneyVo(0L,0L,0L,"免费");
            return moneyVo;
        }
        if (aLong<24){
            //停车开始时间
            Date startTime = parkingChargingDto.getStartTime();
            //停车结束时间
            Date endTime = parkingChargingDto.getEndTime();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startTime);
            Calendar instance = Calendar.getInstance();
            instance.setTime(endTime);
            int i = calendar.get(Calendar.DATE);
            int i1 = instance.get(Calendar.DATE);
            //不同一天
            if (i!=i1){
                Date date = getDate(startTime, "23:59:59");
                Long aLong1 = totalDuration(DateTime.dateDiff(startTime, date));
                //两段计费合计总金额
                MoneyVo money = getMoney(list, aLong1, startTime, date);
                Date date1 = getDate(endTime, "00:00:01");
                Long aLong2 = totalDuration(DateTime.dateDiff(date1, endTime));
                MoneyVo money1 = getMoney(list, aLong2, date1, endTime);
                long l = money.getMoney() + money1.getMoney();
                return getMoneyVo(l,l,0L,null);
            }
            //是同一天
            else {
                return getMoney(list, aLong, startTime, endTime);
            }

        }
        if (aLong>=24){
            //一天总价
            Long count=0L;
            for (ParkingBillingPeriod parkingBillingPeriod : list) {
                String startime = parkingBillingPeriod.getStartime();
                String endtime = parkingBillingPeriod.getEndtime();
                Long aLong1 = Long.valueOf(startime.split(":")[0]);
                Long aLong2 = Long.valueOf(endtime.split(":")[0]);
                long l = ((aLong2 - aLong1) - (parkingBillingPeriod.getStartingtime())/60) * parkingBillingPeriod.getAddmoney() + parkingBillingPeriod.getStartingprice();
                if (l>=parkingBillingPeriod.getSuperiorlimit()){
                    l=parkingBillingPeriod.getSuperiorlimit();
                }
                count=count+l;
            }
            long l = aLong / 24;
            //减去整天剩余小时数
            long l2 = aLong % 24;
            long l1 = count * l;
            if (l2==0){
               return getMoneyVo(l1,l1,0L,null);
            }else {
                Date endTime = parkingChargingDto.getEndTime();
                long l3 = endTime.getTime() - l2 * 60 * 60 * 1000;
                Date date = new Date(l3);
                //停车时长分钟
                long l4 = DateTime.dateDiff(date, endTime);
                //停车总时长小时
                Long aLong1 = totalDuration(l4);
                MoneyVo money = getMoney(list, aLong1, date, endTime);

                return getMoneyVo(l1+money.getMoney(),l1+money.getMoney(),0L,null);
            }
        }
        log.info("规则三出现bug");
        return null;
    }
    //规则三同一天业务逻辑
    private MoneyVo getMoney(List<ParkingBillingPeriod> list, Long aLong, Date startTime, Date endTime) {
        //判断是哪个时间段上
        int start=0;
        Long type=null;
        int end=0;
        Long type1=null;

        for(int a = 0; a< list.size(); a++) {
            ParkingBillingPeriod parkingBillingPeriod = list.get(a);
            //时间段开始时间
            String startime = parkingBillingPeriod.getStartime();
            //时间段结束时间
            String endtime = parkingBillingPeriod.getEndtime();
            //判断是否在时间段内
            ResultVo judge = judge(startTime, startime, endtime);
            if (judge.getState()){
                start=a;
                type=judge.getType();
                for(int a1 = 0; a1< list.size(); a1++) {
                    ParkingBillingPeriod parkingBillingPeriod1 = list.get(a1);
                    ResultVo judge1 = judge(endTime, parkingBillingPeriod1.getStartime(), parkingBillingPeriod1.getEndtime());

                        if (judge1.getType()!=2){
                            end=a1;
                            type1=judge1.getType();
                            break;
                        }
                      if (a1==list.size()-1&&judge1.getType()==2){
                          end=a1;
                          type1=judge1.getType();
                          break;
                      }

                }
                break;
            }
        }
        if (start==end ){
            //都在第一段计费开始时间前
            if (start==0 && type1==0){
                //没在计费时间段内
                return getMoneyVo(0L,0L,0L,"免费");
            }
            //都在最后一段计费结束时间后
            if(start==list.size()-1&&type==2&&type1==2){
                //没在计费时间段内
                return getMoneyVo(0L,0L,0L,"免费");
            }

            ParkingBillingPeriod parkingBillingPeriod = list.get(start);
            //时段开始时间
            String startime = parkingBillingPeriod.getStartime();
            //时段结束时间
            String endtime = parkingBillingPeriod.getEndtime();
            //收费上限
            Long superiorlimit = parkingBillingPeriod.getSuperiorlimit();
            //起步时长(分钟单位）
            Long startingtime = parkingBillingPeriod.getStartingtime();
            //起步价
            Long startingprice = parkingBillingPeriod.getStartingprice();
            //每小时加收
            Long addmoney = parkingBillingPeriod.getAddmoney();
            //在时段开始时间前
            if (type==0){
                //停车结束时间在时段开始时间前
                if (type1==0){
                    return getMoneyVo(0L,0L,0L,null);
                }
                //停车结束时间在时段结束时间前
                if (type1==1){
                    //时间段开始时间
                    Date date = getDate(startTime, startime);
                    //停车结束时间
                    long l = DateTime.dateDiff(date, endTime);
                    //停车时长（小时）
                    Long aLong1 = totalDuration(l);

                    if (aLong1<startingtime/60){
                        return getMoneyVo(startingprice,startingprice,0L,null);
                    }else {
                        //减去起步时长剩余时间
                        long l1 = aLong1 - startingtime / 60;
                        //总价
                        long l2 = l1 * addmoney + startingprice;
                        if (l2>=superiorlimit){
                            return getMoneyVo(superiorlimit,superiorlimit,0L,null);
                        }else {
                            return getMoneyVo(l2,l2,0L,null);
                        }
                    }

                }
                if (type1==2){
                    //时间段开始时间
                    Date date = getDate(startTime, startime);
                    Date date1 = getDate(startTime, endtime);
                    //停车结束时间
                    long l = DateTime.dateDiff(date, date1);
                    //停车时长（小时）
                    Long aLong1 = totalDuration(l);

                    if (aLong1<startingtime/60){
                        return getMoneyVo(startingprice,startingprice,0L,null);
                    }else {
                        //减去起步时长剩余时间
                        long l1 = aLong1 - startingtime / 60;
                        //总价
                        long l2 = l1 * addmoney + startingprice;
                        if (l2>=superiorlimit){
                            return getMoneyVo(superiorlimit,superiorlimit,0L,null);
                        }else {
                            return getMoneyVo(l2,l2,0L,null);
                        }
                    }
                }
            }
            //在时段结束时间前
            if (type==1){
                if (type1==1) {
                    if (aLong < startingtime / 60) {
                        return getMoneyVo(startingprice, startingprice, 0L, null);
                    } else {
                        //减去起步时长剩余时间
                        long l1 = aLong - startingtime / 60;
                        //总价
                        long l2 = l1 * addmoney + startingprice;
                        if (l2 >= superiorlimit) {
                            return getMoneyVo(superiorlimit, superiorlimit, 0L, null);
                        } else {
                            return getMoneyVo(l2, l2, 0L, null);
                        }
                    }
                }
                if (type1==2){
                    //时间段开始时间
                    Date date = getDate(endTime, endtime);
                    //停车结束时间
                    long l = DateTime.dateDiff(startTime, date);
                    //停车时长（小时）
                    Long aLong1 = totalDuration(l);

                    if (aLong1<startingtime/60){
                        return getMoneyVo(startingprice,startingprice,0L,null);
                    }else {
                        //减去起步时长剩余时间
                        long l1 = aLong1 - startingtime / 60;
                        //总价
                        long l2 = l1 * addmoney + startingprice;
                        if (l2>=superiorlimit){
                            return getMoneyVo(superiorlimit,superiorlimit,0L,null);
                        }else {
                            return getMoneyVo(l2,l2,0L,null);
                        }
                    }
                }
            }
        }
            Long count=0L;
            for (int aa=start+1;aa<end;aa++){
                ParkingBillingPeriod parkingBillingPeriod = list.get(aa);
                String startime = parkingBillingPeriod.getStartime();
                String endtime = parkingBillingPeriod.getEndtime();
                Long aLong1 = Long.valueOf(startime.split(":")[0]);
                Long aLong2 = Long.valueOf(endtime.split(":")[0]);
                long l = ((aLong2 - aLong1) - parkingBillingPeriod.getStartingtime() / 60) * parkingBillingPeriod.getAddmoney() + parkingBillingPeriod.getStartingprice();
                if (l>=parkingBillingPeriod.getSuperiorlimit()){
                    l=parkingBillingPeriod.getSuperiorlimit();
                }
                count=count+l;
            }
            //开始时间在某个时间段内
            ParkingBillingPeriod parkingBillingPeriod = list.get(start);
            //结束时间在某个时间段内
            ParkingBillingPeriod parkingBillingPeriod1 = list.get(end);

            //开始时间在时段开始时间前
            if (type==0){
                String startime = parkingBillingPeriod.getStartime();
                String endtime = parkingBillingPeriod.getEndtime();
                Long aLong1 = Long.valueOf(startime.split(":")[0]);
                Long aLong2 = Long.valueOf(endtime.split(":")[0]);
                long l = ((aLong2 - aLong1) - parkingBillingPeriod.getStartingtime() / 60) * parkingBillingPeriod.getAddmoney() + parkingBillingPeriod.getStartingprice();
                if (l>=parkingBillingPeriod.getSuperiorlimit()){
                    l=parkingBillingPeriod.getSuperiorlimit();
                }
               count=count+l;
            }
            //开始时间在时段结束时间前
            if (type==1){
                //时段结束时间
                Date date = getDate1(startTime, parkingBillingPeriod.getEndtime());
                //在这个时间段内停车时间（分钟）
                long l = DateTime.dateDiff(startTime, date);
                if (l<=parkingBillingPeriod.getStartingtime()){
                    count=count+parkingBillingPeriod.getStartingprice();
                }else {
                    //在这个时间段内停车时间（小时）
                    Long aLong1 = totalDuration(l);
                    Long startingtime = parkingBillingPeriod.getStartingtime();
                    Long startingprice = parkingBillingPeriod.getStartingprice();
                    Long addmoney = parkingBillingPeriod.getAddmoney();
                    long l1 = (aLong1 - startingtime/60) * addmoney + startingprice;
                    if (l1>=parkingBillingPeriod.getSuperiorlimit()){
                        count=count+parkingBillingPeriod.getSuperiorlimit();
                    }else {
                        count=count+l1;
                    }
                }
            }
            if (type1==0){
                count=count+0;
            }
            if (type1==1){
                String startime = parkingBillingPeriod1.getStartime();
                //这个时间段开始时间
                Date date = getDate2(endTime, startime);
                //在这个时间段内停车时间（分钟）
                long l = DateTime.dateDiff( date,endTime);
                if (l<=parkingBillingPeriod1.getStartingtime()){
                    count=count+parkingBillingPeriod1.getStartingprice();
                }else {
                    Long aLong1 = totalDuration(l);
                    Long startingtime = parkingBillingPeriod1.getStartingtime();
                    Long startingprice = parkingBillingPeriod1.getStartingprice();
                    Long addmoney = parkingBillingPeriod1.getAddmoney();
                    long l1 = (aLong1 - startingtime/60) * addmoney + startingprice;
                    if (l1>=parkingBillingPeriod1.getSuperiorlimit()){
                        count=count+parkingBillingPeriod1.getSuperiorlimit();
                    }else {
                        count=count+l1;
                    }
                }
            }
            if (type1==2){
                String startime = parkingBillingPeriod1.getStartime();
                String endtime = parkingBillingPeriod1.getEndtime();
                //这个时间段开始时间
                Date date = getDate2(endTime, startime);
                Date date2 = getDate2(endTime, endtime);
                //在这个时间段内停车时间（分钟）
                long l = DateTime.dateDiff( date,date2);
                if (l<=parkingBillingPeriod1.getStartingtime()){
                    count=count+parkingBillingPeriod1.getStartingprice();
                }else {
                    Long aLong1 = totalDuration(l);
                    Long startingtime = parkingBillingPeriod1.getStartingtime();
                    Long startingprice = parkingBillingPeriod1.getStartingprice();
                    Long addmoney = parkingBillingPeriod1.getAddmoney();
                    long l1 = (aLong1 - startingtime/60) * addmoney + startingprice;
                    if (l1>=parkingBillingPeriod1.getSuperiorlimit()){
                        count=count+parkingBillingPeriod1.getSuperiorlimit();
                    }else {
                        count=count+l1;
                    }
                }
            }

                return getMoneyVo(count,count,0L,null);
    }
    //规则三工具类
    private ResultVo judge(Date time,String startTime,String endTime) {
        //需要判断的时间
        Calendar date = Calendar.getInstance();
        date.setTime(time);
        System.out.println(date.getTime());
        //开始时间
        Calendar begin = Calendar.getInstance();
        // begin.add(Calendar.DATE, 1);
        String[] split = startTime.split(":");
        System.out.println(split);
        begin.setTime(time);
        begin.set(Calendar.HOUR_OF_DAY, Integer.valueOf(split[0]));

        begin.set(Calendar.MINUTE, 0);

        begin.set(Calendar.SECOND, 0);

        begin.set(Calendar.MILLISECOND, 0);
        Date time1 = begin.getTime();
        System.out.println(time1);

        //结束时间
        Calendar end = Calendar.getInstance();
        end.setTime(time);
        String[] split1 = endTime.split(":");
        end.set(Calendar.HOUR_OF_DAY, Integer.valueOf(split1[0]));

        end.set(Calendar.MINUTE, 0);

        end.set(Calendar.SECOND, 0);

        end.set(Calendar.MILLISECOND, 0);

        boolean flag = date.before(begin);
        if (flag){
            return new ResultVo(true,0L);
        }
        boolean before = date.before(end);
        if (before){
            return new ResultVo(true,1L);
        }

        return new ResultVo(true,2L);
    }

    //计费规则二&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
    private MoneyVo MoneyVoTwo(ParkingChargingDto parkingChargingDto) {
        MoneyVo moneyVo = ruleTwo(parkingChargingDto);
        //查询优惠卷 停车场id,车牌号码，状态部位1且不为2
        ParkingCouponrecord parkingCouponrecord = parkingCouponrecordService.findByParkingLotInformationIdAndLicenseAndState(parkingChargingDto.getParkinglotinformationid(), parkingChargingDto.getLicense());
        if (parkingCouponrecord!=null){
            //次卷
            if (parkingCouponrecord.getParkingCoupon().getType()==0){
                return getMoneyVo(0L,moneyVo.getMoney(),moneyVo.getMoney(),"次卷抵扣");
            }
            // 2是金额卷
            if (parkingCouponrecord.getParkingCoupon().getType()==2){
                long l =moneyVo.getMoney()- parkingCouponrecord.getParkingCoupon().getPreferentialfacevalue();
                if(l<=0){
                    return getMoneyVo(0L,moneyVo.getMoney(),moneyVo.getMoney(),"金额卷抵扣");
                }
                //为0就是金额卷抵扣完 不为0就是比如：微信加金额卷
                return getMoneyVo(l,moneyVo.getMoney(),Long.valueOf(parkingCouponrecord.getParkingCoupon().getPreferentialfacevalue()),"金额卷抵扣");
            }
            //小时卷
            if (parkingCouponrecord.getParkingCoupon().getType()==1){
                //优惠卷优惠时间
                long l = parkingCouponrecord.getParkingCoupon().getPreferentialfacevalue()*60;
                // 起步时长（60倍数）小于优惠时长
                if (DateTime.dateDiff(parkingChargingDto.getStartTime(), parkingChargingDto.getEndTime())<=l){
                    return getMoneyVo(0L,moneyVo.getMoney(),moneyVo.getMoney(),"小时卷抵扣");
                }
                else {
                    Date endTime = parkingChargingDto.getEndTime();
                    long l1 = endTime.getTime() - l * 60 * 1000;
                    Date date = new Date(l1);
                    parkingChargingDto.setEndTime(date);
                    MoneyVo three1 = ruleTwo(parkingChargingDto);
                    return getMoneyVo(three1.getMoney(),moneyVo.getMoney(),moneyVo.getMoney()-three1.getMoney(),"小时卷抵扣");
                }
            }
        }
        return moneyVo;
    }
    private MoneyVo ruleTwo(ParkingChargingDto parkingChargingDto) {
        Long parkinglotinformationid = parkingChargingDto.getParkinglotinformationid();
        ParkingLotInformation parkingLotInformation = parkingLotInformationService.selectParkingLotInformationById(parkinglotinformationid);
        //计费规则
        ParkingCharging parkingCharging = parkingChargingMapper.findByParkinglotinformationid(parkingChargingDto.getParkinglotinformationid(),parkingChargingDto.getDistinguish());
        List<ParkingBillingPeriod> list = parkingCharging.getParkingBillingPeriodList();
        //查询优惠卷 停车场id,车牌号码，状态部位1且不为2
        ParkingCouponrecord parkingCouponrecord = parkingCouponrecordService.findByParkingLotInformationIdAndLicenseAndState(parkinglotinformationid, parkingChargingDto.getLicense());
        //免费时长
        Long freetime = parkingCharging.getFreetime();
        //停车时长分钟
        long time = DateTime.dateDiff(parkingChargingDto.getStartTime(), parkingChargingDto.getEndTime());
        //起步时长（单位分钟）
        Long startingpriceduration = parkingCharging.getStartingpriceduration();
        //起步价
        Long startingprice = parkingCharging.getStartingprice();
        //每小时加收
        Long increaseincome = parkingCharging.getIncreaseincome();
        //停车总时长小时
        Long aLong = totalDuration(time);
        //判断免费时长
        if(freetime>time){
            //金额为0
            MoneyVo moneyVo = getMoneyVo(0L,0L,0L,"免费");
            return moneyVo;
        }
        //没有附加收费时间段
        if (list.size() == 0) {
            return getTwo(parkingCharging, time, startingpriceduration, startingprice, aLong);
        }
        if (list.size() != 0) {
            if (aLong<=24){
            return getMoneyTwoVo(list, time, increaseincome);
            }
            if (aLong>24){
                int freeTimeState = parkingCharging.getFreeTimeState();
                //计算停车几天
                long l1 = aLong / 24;
                //扣除几天后还剩下好多分钟
                long l = time % (24 * 60);
                /* ***********************计算一天内收取费用*********************************************/
                //分段计费最大值
                ParkingBillingPeriod parkingBill = list.get(list.size() - 1);
                //减去分段计费最大值计算剩余多少分钟
                long l2 = 24 * 60 - parkingBill.getMinutecharge();
                Long aLong1 = totalDuration(l2);
                //停车几天总金额
                long l3 = (parkingBill.getAddmoney() + aLong1 * increaseincome)*l1;
                if (l==0){
                    return getMoneyVo(l3,l3,0L,null);
                }
                //重复计算免费时长
                if (freeTimeState==0){
                    //判断剩余分钟是否大于免费时长
                    if (l<=freetime){
                        return getMoneyVo(l3,l3,0L,null);
                    }
                }
                MoneyVo moneyTwoVo = getMoneyTwoVo(list, l, increaseincome);
                return getMoneyVo(l3 + moneyTwoVo.getMoney(), l3 + moneyTwoVo.getMoney(), 0L, null);
            }
        }
        log.info("计费规则二时段计费出现bug");
      return null;
    }
    private MoneyVo getMoneyTwoVo(List<ParkingBillingPeriod> list, long time, Long increaseincome) {
        //循环计费时间段
        for (ParkingBillingPeriod parkingBillingPeriod : list) {
            //如果小于等于某个分钟时间段
            if (time <=parkingBillingPeriod.getMinutecharge()){
                //总价
                MoneyVo moneyVo = getMoneyVo(parkingBillingPeriod.getAddmoney(), parkingBillingPeriod.getAddmoney(), 0L, null);
                //返回没有优惠的总价
                return moneyVo;
            }
        }
        //如果超过分段计费最大值
        ParkingBillingPeriod parkingBill = list.get(list.size() - 1);
        //分钟
        long l = time - parkingBill.getMinutecharge();
        //小时
        Long aLong1 = totalDuration(l);
        //总金额
        long l1 = parkingBill.getAddmoney() + aLong1 * increaseincome;

        return getMoneyVo(l1,l1,0L,null);
    }
    //计费方式二叠加总 金额
    private MoneyVo getTwo(ParkingCharging parkingCharging, long time, Long startingpriceduration, Long startingprice, Long aLong) {
        //判断停车时长小于等于起步时间 小于等于直接返回起步价
        if (time <= startingpriceduration){
            return getMoneyVo(parkingCharging.getStartingprice(),0L,0L,null);
        }
        //没有优惠卷总返回值
        MoneyVo moneyVo = addMoneyVoTwo(parkingCharging, startingpriceduration, startingprice, aLong);
        return moneyVo;
    }
    private MoneyVo addMoneyVoTwo(ParkingCharging parkingCharging, Long startingpriceduration, Long startingprice, Long aLong) {
        //减去起步时长
        long l = aLong - startingpriceduration / 60; //(小时)
        //加收金额
        long l1 = parkingCharging.getIncreaseincome() * l;
        //总金额
        long l2 = startingprice + l1;

        return new MoneyVo(0L, l2,l2,null);
    }
















/******************************计算规则工具类*****************************************************/
    private Date getDate2(Date date1,String time){
        Calendar begin = Calendar.getInstance();
        // begin.add(Calendar.DATE, 1);
        String[] split = time.split(":");
        begin.setTime(date1);
        begin.set(Calendar.HOUR_OF_DAY, Integer.valueOf(split[0]));

        begin.set(Calendar.MINUTE, Integer.valueOf(split[1]));

        begin.set(Calendar.SECOND, Integer.valueOf(split[2]));

        begin.set(Calendar.MILLISECOND, 0);
        Date time1 = begin.getTime();
        long l = time1.getTime() +1000;
        return time1;
    }

    private Date getDate1(Date date1,String time){
        Calendar begin = Calendar.getInstance();
        // begin.add(Calendar.DATE, 1);
        String[] split = time.split(":");
        begin.setTime(date1);
        begin.set(Calendar.HOUR_OF_DAY, Integer.valueOf(split[0]));

        begin.set(Calendar.MINUTE, Integer.valueOf(split[1]));

        begin.set(Calendar.SECOND, Integer.valueOf(split[2]));

        begin.set(Calendar.MILLISECOND, 0);
        Date time1 = begin.getTime();
        long l = time1.getTime() - 1000;
        return time1;
    }

    private Date getDate(Date date1,String time){
        Calendar begin = Calendar.getInstance();
        // begin.add(Calendar.DATE, 1);
        String[] split = time.split(":");
        begin.setTime(date1);
        begin.set(Calendar.HOUR_OF_DAY, Integer.valueOf(split[0]));

        begin.set(Calendar.MINUTE, Integer.valueOf(split[1]));

        begin.set(Calendar.SECOND, Integer.valueOf(split[2]));

        begin.set(Calendar.MILLISECOND, 0);
        Date time1 = begin.getTime();
        return time1;
    }

    //停车总时长
    private Long totalDuration(long time) {
        long l1 = time % 60;
        //停车几小时
        long l=0;
        if (l1!=0){
            //取模有余数加一个小时
            long l2 = time / 60;
           return l=l2+1;
        }else {
            long l2 = time / 60;
           return l=l2;
        }
    }

    //返回值
    private MoneyVo getMoneyVo(Long money,Long amountpayable,Long discountamount,String paymentmethod ) {
        MoneyVo moneyVo = new MoneyVo();
        //实际支付好多
        moneyVo.setMoney(money);
        //应交金额
        moneyVo.setAmountpayable(money);
        //优惠金额
        moneyVo.setDiscountamount(discountamount);
        //
        moneyVo.setPaymentmethod(paymentmethod);
        return moneyVo;
    }

    //判断是否同一天
    private static boolean isSameDate(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        boolean isSameYear = cal1.get(Calendar.YEAR) == cal2
                .get(Calendar.YEAR);
        boolean isSameMonth = isSameYear
                && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
        boolean isSameDate = isSameMonth
                && cal1.get(Calendar.DAY_OF_MONTH) == cal2
                .get(Calendar.DAY_OF_MONTH);
        return isSameDate;
    }

   // 判断当前时间距离第二天凌晨的分钟数
    public static long getSecondsNextEarlyMorning(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return (cal.getTimeInMillis() - date.getTime()) / (1000*60);
    }

    public static long get(Date date) {
        // 明天00:00
        LocalDateTime midnight = LocalDateTime.ofInstant(date.toInstant(),
                ZoneId.systemDefault()).plusDays(1).withHour(0).withMinute(0)
                .withSecond(0).withNano(0);
        // 当前时间
        LocalDateTime currentDateTime = LocalDateTime.ofInstant(date.toInstant(),
                ZoneId.systemDefault());
        long result = ChronoUnit.SECONDS.between(currentDateTime, midnight);
        return result/60;
    }

    public static void main(String[] args) {
        long secondsNextEarlyMorning = getSecondsNextEarlyMorning(new Date());
        System.out.println(secondsNextEarlyMorning);
        long l = get(new Date());
        System.out.println(l);
    }
}
