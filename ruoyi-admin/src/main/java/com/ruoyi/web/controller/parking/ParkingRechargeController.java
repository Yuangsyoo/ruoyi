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
import com.ruoyi.parking.domain.ParkingRecharge;
import com.ruoyi.parking.service.IParkingRechargeService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 充值车管理Controller
 * 
 * @author ruoyi
 * @date 2023-02-01
 */
@RestController
@RequestMapping("/parking/recharge")
public class ParkingRechargeController extends BaseController
{
    @Autowired
    private IParkingRechargeService parkingRechargeService;

    /**
     * 查询充值车管理列表
     */
    @PreAuthorize("@ss.hasPermi('parking:recharge:list')")
    @GetMapping("/list")
    public TableDataInfo list(ParkingRecharge parkingRecharge)
    {
        startPage();
        List<ParkingRecharge> list = parkingRechargeService.selectParkingRechargeList(parkingRecharge);
        return getDataTable(list);
    }

    /**
     * 导出充值车管理列表
     */
    @PreAuthorize("@ss.hasPermi('parking:recharge:export')")
    @Log(title = "充值车管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ParkingRecharge parkingRecharge)
    {
        List<ParkingRecharge> list = parkingRechargeService.selectParkingRechargeList(parkingRecharge);
        ExcelUtil<ParkingRecharge> util = new ExcelUtil<ParkingRecharge>(ParkingRecharge.class);
        util.exportExcel(response, list, "充值车管理数据");
    }

    /**
     * 获取充值车管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('parking:recharge:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(parkingRechargeService.selectParkingRechargeById(id));
    }

    /**
     * 新增充值车管理
     */
    @PreAuthorize("@ss.hasPermi('parking:recharge:add')")
    @Log(title = "充值车管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ParkingRecharge parkingRecharge)
    {
        return toAjax(parkingRechargeService.insertParkingRecharge(parkingRecharge));
    }

    /**
     * 修改充值车管理
     */
    @PreAuthorize("@ss.hasPermi('parking:recharge:edit')")
    @Log(title = "充值车管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ParkingRecharge parkingRecharge)
    {
        return toAjax(parkingRechargeService.updateParkingRecharge(parkingRecharge));
    }
    @PutMapping("/update")
    public AjaxResult edit1(@RequestBody ParkingRecharge parkingRecharge)
    {
        return toAjax(parkingRechargeService.updateParkingRecharge1(parkingRecharge));
    }
    /**
     * 删除充值车管理
     */
    @PreAuthorize("@ss.hasPermi('parking:recharge:remove')")
    @Log(title = "充值车管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(parkingRechargeService.deleteParkingRechargeByIds(ids));
    }
}
