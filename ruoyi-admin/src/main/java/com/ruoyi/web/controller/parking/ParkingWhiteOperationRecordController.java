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
import com.ruoyi.parking.domain.ParkingWhiteOperationRecord;
import com.ruoyi.parking.service.IParkingWhiteOperationRecordService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 白名单操作记录Controller
 * 
 * @author ruoyi
 * @date 2022-11-30
 */
@RestController
@RequestMapping("/parking/parkingWhiteListOperationRecord")
public class ParkingWhiteOperationRecordController extends BaseController
{
    @Autowired
    private IParkingWhiteOperationRecordService parkingWhiteOperationRecordService;

    /**
     * 查询白名单操作记录列表
     */
    @PreAuthorize("@ss.hasPermi('parking:parkingWhiteListOperationRecord:list')")
    @GetMapping("/list")
    public TableDataInfo list(ParkingWhiteOperationRecord parkingWhiteOperationRecord)
    {
        startPage();
        List<ParkingWhiteOperationRecord> list = parkingWhiteOperationRecordService.selectParkingWhiteOperationRecordList(parkingWhiteOperationRecord);
        return getDataTable(list);
    }

    /**
     * 导出白名单操作记录列表
     */
    @PreAuthorize("@ss.hasPermi('parking:parkingWhiteListOperationRecord:export')")
    @Log(title = "白名单操作记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ParkingWhiteOperationRecord parkingWhiteOperationRecord)
    {
        List<ParkingWhiteOperationRecord> list = parkingWhiteOperationRecordService.selectParkingWhiteOperationRecordList(parkingWhiteOperationRecord);
        ExcelUtil<ParkingWhiteOperationRecord> util = new ExcelUtil<ParkingWhiteOperationRecord>(ParkingWhiteOperationRecord.class);
        util.exportExcel(response, list, "白名单操作记录数据");
    }

    /**
     * 获取白名单操作记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('parking:parkingWhiteListOperationRecord:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(parkingWhiteOperationRecordService.selectParkingWhiteOperationRecordById(id));
    }

    /**
     * 新增白名单操作记录
     */
    @PreAuthorize("@ss.hasPermi('parking:parkingWhiteListOperationRecord:add')")
    @Log(title = "白名单操作记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ParkingWhiteOperationRecord parkingWhiteOperationRecord)
    {
        return toAjax(parkingWhiteOperationRecordService.insertParkingWhiteOperationRecord(parkingWhiteOperationRecord));
    }

    /**
     * 修改白名单操作记录
     */
    @PreAuthorize("@ss.hasPermi('parking:parkingWhiteListOperationRecord:edit')")
    @Log(title = "白名单操作记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ParkingWhiteOperationRecord parkingWhiteOperationRecord)
    {
        return toAjax(parkingWhiteOperationRecordService.updateParkingWhiteOperationRecord(parkingWhiteOperationRecord));
    }

    /**
     * 删除白名单操作记录
     */
    @PreAuthorize("@ss.hasPermi('parking:parkingWhiteListOperationRecord:remove')")
    @Log(title = "白名单操作记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(parkingWhiteOperationRecordService.deleteParkingWhiteOperationRecordByIds(ids));
    }
}
