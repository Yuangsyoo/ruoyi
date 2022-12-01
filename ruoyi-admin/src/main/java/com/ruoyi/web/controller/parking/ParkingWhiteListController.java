package com.ruoyi.web.controller.parking;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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
import com.ruoyi.parking.domain.ParkingWhiteList;
import com.ruoyi.parking.service.IParkingWhiteListService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 停车场白名单Controller
 * 
 * @author ruoyi
 * @date 2022-11-29
 */
@RestController
@RequestMapping("/parking/whitelist")
public class ParkingWhiteListController extends BaseController
{
    @Autowired
    private IParkingWhiteListService parkingWhiteListService;

    /**
     * 查询停车场白名单列表
     */
    @PreAuthorize("@ss.hasPermi('parking:whitelist:list')")
    @GetMapping("/list")
    public TableDataInfo list(ParkingWhiteList parkingWhiteList)
    {
        startPage();
        List<ParkingWhiteList> list = parkingWhiteListService.selectParkingWhiteListList(parkingWhiteList);
        return getDataTable(list);
    }

    /**
     * 导出停车场白名单列表
     */
    @PreAuthorize("@ss.hasPermi('parking:whitelist:export')")
    @Log(title = "停车场白名单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ParkingWhiteList parkingWhiteList)
    {
        List<ParkingWhiteList> list = parkingWhiteListService.selectParkingWhiteListList(parkingWhiteList);
        ExcelUtil<ParkingWhiteList> util = new ExcelUtil<ParkingWhiteList>(ParkingWhiteList.class);
        util.exportExcel(response, list, "停车场白名单数据");
    }

    /**
     * 获取停车场白名单详细信息
     */
    @PreAuthorize("@ss.hasPermi('parking:whitelist:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(parkingWhiteListService.selectParkingWhiteListById(id));
    }

    /**
     * 新增停车场白名单
     */
    @PreAuthorize("@ss.hasPermi('parking:whitelist:add')")
    @Log(title = "停车场白名单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody @Valid ParkingWhiteList parkingWhiteList)
    {
        return toAjax(parkingWhiteListService.insertParkingWhiteList(parkingWhiteList));
    }

    /**
     * 修改停车场白名单
     */

    @PreAuthorize("@ss.hasPermi('parking:whitelist:edit')")
    @Log(title = "停车场白名单修改", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit1(@RequestBody ParkingWhiteList parkingWhiteList)
    {
        return toAjax(parkingWhiteListService.updateParkingWhiteList1(parkingWhiteList));
    }
    @PreAuthorize("@ss.hasPermi('parking:whitelist:renewal')")
    @Log(title = "停车场白名单续约", businessType = BusinessType.UPDATE)
    @PutMapping("/renewal")
    public AjaxResult renewal(@RequestBody ParkingWhiteList parkingWhiteList)
    {
        return toAjax(parkingWhiteListService.updateParkingWhiteList(parkingWhiteList));
    }

    /**
     * 删除停车场白名单
     */
    @PreAuthorize("@ss.hasPermi('parking:whitelist:remove')")
    @Log(title = "停车场白名单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(parkingWhiteListService.deleteParkingWhiteListByIds(ids));
    }
}
