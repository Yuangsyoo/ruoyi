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
}
