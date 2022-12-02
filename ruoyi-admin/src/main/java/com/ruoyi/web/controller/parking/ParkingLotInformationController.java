package com.ruoyi.web.controller.parking;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.core.domain.entity.ParkingLotInformation;
import com.ruoyi.parking.dto.ParkingLotEquipmentDto;
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

import com.ruoyi.parking.service.IParkingLotInformationService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 停车场管理Controller
 * 
 * @author ruoyi
 * @date 2022-11-23
 */
@RestController
@RequestMapping("/parking/information")
public class ParkingLotInformationController extends BaseController
{
    @Autowired
    private IParkingLotInformationService parkingLotInformationService;

    /**
     * 查询停车场管理列表
     */
    @PreAuthorize("@ss.hasPermi('parking:information:list')")
    @GetMapping("/list")
    public TableDataInfo list(ParkingLotInformation parkingLotInformation)
    {
        startPage();
        List<ParkingLotInformation> list = parkingLotInformationService.selectParkingLotInformationList(parkingLotInformation);
        return getDataTable(list);
    }

    @GetMapping("/list1")
    public AjaxResult getlist()
    {
        List<ParkingLotInformation> list = parkingLotInformationService.findParkingLotInformationList();
        return AjaxResult.success("200",list);
    }
    /**
     * 导出停车场管理列表
     */
    @PreAuthorize("@ss.hasPermi('parking:information:export')")
    @Log(title = "停车场管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ParkingLotInformation parkingLotInformation)
    {
        List<ParkingLotInformation> list = parkingLotInformationService.selectParkingLotInformationList(parkingLotInformation);
        ExcelUtil<ParkingLotInformation> util = new ExcelUtil<ParkingLotInformation>(ParkingLotInformation.class);
        util.exportExcel(response, list, "停车场管理数据");
    }

    /**
     * 获取停车场管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('parking:information:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(parkingLotInformationService.selectParkingLotInformationById(id));
    }

    /**
     * 新增停车场管理
     */
    @PreAuthorize("@ss.hasPermi('parking:information:add')")
    @Log(title = "停车场管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ParkingLotEquipmentDto parkingLotEquipmentDto)
    {
        return toAjax(parkingLotInformationService.insertParkingLotInformation(parkingLotEquipmentDto));
    }

    /**
     * 修改停车场管理
     */
    @PreAuthorize("@ss.hasPermi('parking:information:edit')")
    @Log(title = "停车场管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ParkingLotInformation parkingLotInformation)
    {
        return toAjax(parkingLotInformationService.updateParkingLotInformation(parkingLotInformation));
    }

    /**
     * 删除停车场管理
     */
    @PreAuthorize("@ss.hasPermi('parking:information:remove')")
    @Log(title = "停车场管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(parkingLotInformationService.deleteParkingLotInformationByIds(ids));
    }
}
