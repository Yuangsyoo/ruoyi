package com.ruoyi.web.controller.parking;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.ruoyi.parking.service.IParkingBlackListService;
import com.ruoyi.parking.service.IParkingCouponService;
import com.ruoyi.parking.vo.SumAllListAndCouponVo;
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
import org.springframework.web.multipart.MultipartFile;

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
    @Autowired
    private IParkingBlackListService parkingBlackListService;
    @Autowired
    private IParkingCouponService parkingCouponService;

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

    /**
     * 查询白名单、过期白名单、黑名单、优惠卷领取总数量、优惠卷过期数量总汇
     * @param id
     * @return
     */
    @GetMapping("/getSumAllListAndCoupon/{id}")
    public AjaxResult getSumAllListAndCoupon(@PathVariable Long id) {
        if (id!=0){
            SumAllListAndCouponVo sumAllListAndCouponVo = new SumAllListAndCouponVo();
            List<String> list = sumAllListAndCouponVo.getName();
            List<Long> list1 = sumAllListAndCouponVo.getCount();
            Long wl = parkingWhiteListService.summation(id);
            Long owl = parkingWhiteListService.overdueWhiteList(id);
            Long bl = parkingBlackListService.sumAllBlack(id);
            Long allCoupon = parkingCouponService.sumAllCoupon(id);
            Long overdueAllCoupon = parkingCouponService.sumAllExpiredCoupon(id);
            /*,'过期白名单','黑名单','优惠卷活动总数量','过期优惠数量'*/
            list.add("白名单");
            list.add("过期白名单");
            list.add("黑名单");
            list.add("优惠卷活动总数量");
            list.add("过期优惠数量");
            list1.add(wl);
            list1.add(owl);
            list1.add(bl);
            list1.add(allCoupon);
            list1.add(overdueAllCoupon);
            return AjaxResult.success(sumAllListAndCouponVo);
        }

       return AjaxResult.success();
    }

    @Log(title = "添加停车场名单", businessType = BusinessType.IMPORT) // todo
    @PreAuthorize("@ss.hasPermi('parking:whitelist:import')") // todo
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport,Long parkingLotInformationId) throws Exception
    {
        System.out.println(parkingLotInformationId);
        ExcelUtil<ParkingWhiteList> util = new ExcelUtil<>(ParkingWhiteList.class); // todo
        List<ParkingWhiteList> stuList = util.importExcel(file.getInputStream()); // todo
        String operName = getUsername();
        String message = parkingWhiteListService.importUser(stuList, updateSupport, operName,parkingLotInformationId); // todo
        return AjaxResult.success(message);
    }

    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response)
    {
        ExcelUtil<ParkingWhiteList> util = new ExcelUtil<>(ParkingWhiteList.class); // todo
        util.importTemplateExcel(response, "白名单基本信息");
    }

}
