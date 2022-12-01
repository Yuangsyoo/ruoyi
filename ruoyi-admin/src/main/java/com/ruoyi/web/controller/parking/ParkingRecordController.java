package com.ruoyi.web.controller.parking;

import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.annotation.Anonymous;
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
    public TableDataInfo list(ParkingRecord parkingRecord)
    {
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
    public void export(HttpServletResponse response, ParkingRecord parkingRecord)
    {
        List<ParkingRecord> list = parkingRecordService.selectParkingRecordList(parkingRecord);
        ExcelUtil<ParkingRecord> util = new ExcelUtil<ParkingRecord>(ParkingRecord.class);
        util.exportExcel(response, list, "停车记录数据");
    }

    /**
     * 获取停车记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('parking:record:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(parkingRecordService.selectParkingRecordById(id));
    }

    /**
     * 新增停车记录
     */
    @PreAuthorize("@ss.hasPermi('parking:record:add')")
    @Log(title = "停车记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ParkingRecord parkingRecord)
    {
        return toAjax(parkingRecordService.insertParkingRecord(parkingRecord));
    }

    /**
     * 修改停车记录
     */
    @PreAuthorize("@ss.hasPermi('parking:record:edit')")
    @Log(title = "停车记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ParkingRecord parkingRecord)
    {
        redisTemplate.opsForValue().set("123","123",5, TimeUnit.MICROSECONDS);
        return toAjax(parkingRecordService.updateParkingRecord(parkingRecord));
    }

    /**
     * 删除停车记录
     */
    @PreAuthorize("@ss.hasPermi('parking:record:remove')")
    @Log(title = "停车记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(parkingRecordService.deleteParkingRecordByIds(ids));
    }


    @Anonymous
    @Log(title = "//通过车牌号，未支付状态查询出来修改状态", businessType = BusinessType.UPDATE)
    @PutMapping("/editPayState")
    //公共接口  支付服务那边调用
    public void editPayState(@RequestParam(value ="parkingLotInformationId") Long parkingLotInformationId
                            ,@RequestParam(value ="license") String license
                            ,@RequestParam(value ="money") Long money)
    {
        parkingRecordService.editPayState(parkingLotInformationId,license,money);
    }
}