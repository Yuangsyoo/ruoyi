package com.ruoyi.web.controller.parking;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import com.ruoyi.common.annotation.Anonymous;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.parking.domain.ParkingCouponrecord;
import com.ruoyi.parking.service.IParkingCouponrecordService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
/**停车场优惠卷记录Controller
 * @author ruoyi
 * @date 2022-12-12
 */
@RestController
@RequestMapping("/parking/couponrecord")
public class ParkingCouponrecordController extends BaseController
{
    @Autowired
    private IParkingCouponrecordService parkingCouponrecordService;
    /**
     * 查询停车场优惠卷记录列表
     */
    @PreAuthorize("@ss.hasPermi('parking:couponrecord:list')")
    @GetMapping("/list")
    public TableDataInfo list(ParkingCouponrecord parkingCouponrecord)
    {
        startPage();
        List<ParkingCouponrecord> list = parkingCouponrecordService.selectParkingCouponrecordList(parkingCouponrecord);
        return getDataTable(list);
    }

    /**
     * 导出停车场优惠卷记录列表
     */
    @PreAuthorize("@ss.hasPermi('parking:couponrecord:export')")
    @Log(title = "停车场优惠卷记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ParkingCouponrecord parkingCouponrecord)
    {
        List<ParkingCouponrecord> list = parkingCouponrecordService.selectParkingCouponrecordList(parkingCouponrecord);
        ExcelUtil<ParkingCouponrecord> util = new ExcelUtil<ParkingCouponrecord>(ParkingCouponrecord.class);
        util.exportExcel(response, list, "停车场优惠卷记录数据");
    }

    /**
     * 获取停车场优惠卷记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('parking:couponrecord:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(parkingCouponrecordService.selectParkingCouponrecordById(id));
    }

    /**
     * 新增停车场优惠卷记录
     * 领取停车场优惠卷记录
     */
   // @PreAuthorize("@ss.hasPermi('parking:couponrecord:add')")
    //@Log(title = "停车场优惠卷记录", businessType = BusinessType.INSERT)
    @GetMapping("/add")
    @Anonymous
    public AjaxResult add(@RequestParam  Long parkingLotInformationId,
                          @RequestParam Long parkingCouponId,
                          @RequestParam String license)

    {
        ParkingCouponrecord parkingCouponrecord = new ParkingCouponrecord();
        parkingCouponrecord.setParkinglotinformationid(parkingLotInformationId);
        parkingCouponrecord.setLicense(license);
        parkingCouponrecord.setState("0");
        List<ParkingCouponrecord> list = parkingCouponrecordService.selectParkingCouponrecordList(parkingCouponrecord);
        String replace = license.replace(" ", "");
        if (replace.length()<7){
            return AjaxResult.error("请检查车牌");
        }
        String trim = license.trim();
        if (list.size()!=0){
            return AjaxResult.error("请勿多次领取");
        }
        return   parkingCouponrecordService.add(parkingLotInformationId,parkingCouponId,trim);
    }
    /**
     * 修改停车场优惠卷记录
     */
    @PreAuthorize("@ss.hasPermi('parking:couponrecord:edit')")
    @Log(title = "停车场优惠卷记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ParkingCouponrecord parkingCouponrecord)
    {
        return toAjax(parkingCouponrecordService.updateParkingCouponrecord(parkingCouponrecord));
    }
    /**
     * 删除停车场优惠卷记录
     */
    @PreAuthorize("@ss.hasPermi('parking:couponrecord:remove')")
    @Log(title = "停车场优惠卷记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(parkingCouponrecordService.deleteParkingCouponrecordByIds(ids));
    }
    public static void main(String[] args) {
        String a=" sf sdsa sd";
        String replace = a.replace(" ", "");
        System.out.println(replace);
    }

}
