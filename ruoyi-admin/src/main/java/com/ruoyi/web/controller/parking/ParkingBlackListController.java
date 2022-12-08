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
import com.ruoyi.parking.domain.ParkingBlackList;
import com.ruoyi.parking.service.IParkingBlackListService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 黑名单管理Controller
 * 
 * @author ruoyi
 * @date 2022-12-05
 */
@RestController
@RequestMapping("/parking/blackList")
public class ParkingBlackListController extends BaseController
{
    @Autowired
    private IParkingBlackListService parkingBlackListService;

    /**
     * 查询黑名单管理列表
     */
    @PreAuthorize("@ss.hasPermi('parking:blackList:list')")
    @GetMapping("/list")
    public TableDataInfo list(ParkingBlackList parkingBlackList)
    {
        startPage();
        List<ParkingBlackList> list = parkingBlackListService.selectParkingBlackListList(parkingBlackList);
        return getDataTable(list);
    }

    /**
     * 导出黑名单管理列表
     */
    @PreAuthorize("@ss.hasPermi('parking:blackList:export')")
    @Log(title = "黑名单管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ParkingBlackList parkingBlackList)
    {
        List<ParkingBlackList> list = parkingBlackListService.selectParkingBlackListList(parkingBlackList);
        ExcelUtil<ParkingBlackList> util = new ExcelUtil<ParkingBlackList>(ParkingBlackList.class);
        util.exportExcel(response, list, "黑名单管理数据");
    }

    /**
     * 获取黑名单管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('parking:blackList:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(parkingBlackListService.selectParkingBlackListById(id));
    }

    /**
     * 新增黑名单管理
     */
    @PreAuthorize("@ss.hasPermi('parking:blackList:add')")
    @Log(title = "黑名单管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ParkingBlackList parkingBlackList)
    {
        return toAjax(parkingBlackListService.insertParkingBlackList(parkingBlackList));
    }

    /**
     * 修改黑名单管理
     */
    @PreAuthorize("@ss.hasPermi('parking:blackList:edit')")
    @Log(title = "黑名单管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ParkingBlackList parkingBlackList)
    {
        return toAjax(parkingBlackListService.updateParkingBlackList(parkingBlackList));
    }

    /**
     * 删除黑名单管理
     */
    @PreAuthorize("@ss.hasPermi('parking:blackList:remove')")
    @Log(title = "黑名单管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(parkingBlackListService.deleteParkingBlackListByIds(ids));
    }
}
