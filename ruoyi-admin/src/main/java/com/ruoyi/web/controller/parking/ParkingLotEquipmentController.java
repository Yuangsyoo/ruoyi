package com.ruoyi.web.controller.parking;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.annotation.Anonymous;
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
import com.ruoyi.common.core.domain.entity.ParkingLotEquipment;
import com.ruoyi.parking.service.IParkingLotEquipmentService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 停车场设备管理Controller
 * 
 * @author ruoyi
 * @date 2022-11-24
 */
@RestController
@RequestMapping("/parking/equipment")
public class ParkingLotEquipmentController extends BaseController
{
    @Autowired
    private IParkingLotEquipmentService parkingLotEquipmentService;

    /**
     * 查询停车场设备管理列表
     */
    @PreAuthorize("@ss.hasPermi('parking:equipment:list')")
    @GetMapping("/list")
    public TableDataInfo list(ParkingLotEquipment parkingLotEquipment)
    {
        startPage();
        List<ParkingLotEquipment> list = parkingLotEquipmentService.selectParkingLotEquipmentList(parkingLotEquipment);
        return getDataTable(list);
    }

    /**
     * 导出停车场设备管理列表
     */
    @PreAuthorize("@ss.hasPermi('parking:equipment:export')")
    @Log(title = "停车场设备管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ParkingLotEquipment parkingLotEquipment)
    {
        List<ParkingLotEquipment> list = parkingLotEquipmentService.selectParkingLotEquipmentList(parkingLotEquipment);
        ExcelUtil<ParkingLotEquipment> util = new ExcelUtil<ParkingLotEquipment>(ParkingLotEquipment.class);
        util.exportExcel(response, list, "停车场设备管理数据");
    }

    /**
     * 获取停车场设备管理详细信息
     */
    @GetMapping(value = "/{id}")
    @Anonymous
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(parkingLotEquipmentService.selectParkingLotEquipmentById(id));
    }

    @GetMapping(value = "/ByParkinglotinformationid/{parkinglotinformationid}")
    public AjaxResult byParkinglotinformationid(@PathVariable("parkinglotinformationid") Long parkinglotinformationid)
    {
       List<ParkingLotEquipment> list=parkingLotEquipmentService.byParkinglotinformationid(parkinglotinformationid);
        return AjaxResult.success(list);
    }
    @GetMapping(value = "/ByParkinglotinformationid1/{parkinglotinformationid}")
    public AjaxResult byParkinglotinformationid1(@PathVariable("parkinglotinformationid") Long parkinglotinformationid)
    {
        List<ParkingLotEquipment> list=parkingLotEquipmentService.byParkinglotinformationid1(parkinglotinformationid);
        return AjaxResult.success(list);
    }
    @GetMapping(value = "/getEquipmentOne/{parkinglotinformationid}")
    public AjaxResult getEquipmentOne(@PathVariable("parkinglotinformationid") Long parkinglotinformationid)
    {
        List<ParkingLotEquipment> list=parkingLotEquipmentService.getEquipmentOne(parkinglotinformationid);
        return AjaxResult.success(list);
    }

    /**
     * 新增停车场设备管理
     */
    @PreAuthorize("@ss.hasPermi('parking:equipment:add')")
    @Log(title = "停车场设备管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ParkingLotEquipment parkingLotEquipment)
    {
        return toAjax(parkingLotEquipmentService.insertParkingLotEquipment(parkingLotEquipment));
    }

    /**
     * 修改停车场设备管理
     */
    @PreAuthorize("@ss.hasPermi('parking:equipment:edit')")
    @Log(title = "停车场设备管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ParkingLotEquipment parkingLotEquipment)
    {
        return toAjax(parkingLotEquipmentService.updateParkingLotEquipment(parkingLotEquipment));
    }

    /**
     * 删除停车场设备管理
     */
    @PreAuthorize("@ss.hasPermi('parking:equipment:remove')")
    @Log(title = "停车场设备管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(parkingLotEquipmentService.deleteParkingLotEquipmentByIds(ids));
    }
}
