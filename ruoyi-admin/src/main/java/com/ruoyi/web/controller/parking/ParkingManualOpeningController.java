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
import com.ruoyi.parking.domain.ParkingManualOpening;
import com.ruoyi.parking.service.IParkingManualOpeningService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 停车场手动开杆管理Controller
 * 
 * @author ruoyi
 * @date 2022-12-01
 */
@RestController
@RequestMapping("/parking/opening")
public class ParkingManualOpeningController extends BaseController
{
    @Autowired
    private IParkingManualOpeningService parkingManualOpeningService;

    /**
     * 查询停车场手动开杆管理列表
     */
    @PreAuthorize("@ss.hasPermi('parking:opening:list')")
    @GetMapping("/list")
    public TableDataInfo list(ParkingManualOpening parkingManualOpening)
    {
        startPage();
        List<ParkingManualOpening> list = parkingManualOpeningService.selectParkingManualOpeningList(parkingManualOpening);
        return getDataTable(list);
    }

    /**
     * 导出停车场手动开杆管理列表
     */
    @PreAuthorize("@ss.hasPermi('parking:opening:export')")
    @Log(title = "停车场手动开杆管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ParkingManualOpening parkingManualOpening)
    {
        List<ParkingManualOpening> list = parkingManualOpeningService.selectParkingManualOpeningList(parkingManualOpening);
        ExcelUtil<ParkingManualOpening> util = new ExcelUtil<ParkingManualOpening>(ParkingManualOpening.class);
        util.exportExcel(response, list, "停车场手动开杆管理数据");
    }

    /**
     * 获取停车场手动开杆管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('parking:opening:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(parkingManualOpeningService.selectParkingManualOpeningById(id));
    }

    /**
     * 停车场手动开杆操作
     */
    @PreAuthorize("@ss.hasPermi('parking:opening:add')")
    @Log(title = "停车场手动开杆管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ParkingManualOpening parkingManualOpening)
    {
        return toAjax(parkingManualOpeningService.insertParkingManualOpening(parkingManualOpening));
    }

    /**
     * 修改停车场手动开杆管理
     */
    @PreAuthorize("@ss.hasPermi('parking:opening:edit')")
    @Log(title = "停车场手动开杆管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ParkingManualOpening parkingManualOpening)
    {
        return toAjax(parkingManualOpeningService.updateParkingManualOpening(parkingManualOpening));
    }

    /**
     * 删除停车场手动开杆管理
     */
    @PreAuthorize("@ss.hasPermi('parking:opening:remove')")
    @Log(title = "停车场手动开杆管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(parkingManualOpeningService.deleteParkingManualOpeningByIds(ids));
    }
}
