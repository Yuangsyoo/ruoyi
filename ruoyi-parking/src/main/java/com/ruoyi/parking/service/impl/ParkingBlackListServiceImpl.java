package com.ruoyi.parking.service.impl;

import java.util.Date;
import java.util.List;

import com.ruoyi.common.exception.GlobalException;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanValidators;
import com.ruoyi.parking.domain.ParkingWhiteList;
import com.ruoyi.parking.mapper.ParkingLotEquipmentMapper;
import com.ruoyi.parking.mapper.ParkingLotInformationMapper;
import com.ruoyi.system.service.impl.SysUserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.parking.mapper.ParkingBlackListMapper;
import com.ruoyi.parking.domain.ParkingBlackList;
import com.ruoyi.parking.service.IParkingBlackListService;

import javax.validation.Validator;

/**
 * 黑名单管理Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-12-05
 */
@Service
public class ParkingBlackListServiceImpl implements IParkingBlackListService 
{
    @Autowired
    private ParkingBlackListMapper parkingBlackListMapper;
    @Autowired
    private ParkingLotInformationMapper parkingLotInformationMapper;
    @Autowired
    private ParkingLotEquipmentMapper parkingLotEquipmentMapper;


    /**
     * 查询黑名单管理
     * 
     * @param id 黑名单管理主键
     * @return 黑名单管理
     */
    @Override
    public ParkingBlackList selectParkingBlackListById(Long id)
    {
        return parkingBlackListMapper.selectParkingBlackListById(id);
    }

    /**
     * 查询黑名单管理列表
     * 
     * @param parkingBlackList 黑名单管理
     * @return 黑名单管理
     */
    @Override
    public List<ParkingBlackList> selectParkingBlackListList(ParkingBlackList parkingBlackList)
    {
        return parkingBlackListMapper.selectParkingBlackListList(parkingBlackList);
    }

    /**
     * 新增黑名单管理
     * 
     * @param parkingBlackList 黑名单管理
     * @return 结果
     */
    @Override
    public int insertParkingBlackList(ParkingBlackList parkingBlackList)
    {
        //判断是否重复添加黑名单
        ParkingBlackList parkingBlackList1 = parkingBlackListMapper.selectParkingBlackListByIdAndLicense(parkingBlackList.getParkinglotinformationid(), parkingBlackList.getLicense());
        if (parkingBlackList1!=null){
            throw new GlobalException("黑名单已存在");
        }
        parkingBlackList.setTime(new Date());
        String username = SecurityUtils.getUsername();
        parkingBlackList.setOperator(username);
        return parkingBlackListMapper.insertParkingBlackList(parkingBlackList);
    }

    /**
     * 修改黑名单管理
     * 
     * @param parkingBlackList 黑名单管理
     * @return 结果
     */
    @Override
    public int updateParkingBlackList(ParkingBlackList parkingBlackList)
    {
        return parkingBlackListMapper.updateParkingBlackList(parkingBlackList);
    }

    /**
     * 批量删除黑名单管理
     * 
     * @param ids 需要删除的黑名单管理主键
     * @return 结果
     */
    @Override
    public int deleteParkingBlackListByIds(Long[] ids)
    {
        return parkingBlackListMapper.deleteParkingBlackListByIds(ids);
    }
    @Autowired
    protected Validator validator;
    private static final Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);
    /**
     * 删除黑名单管理信息
     * 
     * @param id 黑名单管理主键
     * @return 结果
     */
    @Override
    public int deleteParkingBlackListById(Long id)
    {
        return parkingBlackListMapper.deleteParkingBlackListById(id);
    }
    /**
     * 求出黑名单总和数量
     * @return
     */
    @Override
    public Long sumAllBlack(Long id){
        if (id!=0){
            return parkingBlackListMapper.selectAllBlackListSum(id);
        }
        return null;
    }

    @Override
    public String importUser(List<ParkingBlackList> stuList, boolean isUpdateSupport, String operName, Long parkingLotInformationId) {
        if (StringUtils.isNull(stuList) || stuList.size() == 0)
        {
            throw new ServiceException("导入黑名单基本信息数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (ParkingBlackList stu : stuList)
        {
            try
            {
                // 验证是否存在这个车牌
                ParkingBlackList byLicense = parkingBlackListMapper.selectParkingBlackListByIdAndLicense(parkingLotInformationId,stu.getLicense());
                if (StringUtils.isNull(byLicense))
                {
                    BeanValidators.validateWithException(validator, stu);
                    stu.setOperator(operName);
                    stu.setParkinglotinformationid(parkingLotInformationId);
                    this.insertParkingBlackList(stu);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、车牌 " + stu.getLicense() + " 导入成功");
                }
                else if (isUpdateSupport)
                {
                    BeanValidators.validateWithException(validator, stu);
                    stu.setUpdateBy(operName);
                    stu.setParkinglotinformationid(parkingLotInformationId);
                    stu.setId(byLicense.getId());
                    parkingBlackListMapper.updateParkingBlackList(stu);
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
