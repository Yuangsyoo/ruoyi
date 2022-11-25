package com.ruoyi.web.controller.parking;

import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.sdk.LPRDemo;
import com.ruoyi.common.utils.CodeGenerateUtils;
import com.ruoyi.common.utils.DateTime.DateTime;
import com.ruoyi.parking.domain.ParkingLotEquipment;
import com.ruoyi.parking.domain.ParkingRecord;
import com.ruoyi.parking.dto.AlarmInfoPlate;
import com.ruoyi.parking.dto.Timeval;
import com.ruoyi.parking.dto.Total;
import com.ruoyi.parking.service.IParkingLotEquipmentService;
import com.ruoyi.parking.service.IParkingLotInformationService;
import com.ruoyi.parking.service.IParkingRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: yangyang
 * @Date: 2022/11/22/18:55
 * @Description:
 *
 * 进场出场业务逻辑
 */
@RestController
@RequestMapping("/parking")
@Anonymous
public class ParkingController {
    //停车记录
    @Autowired
    private IParkingRecordService parkingRecordService;
    //设备
    @Autowired
    private IParkingLotEquipmentService parkingLotEquipmentService;



    //设备扫描进场出场业务逻辑
    @PostMapping("/operation")
    public void test1(@RequestBody Total total){

        //获取车牌号
        String license =total.getAlarmInfoPlate().getResult().getPlateResult().getLicense();
        //获取推送数据设备序列号
        String serialno = total.getAlarmInfoPlate().getSerialno();
        //通过序列号获取推送数据设备信息
        ParkingLotEquipment parkingLotEquipment=parkingLotEquipmentService.findParkingLotEquipmentBySerialno(serialno);
        //查询停车场id通过序列号
        Long parkingLotInformationId=parkingLotEquipmentService.findBySerialno(serialno);
       //等于0则是进口，不等则是出口
        if(parkingLotEquipment.getDirection().equals("0")){

            //通过停车场id，车牌号查询有无未支付订单
            ParkingRecord parkingRecord= parkingRecordService.findByLicense(license,parkingLotInformationId);
            if (parkingRecord!=null){
                //如果有就删除掉
                parkingRecordService.deleteParkingRecordById(parkingRecord.getId());
            }

            Timeval timeval = total.getAlarmInfoPlate().getResult().getPlateResult().getTimeStamp().getTimeval();
            //获取入场时间
            Date date = DateTime.combineTime2(timeval.getDecyear(), timeval.getDecmon(), timeval.getDecday(), timeval.getDechour(), timeval.getDecmin(), timeval.getDecsec());
            //车牌颜色
            int carColor = total.getAlarmInfoPlate().getResult().getPlateResult().getCarColor();
            //生成订单号
            String s = CodeGenerateUtils.generateUnionPaySn();
            //进入口名称
            String name = parkingLotEquipment.getName();
            //保存进场信息
            saveParkingRecord(date, carColor, s, name,license,parkingLotInformationId);
            //设备开闸
            SwitchOn(total.getAlarmInfoPlate().getIpaddr());


            // TODO: 2022/11/25    停车场车位数减一


        }else {
            // TODO: 2022/11/25 判断是否在白名单范围  在直接放行
            //通过 停车场id，车牌号，和支付状态查询是否有无未支付订单，没有放行
           ParkingRecord parkingRecord=parkingRecordService.findByLicense(license,parkingLotInformationId);
           if (parkingRecord==null){
               // TODO: 2022/11/25 判断支付时间和当前开闸时间是否超过15分钟  超过时间具体业务待想
               // TODO: 2022/11/25 修改停车记录
               //设备开闸
               SwitchOn(total.getAlarmInfoPlate().getIpaddr());
               // TODO: 2022/11/25    停车场车位数减一
           }else {
               int count = 0;
               while(!parkingRecordService.findByLicense(license,parkingLotInformationId).getPaystate().equals("1")) {
                   try {
                       Thread.sleep(5 * 1000); //设置暂停的时间 5 秒
                       count ++ ;
                       //循环到15分钟，间隔5秒循环一次，总共180次
                       if (count==180){
                           System.out.println("循环结束");
                           //结束循环
                           break;
                       }
                       System.out.println("qqqqqq");
                       ////通过 停车场id，车牌号，和支付状态查询是否有无未支付订单，没有放行
                       if (parkingRecordService.findByLicense(license,parkingLotInformationId)==null) {
                           //开闸操作
                           SwitchOn(total.getAlarmInfoPlate().getIpaddr());
                           System.out.println("oooooooooo");
                           // TODO: 2022/11/25 修改停车记录
                           // TODO: 2022/11/25    停车场车位数减一
                           //结束循环
                           break ;
                       }
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
               }
           }




        }

    }
    //开闸方法
    private void SwitchOn(String ipAdress) {
        LPRDemo lprDemo = new LPRDemo();
        //初始化返回句柄
        int handle = lprDemo.InitClient(ipAdress);
        //开闸
        int i1 = lprDemo.switchOn(handle, 0, 500);
        //关闭设备的控制句柄
        lprDemo.VzLPRClient_Close(handle);
        //执行结束释放
        lprDemo.VzLPRClient_Cleanup();
    }
    //保存进场信息
    private void saveParkingRecord(Date date, int carColor, String s, String name,String license,Long parkingLotInformationId) {
        //添加进场信息
        ParkingRecord parkingRecord1 = new ParkingRecord();
        parkingRecord1.setLicense(license);
        parkingRecord1.setAdmissiontime(date);
        parkingRecord1.setParkinglotinformationid(parkingLotInformationId);
        /** 车牌颜色 */
        parkingRecord1.setLicensepllatecolor(String.valueOf(carColor));
        /** 订单编号 */
        parkingRecord1.setOrdernumber(s);
        /** 订单状态0代表未支付1代表已支付 */
        parkingRecord1.setOrderstate("0");
        /** 支付状态0代表未支付1代表已支付 */
        parkingRecord1.setPaystate("0");
        /** 出入口名称     出口时要拼接出口名称*/
        parkingRecord1.setEntranceandexitname(name);
        parkingRecordService.insertParkingRecord(parkingRecord1);
    }
}
