package com.ruoyi.web.controller.parking;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.parking.domain.ParkingRechargeRecord;
import com.ruoyi.parking.service.IParkingRechargeRecordService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 充值车操作记录Controller
 * 
 * @author ruoyi
 * @date 2023-02-01
 */
@RestController
@RequestMapping("/parking/rechargerecord")
public class ParkingRechargeRecordController extends BaseController
{
    @Autowired
    private IParkingRechargeRecordService parkingRechargeRecordService;

    /**
     * 查询充值车操作记录列表
     */
    @PreAuthorize("@ss.hasPermi('parking:rechargerecord:list')")
    @GetMapping("/list")
    public TableDataInfo list(ParkingRechargeRecord parkingRechargeRecord)
    {
        startPage();
        List<ParkingRechargeRecord> list = parkingRechargeRecordService.selectParkingRechargeRecordList(parkingRechargeRecord);
        return getDataTable(list);
    }

    /**
     * 导出充值车操作记录列表
     */
    @PreAuthorize("@ss.hasPermi('parking:rechargerecord:export')")
    @Log(title = "充值车操作记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ParkingRechargeRecord parkingRechargeRecord)
    {
        List<ParkingRechargeRecord> list = parkingRechargeRecordService.selectParkingRechargeRecordList(parkingRechargeRecord);
        ExcelUtil<ParkingRechargeRecord> util = new ExcelUtil<ParkingRechargeRecord>(ParkingRechargeRecord.class);
        util.exportExcel(response, list, "充值车操作记录数据");
    }

    /**
     * 获取充值车操作记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('parking:rechargerecord:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(parkingRechargeRecordService.selectParkingRechargeRecordById(id));
    }

    /**
     * 新增充值车操作记录
     */
    @PreAuthorize("@ss.hasPermi('parking:rechargerecord:add')")
    @Log(title = "充值车操作记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ParkingRechargeRecord parkingRechargeRecord)
    {
        return toAjax(parkingRechargeRecordService.insertParkingRechargeRecord(parkingRechargeRecord));
    }

    /**
     * 修改充值车操作记录
     */
    @PreAuthorize("@ss.hasPermi('parking:rechargerecord:edit')")
    @Log(title = "充值车操作记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ParkingRechargeRecord parkingRechargeRecord)
    {
        return toAjax(parkingRechargeRecordService.updateParkingRechargeRecord(parkingRechargeRecord));
    }

    /**
     * 删除充值车操作记录
     */
    @PreAuthorize("@ss.hasPermi('parking:rechargerecord:remove')")
    @Log(title = "充值车操作记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(parkingRechargeRecordService.deleteParkingRechargeRecordByIds(ids));
    }
}
