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
import com.ruoyi.parking.domain.ParkingBlackList;
import com.ruoyi.parking.domain.ParkingLotEquipment;
import com.ruoyi.parking.domain.ParkingWhiteList;
import com.ruoyi.parking.mapper.ParkingBlackListMapper;
import com.ruoyi.parking.mapper.ParkingWhiteListMapper;
import com.ruoyi.parking.service.IParkingLotEquipmentService;
import com.ruoyi.parking.service.IParkingLotInformationService;
import com.ruoyi.parking.vo.ParkingRecordVo;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.parking.domain.ParkingRecord;
import com.ruoyi.parking.service.IParkingRecordService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 停车记录Controller
 * 
 * @author ruoyi
 * @date 2022-11-24
 */
@RestController
@RequestMapping("/parking/record")
public class ParkingRecordController extends BaseController
{
    @Autowired
    private IParkingRecordService parkingRecordService;
    @Autowired
    private RedisTemplate<Object,Object>redisTemplate;





    /**
     * 查询停车记录列表
     */
    @PreAuthorize("@ss.hasPermi('parking:record:list')")
    @GetMapping("/list")
    public TableDataInfo list(ParkingRecord parkingRecord) {
        if(parkingRecord.getParkinglotinformationid()==0){
            return null;
        }
        startPage();
        List<ParkingRecord> list = parkingRecordService.selectParkingRecordList(parkingRecord);
        return getDataTable(list);
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
    @GetMapping("/updateToRecordFromCoupon/{id}")
    public AjaxResult updateToRecordFromCoupon(@PathVariable Long id) {
        return parkingRecordService.updateToRecordFromCoupon(id);
    }

    //门口支付后调用接口 有牌无牌车公用开闸接口
    @Anonymous
    @GetMapping("/editPayState")
    public void editPayState(@RequestParam(value ="parkingLotInformationId") Long parkingLotInformationId,@RequestParam(value ="parkinglotequipmentid") Long parkinglotequipmentid,@RequestParam(value ="license") String license,@RequestParam(value ="paymentMethod") String paymentMethod) {
        parkingRecordService.editPayState(parkingLotInformationId,license,parkinglotequipmentid,paymentMethod);
    }
    //无牌车进场接口
    @Anonymous
    @GetMapping("/noLicensePlate")
    public AjaxResult noLicensePlate(@RequestParam(value ="parkinglotequipmentid") Long parkinglotequipmentid,@RequestParam(value ="license") String license,@RequestParam(value ="openid") String openid) {
        return parkingRecordService.noLicensePlate(parkinglotequipmentid,license,openid);

    }
    //有牌车从redis中获取要出口闸出场车信息等待扫码页面调用
    @GetMapping("/echoInformation/{parkinglotequipmentid}")
    @Anonymous
    public AjaxResult echoInformation(@PathVariable Long parkinglotequipmentid){
        ParkingRecordVo parkingRecordVo = (ParkingRecordVo) redisTemplate.opsForValue().get(String.valueOf(parkinglotequipmentid));
        if (parkingRecordVo!=null){
            //判断是否是超时补费
            String license = parkingRecordVo.getLicense();
            Long parkinglotinformationid = parkingRecordVo.getParkinglotinformationid();
            ParkingRecord parkingRecord= parkingRecordService.findbypaystateandlicense(parkinglotinformationid,license);
            if (parkingRecord!=null){
                redisTemplate.opsForValue().set(parkinglotequipmentid+"Overtimefee",parkingRecordVo.getMoney(),5,TimeUnit.MINUTES);
            }
            redisTemplate.delete(String.valueOf(parkinglotequipmentid));
            return AjaxResult.success(parkingRecordVo);
        }else {
            return AjaxResult.error("无记录");
        }

    }

    //无牌车出闸口返会支付页面信息
    @Anonymous
    @GetMapping("/echoInformationToLicense")
    public AjaxResult echoInformationToLicense( @RequestParam(value ="parkinglotequipmentid") Long parkinglotequipmentid
            , @RequestParam(value ="openid")String openid){
        return parkingRecordService.echoInformationToLicense(parkinglotequipmentid,openid);
    }
    //室内返会支付页面信息
    @Anonymous
    @GetMapping("/indoor")
    public AjaxResult indoor(@RequestParam(value ="parkingLotInformationId") Long parkingLotInformationId,@RequestParam(value ="license") String license){
        return parkingRecordService.indoor(parkingLotInformationId,license);
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
}
