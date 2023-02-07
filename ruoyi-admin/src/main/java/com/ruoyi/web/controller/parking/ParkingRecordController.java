package com.ruoyi.web.controller.parking;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.domain.entity.ParkingLotInformation;
import com.ruoyi.common.sdk.LPRDemo;
import com.ruoyi.common.utils.CodeGenerateUtils;
import com.ruoyi.parking.domain.*;
import com.ruoyi.parking.dto.ParkingRecordDDo;
import com.ruoyi.parking.mapper.ParkingBlackListMapper;
import com.ruoyi.parking.mapper.ParkingWhiteListMapper;
import com.ruoyi.parking.service.IParkingCouponService;
import com.ruoyi.parking.service.IParkingLotEquipmentService;
import com.ruoyi.parking.service.IParkingLotInformationService;
import com.ruoyi.parking.service.impl.ParkingCouponServiceImpl;
import com.ruoyi.parking.vo.ParkingRecordVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.parking.service.IParkingRecordService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 停车记录Controller
 * 
 * @author ruoyi
 * @date 2022-11-24
 */
@Slf4j
@RestController
@RequestMapping("/parking/record")
public class ParkingRecordController extends BaseController {
    @Autowired
    private IParkingRecordService parkingRecordService;
    @Autowired
    private RedisTemplate<Object,Object>redisTemplate;
    @Autowired
    private IParkingCouponService parkingCouponService;
    @Autowired
    private IParkingLotInformationService parkingLotInformationService;

    /**
     * 查询停车记录列表
     */
    @PreAuthorize("@ss.hasPermi('parking:record:list')")
    @GetMapping("/list")
    public ParkingRecordDDo list(ParkingRecord parkingRecord) {
        if(parkingRecord.getParkinglotinformationid()==0){
            return null;
        }
        startPage();
        List<ParkingRecord> list = parkingRecordService.selectParkingRecordList(parkingRecord);
        TableDataInfo dataTable = getDataTable(list);
       Long count= parkingRecordService.selectParkingRecordList1(parkingRecord);
        return  new ParkingRecordDDo(dataTable,count);
    }
    /**
     * 导出停车记录列表
     */
    @PreAuthorize("@ss.hasPermi('parking:record:export')")
    @Log(title = "停车记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ParkingRecord parkingRecord) {
        List<ParkingRecord> list = parkingRecordService.selectParkingRecordList(parkingRecord);
        ExcelUtil<ParkingRecord> util = new ExcelUtil<ParkingRecord>(ParkingRecord.class);
        util.exportExcel(response, list, "停车记录数据");
    }
    /**
     * 获取停车记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('parking:record:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(parkingRecordService.selectParkingRecordById(id));
    }
    @GetMapping(value = "/moneyOne/{id}")
    public AjaxResult getInfo1(@PathVariable("id") Long id) {
        return success(parkingRecordService.selectParkingRecordById1(id));
    }
    /**
     * 新增停车记录
     */
    @PreAuthorize("@ss.hasPermi('parking:record:add')")
    @Log(title = "停车记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody  ParkingRecord parkingRecord) {
        return toAjax(parkingRecordService.insertParkingRecord(parkingRecord));
    }
    /**
     * 修改停车记录
     */
    @PreAuthorize("@ss.hasPermi('parking:record:edit')")
    @Log(title = "停车记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ParkingRecord parkingRecord) {
        redisTemplate.opsForValue().set("123","123",5, TimeUnit.MICROSECONDS);
        return toAjax(parkingRecordService.updateParkingRecord(parkingRecord));
    }
    /**
     * 删除停车记录
     */
    @PreAuthorize("@ss.hasPermi('parking:record:remove')")
    @Log(title = "停车记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(parkingRecordService.deleteParkingRecordByIds(ids));
    }
    //查询指定停车场最近离场记录
    @GetMapping("/getPayRecord/{id}")
    public AjaxResult getPayRecord(@PathVariable Long id) {
        List<ParkingRecord> list = parkingRecordService.getPayRecord(id);
      return AjaxResult.success(list);
    }
    //查询指定停车场最近离场记录
    @GetMapping("/updateToRecord/{id}")
    public AjaxResult updateToRecord(@PathVariable Long id) {
       return parkingRecordService.updateToRecord(id);
    }

    //查询指定停车场最近离场记录
    @GetMapping("/updateToRecordOne/{id}")
    public AjaxResult updateToRecordOne(@PathVariable Long id) {
        return parkingRecordService.updateToRecordOne(id);
    }
    //查询指定停车场最近离场记录
    @GetMapping("/updateToRecordFromCoupon/{id}")
    public AjaxResult updateToRecordFromCoupon(@PathVariable Long id) {
        return parkingRecordService.updateToRecordFromCoupon(id);
    }
    //查询指定停车场最近离场记录
    @GetMapping("/updateToRecordFromCouponOne/{id}")
    public AjaxResult updateToRecordFromCouponOne(@PathVariable Long id) {
        return parkingRecordService.updateToRecordFromCouponOne(id);
    }
    //门口支付后调用接口 有牌无牌车公用开闸接口
    @Anonymous
    @PostMapping("/editPayState")
    public AjaxResult editPayState(@RequestBody ParkingRecordVo parkingRecordVo) {

       return parkingRecordService.editPayState(parkingRecordVo);
    }
    //无牌车金额为0公用开闸接口
    @Anonymous
    @PostMapping("/editPayStateOne")
    public AjaxResult editPayStateOne(@RequestBody ParkingRecordVo parkingRecordVo) {

        return parkingRecordService.editPayStateOne(parkingRecordVo);
    }
    //无牌车进场接口
    @Anonymous
    @GetMapping("/noLicensePlate")
    public AjaxResult noLicensePlate(@RequestParam(value ="parkinglotequipmentid") Long parkinglotequipmentid,@RequestParam(value ="license") String license,@RequestParam(value ="openid") String openid) {
        return parkingRecordService.noLicensePlate(parkinglotequipmentid,license,openid);
    }
    //有牌车从redis中获取要出口闸出场车信息等待扫码页面调用
    @GetMapping("/echoInformation")
    @Anonymous
    public AjaxResult echoInformation(@RequestParam String parkinglotequipmentid){
        ParkingRecordVo parkingRecordVo = (ParkingRecordVo) redisTemplate.opsForValue().get(parkinglotequipmentid);
        if (parkingRecordVo!=null){
            //判断是否是超时补费
            String license = parkingRecordVo.getLicense();
            Long parkinglotinformationid = parkingRecordVo.getParkinglotinformationid();
            ParkingRecord parkingRecord= parkingRecordService.findbypaystateandlicense(parkinglotinformationid,license);
            if (parkingRecord!=null){
                redisTemplate.opsForValue().set(parkinglotequipmentid+"Overtimefee",parkingRecordVo.getMoney());
            }
            /*redisTemplate.delete(String.valueOf(parkinglotequipmentid));*/
            ParkingLotInformation parkingLotInformation = parkingLotInformationService.selectParkingLotInformationById(parkinglotinformationid);
            BeanUtils.copyProperties(parkingLotInformation,parkingRecordVo);
            // TODO: 2023/2/3 判断是否场内支付过
            String license1 = parkingRecordVo.getLicense();
          ParkingRecord parkingRecord1=parkingRecordService.findByLicenseAndIndoorPayment(parkinglotinformationid,license1);
          if (parkingRecord1!=null){
              log.info("场内支付未超过离场时间");
              return AjaxResult.error("场内支付未超过离场时间,请尽快出场");
          }

            return AjaxResult.success(parkingRecordVo);
        }else {
            return AjaxResult.error("无记录");
        }

    }
    //无牌车出闸口返会支付页面信息
    @Anonymous
    @GetMapping("/echoInformationToLicense")
    public AjaxResult echoInformationToLicense( @RequestParam(value ="parkinglotequipmentid") Long parkinglotequipmentid
            , @RequestParam(value ="openid")String openid,@RequestParam(value ="license") String license){
        return parkingRecordService.echoInformationToLicense(parkinglotequipmentid,openid,license);
    }
    //室内返会支付页面信息
    @Anonymous
    @GetMapping("/indoor")
    public AjaxResult indoor(@RequestParam(value ="parkingLotInformationId") Long parkingLotInformationId,@RequestParam(value ="license") String license,@RequestParam(value ="openid") String openid){
        return parkingRecordService.indoor(parkingLotInformationId,license,openid);
    }
    //室内支付回调
    @Anonymous
    @PostMapping("/indoorCallback")
    public AjaxResult indoorCallback(@RequestBody ParkingRecordVo parkingRecordVo){
        return parkingRecordService.indoorCallback(parkingRecordVo);
    }
    @GetMapping("/getMoney/{id}")
    public AjaxResult getMoney(@PathVariable Long id){
        return parkingRecordService.getMoney(id);
    }
    @GetMapping("/getDailyInformation/{id}")
    public AjaxResult getDailyInformation(@PathVariable Long id){
        return parkingRecordService.getDailyInformation(id);
    }
    @GetMapping("/getByOpend")
    @Anonymous
    public AjaxResult getByOpend(@RequestParam("openid") String  openid,@RequestParam("parkingLotInformationId") Long  parkingLotInformationId){
        return parkingRecordService.getByOpend(openid,parkingLotInformationId);
    }
    //查询优惠卷信息
    @GetMapping("/obtain")
    @Anonymous
    public AjaxResult obtain(@RequestParam("parkingCouponId") Long  parkingCouponId){
        ParkingCoupon parkingCoupon = parkingCouponService.selectParkingCouponById(parkingCouponId);

        return AjaxResult.success(parkingCoupon);
    }

    //查询前10条信息接口
    @GetMapping("/list1")
    public AjaxResult list1(ParkingRecord parkingRecord) {

        List<ParkingRecord> list = parkingRecordService.selectParkingRecordListOne(parkingRecord.getParkinglotinformationid());

        return  AjaxResult.success(list);
    }
    //查询最进一条数据
    @Anonymous
    @GetMapping("/getPayRecord1")
    public AjaxResult getPayRecord1(@RequestParam("parkingLotInformationId")Long parkingLotInformationId,@RequestParam("parkingeqid") Long parkingeqid) {
        if (parkingeqid!=null){
            ParkingRecord parkingRecord = new ParkingRecord();
            parkingRecord.setParkingeqid(parkingeqid);
            parkingRecord.setParkinglotinformationid(parkingLotInformationId);
            List<ParkingRecord> payRecord1 = parkingRecordService.getPayRecord1(parkingRecord);
            if (payRecord1.size()==0){
                return AjaxResult.error("无最近停车记录");
            }
            return AjaxResult.success(payRecord1);
        }else {
            return AjaxResult.success("无记录");
        }
    }
}
