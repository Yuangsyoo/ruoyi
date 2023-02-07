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
import com.ruoyi.parking.domain.ParkingCharging;
import com.ruoyi.parking.service.IParkingChargingService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 计费规则Controller
 * 
 * @author ruoyi
 * @date 2022-12-19
 */
@RestController
@RequestMapping("/parking/charging")
public class ParkingChargingController extends BaseController
{
    @Autowired
    private IParkingChargingService parkingChargingService;

    /**
     * 查询计费规则列表
     */
    @PreAuthorize("@ss.hasPermi('parking:charging:list')")
    @GetMapping("/list")
    public TableDataInfo list(ParkingCharging parkingCharging)
    {
        startPage();
        List<ParkingCharging> list = parkingChargingService.selectParkingChargingList(parkingCharging);
        return getDataTable(list);
    }
    /**
     * 导出计费规则列表
     */
    @PreAuthorize("@ss.hasPermi('parking:charging:export')")
    @Log(title = "计费规则", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ParkingCharging parkingCharging)
    {
        List<ParkingCharging> list = parkingChargingService.selectParkingChargingList(parkingCharging);
        ExcelUtil<ParkingCharging> util = new ExcelUtil<ParkingCharging>(ParkingCharging.class);
        util.exportExcel(response, list, "计费规则数据");
    }
    /**
     * 获取计费规则详细信息
     */
    @PreAuthorize("@ss.hasPermi('parking:charging:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(parkingChargingService.selectParkingChargingById(id));
    }
    /**
     * 新增计费规则
     */
    @PreAuthorize("@ss.hasPermi('parking:charging:add')")
    @Log(title = "计费规则", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ParkingCharging parkingCharging) {
        return toAjax(parkingChargingService.insertParkingCharging(parkingCharging));
    }
    /**
     * 修改计费规则
     */
    @PreAuthorize("@ss.hasPermi('parking:charging:edit')")
    @Log(title = "计费规则", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ParkingCharging parkingCharging) {
        return toAjax(parkingChargingService.updateParkingCharging(parkingCharging));
    }
    /**
     * 删除计费规则
     */
    @PreAuthorize("@ss.hasPermi('parking:charging:remove')")
    @Log(title = "计费规则", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids){
        return toAjax(parkingChargingService.deleteParkingChargingByIds(ids));
    }
}
