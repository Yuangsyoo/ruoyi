package com.ruoyi.parking.service.impl;

import java.util.Date;
import java.util.List;

import com.ruoyi.common.core.domain.entity.ParkingLotInformation;
import com.ruoyi.common.exception.GlobalException;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.sdk.LPRDemo;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.common.utils.bean.BeanValidators;
import com.ruoyi.parking.domain.*;
import com.ruoyi.parking.mapper.*;
import com.ruoyi.system.service.impl.SysUserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.parking.service.IParkingWhiteListService;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Validator;


/**
 * 停车场白名单Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-11-29
 */
@Service
public class ParkingWhiteListServiceImpl implements IParkingWhiteListService 
{
    @Autowired
    private ParkingWhiteListMapper parkingWhiteListMapper;
    @Autowired
    private ParkingLotInformationMapper parkingLotInformationMapper;
    @Autowired
    private ParkingWhiteOperationRecordMapper parkingWhiteOperationRecordMapper;
    @Autowired
    private ParkingWhiteListChargeRecordMapper parkingWhiteListChargeRecordMapper;
    @Autowired
    private ParkingLotEquipmentMapper parkingLotEquipmentMapper;

    /**
     * 查询停车场白名单
     * 
     * @param id 停车场白名单主键
     * @return 停车场白名单
     */
    @Override
    public ParkingWhiteList selectParkingWhiteListById(Long id)
    {
        return parkingWhiteListMapper.selectParkingWhiteListById(id);
    }

    /**
     * 查询停车场白名单列表
     * 
     * @param parkingWhiteList 停车场白名单
     * @return 停车场白名单
     */
    @Override
    public List<ParkingWhiteList> selectParkingWhiteListList(ParkingWhiteList parkingWhiteList)
    {
        return parkingWhiteListMapper.selectParkingWhiteListList(parkingWhiteList);
    }

    /**
     * 新增停车场白名单
     * 
     * @param parkingWhiteList 停车场白名单
     * @return 结果
     */
    @Override
    @Transactional
    public int insertParkingWhiteList(ParkingWhiteList parkingWhiteList)
    {
        ParkingWhiteList parkingWhiteList1=parkingWhiteListMapper.findByLicense(parkingWhiteList.getLicense(),parkingWhiteList.getParkinglotinformationid());
        if (parkingWhiteList1!=null){
            throw new GlobalException("白名单已存在，请查看是否过期,过期请续约");
        }
        //获取停车场信息
       ParkingLotInformation parkingLotInformation=parkingLotInformationMapper.selectParkingLotInformationById(parkingWhiteList.getParkinglotinformationid());
        if(parkingLotInformation.getMonthlycardpurchase().equals("1")){
            throw new GlobalException("请开启停车场月卡购买功能");
        }
       /* LPRDemo lprDemo = new LPRDemo();
        ParkingLotEquipment parkingLotEquipment = new ParkingLotEquipment();
        parkingLotEquipment.setParkinglotinformationid(parkingLotInformation.getId());
        List<ParkingLotEquipment> parkingLotEquipments = parkingLotEquipmentMapper.selectParkingLotEquipmentList(parkingLotEquipment);
        for (ParkingLotEquipment lotEquipment : parkingLotEquipments) {
            //获取句柄
            int i = lprDemo.InitClient(lotEquipment.getIpadress());
            //设备添加白名单
            lprDemo.AddWlistPlate(i, parkingWhiteList.getLicense());
        }*/

        parkingWhiteList.setAddtime(new Date());
        parkingWhiteList.setState(0);
        //获取操作人名称
        String username = SecurityUtils.getUsername();
        parkingWhiteList.setOperator(username);
        //添加白名单操作记录
        addParkingWhiteOperationRecord(parkingWhiteList, username);
        //添加白名单收费记录
        addParkingWhiteListChargeRecord(parkingWhiteList, username);

        //保存白名单
        return parkingWhiteListMapper.insertParkingWhiteList(parkingWhiteList);
    }

    private void addParkingWhiteListChargeRecord(ParkingWhiteList parkingWhiteList, String username) {
        ParkingWhiteListChargeRecord parkingWhiteListChargeRecord = new ParkingWhiteListChargeRecord();
        parkingWhiteListChargeRecord.setParkinglotinformationid(parkingWhiteList.getParkinglotinformationid());
        parkingWhiteListChargeRecord.setPaymenttime(new Date());
        parkingWhiteListChargeRecord.setStarttime(parkingWhiteList.getStarttime());
        parkingWhiteListChargeRecord.setEndtime(parkingWhiteList.getEndtime());
        parkingWhiteListChargeRecord.setMoney(parkingWhiteList.getMonthlyfee());
        parkingWhiteListChargeRecord.setOperator(username);
        parkingWhiteListChargeRecord.setOperationtype("后台操作");
        parkingWhiteListChargeRecord.setRemarks(parkingWhiteList.getName()+"收费明细");
        parkingWhiteListChargeRecord.setPaymentmethod("现金");
        parkingWhiteListChargeRecord.setLicense(parkingWhiteList.getLicense());
        parkingWhiteListChargeRecordMapper.insertParkingWhiteListChargeRecord(parkingWhiteListChargeRecord);
    }

    private void addParkingWhiteOperationRecord(ParkingWhiteList parkingWhiteList, String username) {
        ParkingWhiteOperationRecord parkingWhiteOperationRecord = new ParkingWhiteOperationRecord();
        parkingWhiteOperationRecord.setParkinglotinformationid(parkingWhiteList.getParkinglotinformationid());
        parkingWhiteOperationRecord.setLicense(parkingWhiteList.getLicense());
        parkingWhiteOperationRecord.setTime(new Date());
        parkingWhiteOperationRecord.setRemarks("增加");
        parkingWhiteOperationRecord.setOperator(username);
        parkingWhiteOperationRecordMapper.insertParkingWhiteOperationRecord(parkingWhiteOperationRecord);
    }
    private void addParkingWhiteOperationRecord1(ParkingWhiteList parkingWhiteList, String username) {
        ParkingWhiteOperationRecord parkingWhiteOperationRecord = new ParkingWhiteOperationRecord();
        parkingWhiteOperationRecord.setParkinglotinformationid(parkingWhiteList.getParkinglotinformationid());
        parkingWhiteOperationRecord.setLicense(parkingWhiteList.getLicense());
        parkingWhiteOperationRecord.setTime(new Date());
        parkingWhiteOperationRecord.setRemarks("续期");
        parkingWhiteOperationRecord.setOperator(username);
        parkingWhiteOperationRecordMapper.insertParkingWhiteOperationRecord(parkingWhiteOperationRecord);
    }

    /**
     * 修改停车场白名单
     * 
     * @param parkingWhiteList 停车场白名单
     * @return 结果
     */
    @Override
    public int updateParkingWhiteList1(ParkingWhiteList parkingWhiteList)
    {
        return parkingWhiteListMapper.updateParkingWhiteList(parkingWhiteList);
    }
    @Override
    public int updateParkingWhiteList(ParkingWhiteList parkingWhiteList)
    {

        Date date = new Date();
        //车主以前白名单记录
        ParkingWhiteList parkingWhiteList1 = parkingWhiteListMapper.selectParkingWhiteListById(parkingWhiteList.getId());
        //续期当前时间小于原纪录结束时间
        if (date.getTime()<parkingWhiteList1.getEndtime().getTime()){
            parkingWhiteList1.setEndtime(parkingWhiteList.getEndtime());
        }else {
            parkingWhiteList1.setEndtime(parkingWhiteList.getEndtime());
            parkingWhiteList1.setAddtime(parkingWhiteList.getStarttime());
            parkingWhiteList1.setAddtime(date);
            parkingWhiteList1.setState(0);
        }
       /* ParkingLotInformation parkingLotInformation=parkingLotInformationMapper.selectParkingLotInformationById(parkingWhiteList1.getParkinglotinformationid());
        LPRDemo lprDemo = new LPRDemo();
        ParkingLotEquipment parkingLotEquipment = new ParkingLotEquipment();
        parkingLotEquipment.setParkinglotinformationid(parkingLotInformation.getId());
        List<ParkingLotEquipment> parkingLotEquipments = parkingLotEquipmentMapper.selectParkingLotEquipmentList(parkingLotEquipment);
        for (ParkingLotEquipment lotEquipment : parkingLotEquipments) {
            int i = lprDemo.InitClient(lotEquipment.getIpadress());
            int i1 = lprDemo.AddWlistPlate(i, parkingWhiteList.getLicense());
        }*/
        String username = SecurityUtils.getUsername();
        //续期缴费详情
        addParkingWhiteListChargeRecord(parkingWhiteList,username);
        //操作记录
        addParkingWhiteOperationRecord1(parkingWhiteList,username);
        //续期

        return  parkingWhiteListMapper.updateParkingWhiteList(parkingWhiteList1);
    }

    /**
     * 批量删除停车场白名单
     * 
     * @param ids 需要删除的停车场白名单主键
     * @return 结果
     */
    @Override
    public int deleteParkingWhiteListByIds(Long[] ids)
    {
      /*  LPRDemo lprDemo = new LPRDemo();
        for (Long id : ids) {
            //获取白名单信息
            ParkingWhiteList parkingWhiteList = selectParkingWhiteListById(id);
            //获取停车场信息
            ParkingLotInformation parkingLotInformation=parkingLotInformationMapper.selectParkingLotInformationById(parkingWhiteList.getParkinglotinformationid());
            ParkingLotEquipment parkingLotEquipment = new ParkingLotEquipment();
            parkingLotEquipment.setParkinglotinformationid(parkingLotInformation.getId());
            List<ParkingLotEquipment> parkingLotEquipments = parkingLotEquipmentMapper.selectParkingLotEquipmentList(parkingLotEquipment);
            for (ParkingLotEquipment lotEquipment : parkingLotEquipments) {
                int i = lprDemo.InitClient(lotEquipment.getIpadress());
                //删除设备白名单
                lprDemo.DeleteWlistByPlate(i,parkingWhiteList.getLicense());
            }
        }*/
        return parkingWhiteListMapper.deleteParkingWhiteListByIds(ids);
    }

    /**
     * 删除停车场白名单信息
     * 
     * @param id 停车场白名单主键
     * @return 结果
     */
    @Override
    public int deleteParkingWhiteListById(Long id)
    {
       /* //获取白名单信息
        ParkingWhiteList parkingWhiteList = selectParkingWhiteListById(id);
        //获取停车场信息
        ParkingLotInformation parkingLotInformation=parkingLotInformationMapper.selectParkingLotInformationById(parkingWhiteList.getParkinglotinformationid());
        ParkingLotEquipment parkingLotEquipment = new ParkingLotEquipment();
        parkingLotEquipment.setParkinglotinformationid(parkingLotInformation.getId());
        LPRDemo lprDemo = new LPRDemo();
        List<ParkingLotEquipment> parkingLotEquipments = parkingLotEquipmentMapper.selectParkingLotEquipmentList(parkingLotEquipment);
        for (ParkingLotEquipment lotEquipment : parkingLotEquipments) {
            int i = lprDemo.InitClient(lotEquipment.getIpadress());
            //删除设备白名单
            lprDemo.DeleteWlistByPlate(i,parkingWhiteList.getLicense());
        }*/
        return parkingWhiteListMapper.deleteParkingWhiteListById(id);
    }

    /**
     * 求出白名单总和数量
     * @return
     */
    @Override
    public Long summation(Long id){
        if (id!=0){
            return parkingWhiteListMapper.selectAllWhiteListSum(id);
        }
        return null;
    }

    /**
     * 查询过期的白名单数量
     * @return
     */
    @Override
    public Long overdueWhiteList(Long id){
        if (id!=0){
            return parkingWhiteListMapper.selectAllOverdueWhite(id);
        }
        return null;
    }

    private static final Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);

    @Autowired
    protected Validator validator;



    @Override
    public String importUser(List<ParkingWhiteList> stuList, Boolean isUpdateSupport, String operName,Long parkingLotInformationId) {
        if (StringUtils.isNull(stuList) || stuList.size() == 0)
        {
            throw new ServiceException("导入白名单基本信息数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (ParkingWhiteList stu : stuList)
        {
            try
            {
                // 验证是否存在这个车牌
                ParkingWhiteList byLicense = parkingWhiteListMapper.findByLicense(stu.getLicense(),parkingLotInformationId);
                if (StringUtils.isNull(byLicense))
                {
                    BeanValidators.validateWithException(validator, stu);
                    stu.setOperator(operName);
                    stu.setParkinglotinformationid(parkingLotInformationId);
                    this.insertParkingWhiteList(stu);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、车牌 " + stu.getLicense() + " 导入成功");
                }
                else if (isUpdateSupport)
                {
                    BeanValidators.validateWithException(validator, stu);
                    stu.setUpdateBy(operName);
                    stu.setParkinglotinformationid(parkingLotInformationId);
                    stu.setId(byLicense.getId());
                    parkingWhiteListMapper.updateParkingWhiteList(stu);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、车牌 " + stu.getLicense() + " 更新成功");
                }
                else
                {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、车牌 " + stu.getLicense() + " 已存在");
                }
            }
            catch (Exception e)
            {
                failureNum++;
                String msg = "<br/>" + failureNum + "、车牌 " + stu.getLicense() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0)
        {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        }
        else
        {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }



}
