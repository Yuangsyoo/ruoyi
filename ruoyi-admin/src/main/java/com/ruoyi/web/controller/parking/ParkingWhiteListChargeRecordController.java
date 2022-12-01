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
import com.ruoyi.parking.domain.ParkingWhiteListChargeRecord;
import com.ruoyi.parking.service.IParkingWhiteListChargeRecordService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 白名单收费记录Controller
 * 
 * @author ruoyi
 * @date 2022-11-30
 */
@RestController
@RequestMapping("/parking/parkingWhiteListChargeRecord")
public class ParkingWhiteListChargeRecordController extends BaseController
{
    @Autowired
    private IParkingWhiteListChargeRecordService parkingWhiteListChargeRecordService;

    /**
     * 查询白名单收费记录列表
     */
    @PreAuthorize("@ss.hasPermi('parking:parkingWhiteListChargeRecord:list')")
    @GetMapping("/list")
    public TableDataInfo list(ParkingWhiteListChargeRecord parkingWhiteListChargeRecord)
    {
        startPage();
        List<ParkingWhiteListChargeRecord> list = parkingWhiteListChargeRecordService.selectParkingWhiteListChargeRecordList(parkingWhiteListChargeRecord);
        return getDataTable(list);
    }

    /**
     * 导出白名单收费记录列表
     */
    @PreAuthorize("@ss.hasPermi('parking:parkingWhiteListChargeRecord:export')")
    @Log(title = "白名单收费记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ParkingWhiteListChargeRecord parkingWhiteListChargeRecord)
    {
        List<ParkingWhiteListChargeRecord> list = parkingWhiteListChargeRecordService.selectParkingWhiteListChargeRecordList(parkingWhiteListChargeRecord);
        ExcelUtil<ParkingWhiteListChargeRecord> util = new ExcelUtil<ParkingWhiteListChargeRecord>(ParkingWhiteListChargeRecord.class);
        util.exportExcel(response, list, "白名单收费记录数据");
    }

    /**
     * 获取白名单收费记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('parking:parkingWhiteListChargeRecord:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(parkingWhiteListChargeRecordService.selectParkingWhiteListChargeRecordById(id));
    }

    /**
     * 新增白名单收费记录
     */
    @PreAuthorize("@ss.hasPermi('parking:parkingWhiteListChargeRecord:add')")
    @Log(title = "白名单收费记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ParkingWhiteListChargeRecord parkingWhiteListChargeRecord)
    {
        return toAjax(parkingWhiteListChargeRecordService.insertParkingWhiteListChargeRecord(parkingWhiteListChargeRecord));
    }

    /**
     * 修改白名单收费记录
     */
    @PreAuthorize("@ss.hasPermi('parking:parkingWhiteListChargeRecord:edit')")
    @Log(title = "白名单收费记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ParkingWhiteListChargeRecord parkingWhiteListChargeRecord)
    {
        return toAjax(parkingWhiteListChargeRecordService.updateParkingWhiteListChargeRecord(parkingWhiteListChargeRecord));
    }

    /**
     * 删除白名单收费记录
     */
    @PreAuthorize("@ss.hasPermi('parking:parkingWhiteListChargeRecord:remove')")
    @Log(title = "白名单收费记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(parkingWhiteListChargeRecordService.deleteParkingWhiteListChargeRecordByIds(ids));
    }
}
