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
import com.ruoyi.parking.domain.ParkingCoupon;
import com.ruoyi.parking.service.IParkingCouponService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 优惠卷Controller
 * 
 * @author ruoyi
 * @date 2022-12-12
 */
@RestController
@RequestMapping("/parking/coupon")
public class ParkingCouponController extends BaseController
{
    @Autowired
    private IParkingCouponService parkingCouponService;

    /**
     * 查询优惠卷列表
     */
    @PreAuthorize("@ss.hasPermi('parking:coupon:list')")
    @GetMapping("/list")
    public TableDataInfo list(ParkingCoupon parkingCoupon)
    {
        startPage();
        List<ParkingCoupon> list = parkingCouponService.selectParkingCouponList(parkingCoupon);
        return getDataTable(list);
    }

    /**
     * 导出优惠卷列表
     */
    @PreAuthorize("@ss.hasPermi('parking:coupon:export')")
    @Log(title = "优惠卷", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ParkingCoupon parkingCoupon)
    {
        List<ParkingCoupon> list = parkingCouponService.selectParkingCouponList(parkingCoupon);
        ExcelUtil<ParkingCoupon> util = new ExcelUtil<ParkingCoupon>(ParkingCoupon.class);
        util.exportExcel(response, list, "优惠卷数据");
    }

    /**
     * 获取优惠卷详细信息
     */
    @PreAuthorize("@ss.hasPermi('parking:coupon:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(parkingCouponService.selectParkingCouponById(id));
    }

    /**
     * 新增优惠卷
     */
    @PreAuthorize("@ss.hasPermi('parking:coupon:add')")
    @Log(title = "优惠卷", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ParkingCoupon parkingCoupon)
    {
        return toAjax(parkingCouponService.insertParkingCoupon(parkingCoupon));
    }

    /**
     * 修改优惠卷
     */
    @PreAuthorize("@ss.hasPermi('parking:coupon:edit')")
    @Log(title = "优惠卷", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ParkingCoupon parkingCoupon)
    {
        return toAjax(parkingCouponService.updateParkingCoupon(parkingCoupon));
    }

    /**
     * 删除优惠卷
     */
    @PreAuthorize("@ss.hasPermi('parking:coupon:remove')")
    @Log(title = "优惠卷", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(parkingCouponService.deleteParkingCouponByIds(ids));
    }

}
