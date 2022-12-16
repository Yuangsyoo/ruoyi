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
import com.ruoyi.parking.domain.ParkingFixedparkingspace;
import com.ruoyi.parking.service.IParkingFixedparkingspaceService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 停车场固定车位Controller
 * 
 * @author ruoyi
 * @date 2022-12-13
 */
@RestController
@RequestMapping("/parking/fixedparkingspace")
public class ParkingFixedparkingspaceController extends BaseController
{
    @Autowired
    private IParkingFixedparkingspaceService parkingFixedparkingspaceService;

    /**
     * 查询停车场固定车位列表
     */
    @PreAuthorize("@ss.hasPermi('parking:fixedparkingspace:list')")
    @GetMapping("/list")
    public TableDataInfo list(ParkingFixedparkingspace parkingFixedparkingspace)
    {
        startPage();
        List<ParkingFixedparkingspace> list = parkingFixedparkingspaceService.selectParkingFixedparkingspaceList(parkingFixedparkingspace);
        return getDataTable(list);
    }

    /**
     * 导出停车场固定车位列表
     */
    @PreAuthorize("@ss.hasPermi('parking:fixedparkingspace:export')")
    @Log(title = "停车场固定车位", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ParkingFixedparkingspace parkingFixedparkingspace)
    {
        List<ParkingFixedparkingspace> list = parkingFixedparkingspaceService.selectParkingFixedparkingspaceList(parkingFixedparkingspace);
        ExcelUtil<ParkingFixedparkingspace> util = new ExcelUtil<ParkingFixedparkingspace>(ParkingFixedparkingspace.class);
        util.exportExcel(response, list, "停车场固定车位数据");
    }

    /**
     * 获取停车场固定车位详细信息
     */
    @PreAuthorize("@ss.hasPermi('parking:fixedparkingspace:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(parkingFixedparkingspaceService.selectParkingFixedparkingspaceById(id));
    }

    /**
     * 新增停车场固定车位
     */
    @PreAuthorize("@ss.hasPermi('parking:fixedparkingspace:add')")
    @Log(title = "停车场固定车位", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ParkingFixedparkingspace parkingFixedparkingspace)
    {
        return toAjax(parkingFixedparkingspaceService.insertParkingFixedparkingspace(parkingFixedparkingspace));
    }

    /**
     * 修改停车场固定车位
     */
    @PreAuthorize("@ss.hasPermi('parking:fixedparkingspace:edit')")
    @Log(title = "停车场固定车位", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ParkingFixedparkingspace parkingFixedparkingspace)
    {
        return toAjax(parkingFixedparkingspaceService.updateParkingFixedparkingspace(parkingFixedparkingspace));
    }

    /**
     * 删除停车场固定车位
     */
    @PreAuthorize("@ss.hasPermi('parking:fixedparkingspace:remove')")
    @Log(title = "停车场固定车位", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(parkingFixedparkingspaceService.deleteParkingFixedparkingspaceByIds(ids));
    }
}
