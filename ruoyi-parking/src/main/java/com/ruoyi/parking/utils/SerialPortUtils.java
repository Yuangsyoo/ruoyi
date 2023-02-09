package com.ruoyi.parking.utils;
import com.ruoyi.common.utils.Hex;
import com.ruoyi.common.core.domain.entity.ParkingLotEquipment;
import lombok.extern.slf4j.Slf4j;

import java.util.Base64;

/**
 * Created with IntelliJ IDEA.
 * @Author: yangyang
 * @Date: 2023/01/04/9:30
 * @Description:
 */
@Slf4j
public class SerialPortUtils {
    //无牌车进场临显指令
    public static  String wupaiche(String data) {
        String s4 = getStringOne("减速慢行", "040A");
        String s3 =  getStringOne("一车一杆", "030A");
        String s2 =  getStringOne(data, "020A");
        String s1 =  getStringOne("欢迎光临","010A");
        String s5 = voicePlayback(data,"01");
        String[] split2 = s2.split(",");
        String[] split1 = s1.split(",");
        String[] split3 = s3.split(",");
        String[] split4 = s4.split(",");
        String[] split5 = s5.split(",");
        String a="{\n" +
                "\"Response_AlarmInfoPlate\": {\n" +
                "\"info\":\"ok\",\n" +
                "\"channelNum\" : 0, \n" +
                "\"manualTrigger\" : \"no\",\n" +
                "\"is_pay\":\"false\",\n" +
                "\"serialData\" :[\n" +
                "{\n" +
                "\"serialChannel\":0,\n" +
                "\"data\" : \""+split1[0]+"\",\n" +
                "\"dataLen\" : "+split1[1]+"\n" +
                "},\n" +
                "{ \n" +
                "\"serialChannel\":0,\n" +
                "\"data\" : \""+split2[0]+"\",\n" +
                "\"dataLen\" : "+split2[1]+"\n" +
                "},\n" +
                "{ \n" +
                "\"serialChannel\":0,\n" +
                "\"data\" : \""+split3[0]+"\",\n" +
                "\"dataLen\" : "+split3[1]+"\n" +
                "},\n" +
                "{ \n" +
                "\"serialChannel\":0,\n" +
                "\"data\" : \""+split4[0]+"\",\n" +
                "\"dataLen\" : "+split4[1]+"\n" +
                "},\n" +
                "{ \n" +
                "\"serialChannel\":0,\n" +
                "\"data\" : \""+split5[0]+"\",\n" +
                "\"dataLen\" : "+split5[1]+"\n" +
                "}\n" +
                "]\n" +
                "}\n" +
                "}";
        return a;

    }
    //出口支付后临显指令
    public static  String payAfter(String data) {
        String s4 = getStringOne("减速慢行", "040A");
        String s3 =  getStringOne("一车一杆", "030A");
        String s2 =   getStringOne(data, "020A");
        String s1 = getStringOne("一路顺风","010A");
        String s5 =  payAfter(data, "02");
        String[] split2 = s2.split(",");
        String[] split1 = s1.split(",");
        String[] split3 = s3.split(",");
        String[] split4 = s4.split(",");
        String[] split5 = s5.split(",");
        String a="{\n" +
                "\"Response_AlarmInfoPlate\": {\n" +
                "\"info\":\"ok\",\n" +
                "\"channelNum\" : 0, \n" +
                "\"manualTrigger\" : \"no\",\n" +
                "\"is_pay\":\"false\",\n" +
                "\"serialData\" :[\n" +
                "{\n" +
                "\"serialChannel\":0,\n" +
                "\"data\" : \""+split1[0]+"\",\n" +
                "\"dataLen\" : "+split1[1]+"\n" +
                "},\n" +
                "{ \n" +
                "\"serialChannel\":0,\n" +
                "\"data\" : \""+split2[0]+"\",\n" +
                "\"dataLen\" : "+split2[1]+"\n" +
                "},\n" +
                "{ \n" +
                "\"serialChannel\":0,\n" +
                "\"data\" : \""+split3[0]+"\",\n" +
                "\"dataLen\" : "+split3[1]+"\n" +
                "},\n" +
                "{ \n" +
                "\"serialChannel\":0,\n" +
                "\"data\" : \""+split4[0]+"\",\n" +
                "\"dataLen\" : "+split4[1]+"\n" +
                "},\n" +
                "{ \n" +
                "\"serialChannel\":0,\n" +
                "\"data\" : \""+split5[0]+"\",\n" +
                "\"dataLen\" : "+split5[1]+"\n" +
                "}\n" +
                "]\n" +
                "}\n" +
                "}";
        return a;


    }
    //出口待缴费临显指令
    public static String ExitOne(String data,Long time,Long money)  {
        //天数
        long l = time /(24 * 60 );
        //天数后剩余分钟
        long l3 = time % (24 * 60 );
        //停车小时
        long l1 = l3 /60;
        //停车剩余分钟
        long l2 = l3 % 60;
        String s4 = getString4("待支付金额:"+money);
        String s3 = getString3("请扫码缴费");
        String s2 = getString2(data);
        String s1 = getString1("停车时长:"+l+"天"+l1+"小时"+l2+"分钟");
        String[] split2 = s2.split(",");
        String[] split1 = s1.split(",");
        String[] split3 = s3.split(",");
        String[] split4 = s4.split(",");
        String s5 = voicePlayback1(l,l1,l2,data, "0b",money);
        String[] split5 = s5.split(",");
        String a="{\n" +
                "\"Response_AlarmInfoPlate\": {\n" +
                "\"info\":\"no\",\n" +
                "\"channelNum\" : 0, \n" +
                "\"manualTrigger\" : \"no\",\n" +
                "\"is_pay\":\"false\",\n" +
                "\"serialData\" :[\n" +
                "{\n" +
                "\"serialChannel\":0,\n" +
                "\"data\" : \""+split1[0]+"\",\n" +
                "\"dataLen\" : "+split1[1]+"\n" +
                "},\n" +
                "{ \n" +
                "\"serialChannel\":0,\n" +
                "\"data\" : \""+split2[0]+"\",\n" +
                "\"dataLen\" : "+split2[1]+"\n" +
                "},\n" +
                "{ \n" +
                "\"serialChannel\":0,\n" +
                "\"data\" : \""+split3[0]+"\",\n" +
                "\"dataLen\" : "+split3[1]+"\n" +
                "},\n" +
                "{ \n" +
                "\"serialChannel\":0,\n" +
                "\"data\" : \""+split4[0]+"\",\n" +
                "\"dataLen\" : "+split4[1]+"\n" +
                "},\n" +
                "{ \n" +
                "\"serialChannel\":0,\n" +
                "\"data\" : \""+split5[0]+"\",\n" +
                "\"dataLen\" : "+split5[1]+"\n" +
                "}\n" +
                "]\n" +
                "}\n" +
                "}";
        return a;
    }
    //室内支付正常出场显示  免费时长
    public static String Exit(String data,Long time) {
        //停车小时
        long l = time /60;
        //停车剩余分钟
        long l1 = time % 60;
        String s4 = getString4("减速慢行");
        String s3 = getString3("停车时长:"+l+"小时"+l1+"分钟");
        String s2 = getString2(data);
        String s1 = getString1("一路顺风");
        String[] split2 = s2.split(",");
        String[] split1 = s1.split(",");
        String[] split3 = s3.split(",");
        String[] split4 = s4.split(",");
        String s5 = voicePlayback(data, "02");
        String[] split5 = s5.split(",");
        String a="{\n" +
                "\"Response_AlarmInfoPlate\": {\n" +
                "\"info\":\"ok\",\n" +
                "\"channelNum\" : 0, \n" +
                "\"manualTrigger\" : \"no\",\n" +
                "\"is_pay\":\"false\",\n" +
                "\"serialData\" :[\n" +
                "{\n" +
                "\"serialChannel\":0,\n" +
                "\"data\" : \""+split1[0]+"\",\n" +
                "\"dataLen\" : "+split1[1]+"\n" +
                "},\n" +
                "{ \n" +
                "\"serialChannel\":0,\n" +
                "\"data\" : \""+split2[0]+"\",\n" +
                "\"dataLen\" : "+split2[1]+"\n" +
                "},\n" +
                "{ \n" +
                "\"serialChannel\":0,\n" +
                "\"data\" : \""+split3[0]+"\",\n" +
                "\"dataLen\" : "+split3[1]+"\n" +
                "},\n" +
                "{ \n" +
                "\"serialChannel\":0,\n" +
                "\"data\" : \""+split4[0]+"\",\n" +
                "\"dataLen\" : "+split4[1]+"\n" +
                "},\n" +
                "{ \n" +
                "\"serialChannel\":0,\n" +
                "\"data\" : \""+split5[0]+"\",\n" +
                "\"dataLen\" : "+split5[1]+"\n" +
                "}\n" +
                "]\n" +
                "}\n" +
                "}";
        return a;
    }
    public static String Exit1(String data) {

        String s4 = getString4("减速慢行");
        String s2 = getString2(data);
        String s1 = getString1("一路顺风");
        String[] split2 = s2.split(",");
        String[] split1 = s1.split(",");

        String[] split4 = s4.split(",");
        String s5 = voicePlayback(data, "02");
        String[] split5 = s5.split(",");
        String a="{\n" +
                "\"Response_AlarmInfoPlate\": {\n" +
                "\"info\":\"ok\",\n" +
                "\"channelNum\" : 0, \n" +
                "\"manualTrigger\" : \"no\",\n" +
                "\"is_pay\":\"false\",\n" +
                "\"serialData\" :[\n" +
                "{\n" +
                "\"serialChannel\":0,\n" +
                "\"data\" : \""+split1[0]+"\",\n" +
                "\"dataLen\" : "+split1[1]+"\n" +
                "},\n" +
                "{ \n" +
                "\"serialChannel\":0,\n" +
                "\"data\" : \""+split2[0]+"\",\n" +
                "\"dataLen\" : "+split2[1]+"\n" +
                "},\n" +
                "{ \n" +
                "\"serialChannel\":0,\n" +
                "\"data\" : \""+split4[0]+"\",\n" +
                "\"dataLen\" : "+split4[1]+"\n" +
                "},\n" +
                "{ \n" +
                "\"serialChannel\":0,\n" +
                "\"data\" : \""+split5[0]+"\",\n" +
                "\"dataLen\" : "+split5[1]+"\n" +
                "}\n" +
                "]\n" +
                "}\n" +
                "}";
        return a;
    }
    //超出出场时间补费
    public static String overtime(Long time,String data,Long money) {
//天数
        long l = time /(24 * 60 );
        //天数后剩余分钟
        long l3 = time % (24 * 60 );
        //停车小时
        long l1 = l3 /60;
        //停车剩余分钟
        long l2 = l3 % 60;

        String s4 = getString4("减速慢行");
        String s3 = getString3("需要补缴费用:"+money+"元");
        String s2 = getString2(data);
        String s1 = getString1("超出时长:"+l+"天"+l1+"小时"+l2+"分钟");
        String[] split2 = s2.split(",");
        String[] split1 = s1.split(",");
        String[] split3 = s3.split(",");
        String[] split4 = s4.split(",");
       String s5 = voicePlayback2(l,l1,l2,data, "0b",money);
        String[] split5 = s5.split(",");
        String a="{\n" +
                "\"Response_AlarmInfoPlate\": {\n" +
                "\"info\":\"no\",\n" +
                "\"channelNum\" : 0, \n" +
                "\"manualTrigger\" : \"no\",\n" +
                "\"is_pay\":\"false\",\n" +
                "\"serialData\" :[\n" +
                "{\n" +
                "\"serialChannel\":0,\n" +
                "\"data\" : \""+split1[0]+"\",\n" +
                "\"dataLen\" : "+split1[1]+"\n" +
                "},\n" +
                "{ \n" +
                "\"serialChannel\":0,\n" +
                "\"data\" : \""+split2[0]+"\",\n" +
                "\"dataLen\" : "+split2[1]+"\n" +
                "},\n" +
                "{ \n" +
                "\"serialChannel\":0,\n" +
                "\"data\" : \""+split3[0]+"\",\n" +
                "\"dataLen\" : "+split3[1]+"\n" +
                "},\n" +
                "{ \n" +
                "\"serialChannel\":0,\n" +
                "\"data\" : \""+split4[0]+"\",\n" +
                "\"dataLen\" : "+split4[1]+"\n" +
                "},\n" +
                "{ \n" +
                "\"serialChannel\":0,\n" +
                "\"data\" : \""+split5[0]+"\",\n" +
                "\"dataLen\" : "+split5[1]+"\n" +
                "}\n" +
                "]\n" +
                "}\n" +
                "}";
        return a;
    }
    //出口次卷下发临显指令
    public static String ExportSecondaryVolume(String data) {
        String s4 = getString4("减速慢行");
        String s3 = getString3("一车一杆");
        String s2 = getString2(data);
        String s1 = getString1("一路顺风");
        String[] split2 = s2.split(",");
        String[] split1 = s1.split(",");
        String[] split3 = s3.split(",");
        String[] split4 = s4.split(",");
        String s5 = voicePlayback(data, "02");
        String[] split5 = s5.split(",");
        String a="{\n" +
                "\"Response_AlarmInfoPlate\": {\n" +
                "\"info\":\"ok\",\n" +
                "\"channelNum\" : 0, \n" +
                "\"manualTrigger\" : \"no\",\n" +
                "\"is_pay\":\"false\",\n" +
                "\"serialData\" :[\n" +
                "{\n" +
                "\"serialChannel\":0,\n" +
                "\"data\" : \""+split1[0]+"\",\n" +
                "\"dataLen\" : "+split1[1]+"\n" +
                "},\n" +
                "{ \n" +
                "\"serialChannel\":0,\n" +
                "\"data\" : \""+split2[0]+"\",\n" +
                "\"dataLen\" : "+split2[1]+"\n" +
                "},\n" +
                "{ \n" +
                "\"serialChannel\":0,\n" +
                "\"data\" : \""+split3[0]+"\",\n" +
                "\"dataLen\" : "+split3[1]+"\n" +
                "},\n" +
                "{ \n" +
                "\"serialChannel\":0,\n" +
                "\"data\" : \""+split4[0]+"\",\n" +
                "\"dataLen\" : "+split4[1]+"\n" +
                "},\n" +
                "{ \n" +
                "\"serialChannel\":0,\n" +
                "\"data\" : \""+split5[0]+"\",\n" +
                "\"dataLen\" : "+split5[1]+"\n" +
                "}\n" +
                "]\n" +
                "}\n" +
                "}";
        return a;
    }
    //白名单出入
    public static String whiteList(String data,Long time) {
        String time1="剩余天数:"+String.valueOf(time)+"天";
        String s2 = getString2(time1);
        String s1 = getString1("白名单车辆");
        String[] split2 = s2.split(",");
        String[] split1 = s1.split(",");
        String s5 = voicePlayback(data, "02");
        String[] split5 = s5.split(",");

        String a="{\n" +
                "\"Response_AlarmInfoPlate\": {\n" +
                "\"info\":\"ok\",\n" +
                "\"channelNum\" : 0, \n" +
                "\"manualTrigger\" : \"no\",\n" +
                "\"is_pay\":\"false\",\n" +
                "\"serialData\" :[\n" +
                "{\n" +
                "\"serialChannel\":0,\n" +
                "\"data\" : \""+split1[0]+"\",\n" +
                "\"dataLen\" : "+split1[1]+"\n" +
                "},\n" +
                "{ \n" +
                "\"serialChannel\":0,\n" +
                "\"data\" : \""+split2[0]+"\",\n" +
                "\"dataLen\" : "+split2[1]+"\n" +
                "},\n" +
                "{ \n" +
                "\"serialChannel\":0,\n" +
                "\"data\" : \""+split5[0]+"\",\n" +
                "\"dataLen\" : "+split5[1]+"\n" +
                "}\n" +
                "]\n" +
                "}\n" +
                "}";
        return a;
    }
    //特定车辆
    public static String specificvehicleexit(String data) {
        String s4 = getString4("减速慢行");
        String s3 = getString3("一车一杆");
        String s2 = getString2(data);
        String s1 = getString1("特定车辆");
        String[] split2 = s2.split(",");
        String[] split1 = s1.split(",");
        String[] split3 = s3.split(",");
        String[] split4 = s4.split(",");
        String s5 = voicePlayback(data, "02");
        String[] split5 = s5.split(",");
        String a="{\n" +
                "\"Response_AlarmInfoPlate\": {\n" +
                "\"info\":\"ok\",\n" +
                "\"channelNum\" : 0, \n" +
                "\"manualTrigger\" : \"no\",\n" +
                "\"is_pay\":\"false\",\n" +
                "\"serialData\" :[\n" +
                "{\n" +
                "\"serialChannel\":0,\n" +
                "\"data\" : \""+split1[0]+"\",\n" +
                "\"dataLen\" : "+split1[1]+"\n" +
                "},\n" +
                "{ \n" +
                "\"serialChannel\":0,\n" +
                "\"data\" : \""+split2[0]+"\",\n" +
                "\"dataLen\" : "+split2[1]+"\n" +
                "},\n" +
                "{ \n" +
                "\"serialChannel\":0,\n" +
                "\"data\" : \""+split3[0]+"\",\n" +
                "\"dataLen\" : "+split3[1]+"\n" +
                "},\n" +
                "{ \n" +
                "\"serialChannel\":0,\n" +
                "\"data\" : \""+split4[0]+"\",\n" +
                "\"dataLen\" : "+split4[1]+"\n" +
                "},\n" +
                "{ \n" +
                "\"serialChannel\":0,\n" +
                "\"data\" : \""+split5[0]+"\",\n" +
                "\"dataLen\" : "+split5[1]+"\n" +
                "}\n" +
                "]\n" +
                "}\n" +
                "}";
        return a;
    }
    //停车场设备不可用
    public static String equipmentAbnormality() {
        String s1 = getString1("设备异常");
        String[] split1 = s1.split(",");
        String a="{\n" +
                "\"Response_AlarmInfoPlate\": {\n" +
                "\"info\":\"no\",\n" +
                "\"channelNum\" : 0, \n" +
                "\"manualTrigger\" : \"no\",\n" +
                "\"is_pay\":\"false\",\n" +
                "\"serialData\" :[\n" +
                "{\n" +
                "\"serialChannel\":0,\n" +
                "\"data\" : \""+split1[0]+"\",\n" +
                "\"dataLen\" : "+split1[1]+"\n" +
                "}\n" +
                "]\n" +
                "}\n" +
                "}";
        return a;
    }
    //停车场不可用
    public static String abnormal() {
        String s1 = getString1("停车场不可用");
        String[] split1 = s1.split(",");

        String a="{\n" +
                "\"Response_AlarmInfoPlate\": {\n" +
                "\"info\":\"no\",\n" +
                "\"channelNum\" : 0, \n" +
                "\"manualTrigger\" : \"no\",\n" +
                "\"is_pay\":\"false\",\n" +
                "\"serialData\" :[\n" +
                "{\n" +
                "\"serialChannel\":0,\n" +
                "\"data\" : \""+split1[0]+"\",\n" +
                "\"dataLen\" : "+split1[1]+"\n" +
                "}\n" +
                "]\n" +
                "}\n" +
                "}";
        return a;
    }
    //临时车禁止进入
    public static String temporaryVehicle(String data) {
        String s2 = getString2(data);
        String s1 = getString1("临时车限制进入");
        String[] split2 = s2.split(",");
        String[] split1 = s1.split(",");
        String s5 = voicePlayback(data, "65");
        String[] split5 = s5.split(",");
        String a="{\n" +
                "\"Response_AlarmInfoPlate\": {\n" +
                "\"info\":\"no\",\n" +
                "\"channelNum\" : 0, \n" +
                "\"manualTrigger\" : \"no\",\n" +
                "\"is_pay\":\"false\",\n" +
                "\"serialData\" :[\n" +
                "{\n" +
                "\"serialChannel\":0,\n" +
                "\"data\" : \""+split1[0]+"\",\n" +
                "\"dataLen\" : "+split1[1]+"\n" +
                "},\n" +
                "{ \n" +
                "\"serialChannel\":0,\n" +
                "\"data\" : \""+split2[0]+"\",\n" +
                "\"dataLen\" : "+split2[1]+"\n" +
                "},\n" +
                "{ \n" +
                "\"serialChannel\":0,\n" +
                "\"data\" : \""+split5[0]+"\",\n" +
                "\"dataLen\" : "+split5[1]+"\n" +
                "}\n" +
                "]\n" +
                "}\n" +
                "}";
        return a;
    }
    //黑名单拒绝进入
    public static String blacklist(String data) {
        String s2 = getString2(data);
        String s1 = getString1("黑名单车辆");
        String[] split2 = s2.split(",");
        String[] split1 = s1.split(",");
        String s4 = getString4("减速慢行");
        String s3 = getString3("一车一杆");
        String[] split3 = s3.split(",");
        String[] split4 = s4.split(",");
        String s5 = voicePlayback("禁止进入", "09");
        String[] split5 = s5.split(",");
        String a="{\n" +
                "\"Response_AlarmInfoPlate\": {\n" +
                "\"info\":\"no\",\n" +
                "\"channelNum\" : 0, \n" +
                "\"manualTrigger\" : \"no\",\n" +
                "\"is_pay\":\"false\",\n" +
                "\"serialData\" :[\n" +
                "{\n" +
                "\"serialChannel\":0,\n" +
                "\"data\" : \""+split1[0]+"\",\n" +
                "\"dataLen\" : "+split1[1]+"\n" +
                "},\n" +
                "{ \n" +
                "\"serialChannel\":0,\n" +
                "\"data\" : \""+split2[0]+"\",\n" +
                "\"dataLen\" : "+split2[1]+"\n" +
                "},\n" +
                "{ \n" +
                "\"serialChannel\":0,\n" +
                "\"data\" : \""+split5[0]+"\",\n" +
                "\"dataLen\" : "+split5[1]+"\n" +
                "}\n" +
                "]\n" +
                "}\n" +
                "}";
        return a;
    }
    //进口下发临显指令
    public static String addSerialPort(String data,Long manth) {
        String s4 = getString4("减速慢行");
        String s3 = getString2("剩余车位:"+manth);
        String s2 = getString3(data);
        String s1 = getString1("欢迎光临");
        String s5 = voicePlayback(data,"01");
        String[] split2 = s2.split(",");
        String[] split1 = s1.split(",");
        String[] split3 = s3.split(",");
        String[] split4 = s4.split(",");
        String[] split5 = s5.split(",");

        String a="{\n" +
                "\"Response_AlarmInfoPlate\": {\n" +
                "\"info\":\"ok\",\n" +
                "\"channelNum\" : 0, \n" +
                "\"manualTrigger\" : \"no\",\n" +
                "\"is_pay\":\"false\",\n" +
                "\"serialData\" :[\n" +
                "{\n" +
                "\"serialChannel\":0,\n" +
                "\"data\" : \""+split1[0]+"\",\n" +
                "\"dataLen\" : "+split1[1]+"\n" +
                "},\n" +
                "{ \n" +
                "\"serialChannel\":0,\n" +
                "\"data\" : \""+split2[0]+"\",\n" +
                "\"dataLen\" : "+split2[1]+"\n" +
                "},\n" +
                "{ \n" +
                "\"serialChannel\":0,\n" +
                "\"data\" : \""+split3[0]+"\",\n" +
                "\"dataLen\" : "+split3[1]+"\n" +
                "},\n" +
                "{ \n" +
                "\"serialChannel\":0,\n" +
                "\"data\" : \""+split4[0]+"\",\n" +
                "\"dataLen\" : "+split4[1]+"\n" +
                "},\n" +
                "{ \n" +
                "\"serialChannel\":0,\n" +
                "\"data\" : \""+split5[0]+"\",\n" +
                "\"dataLen\" : "+split5[1]+"\n" +
                "}\n" +
                "]\n" +
                "}\n" +
                "}";
        return a;
    }
    //进出口临显第4行
    private static String getString4(String data) {
        String test=null;
        try {
            test = Hex.test( data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //转换为byte数组
        byte[] bytes = Hex.toByteArray(test);
        //长度
        int length = bytes.length+2;
        //长度转换16进制
        String s3 = numToHex16(length);
        log.info("长度="+s3);
        String s = "04640020"+ s3+"040A"+test+"0000";
        log.info(s);
        String s1 = "04640020"+ s3+"040A"+test;
        byte[] bytes1 = Hex.toByteArray(s);
        int i = CRCUtil.calcCrc16(bytes1);
        String crc = String.format("%04x", i);
        System.out.println("校验位"+crc);
        String s4 = "AA55" + s1 + crc + "AF";
        log.info("进出口临显第4行="+s4);
        byte[] bytes2 = Hex.toByteArray(s4);
        int length1 = bytes2.length;
        byte base64_data[] = Base64.getEncoder().encode(bytes2);
        String base64_str = new String(base64_data);
        return base64_str+','+length1;
    }
    //进出口临显第3行
    private static String getString3(String data) {
        String test=null;
        try {
            test = Hex.test( data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //转换为byte数组
        byte[] bytes = Hex.toByteArray(test);
        //长度
        int length = bytes.length+2;
        //长度转换16进制
        String s3 = numToHex16(length);
        log.info("长度="+s3);
        String s = "03640020"+ s3+"030A"+test+"0000";
        log.info(s);
        String s1 = "03640020"+ s3+"030A"+test;
        byte[] bytes1 = Hex.toByteArray(s);
        int i = CRCUtil.calcCrc16(bytes1);
        String crc = String.format("%04x", i);
        System.out.println("校验位"+crc);
        String s4 = "AA55" + s1 + crc + "AF";
        log.info("进出口临显第3行="+s4);
        byte[] bytes2 = Hex.toByteArray(s4);
        int length1 = bytes2.length;
        byte base64_data[] = Base64.getEncoder().encode(bytes2);
        String base64_str = new String(base64_data);
        return base64_str+','+length1;
    }
    //进出口临显第2行
    private static String getString2(String data) {
        String test=null;
        try {
            test = Hex.test( data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //转换为byte数组
        byte[] bytes = Hex.toByteArray(test);
        //长度
        int length = bytes.length+2;
        //长度转换16进制
        String s3 = numToHex16(length);
        log.info("长度="+s3);
        String s =  "02640020"+ s3+"020A"+test+"0000";
        log.info(s);
        String s1 = "02640020"+ s3+"020A"+test;
        byte[] bytes1 = Hex.toByteArray(s);
        int i = CRCUtil.calcCrc16(bytes1);
        String crc = String.format("%04x", i);
        System.out.println("校验位"+crc);
        String s4 = "AA55" + s1 + crc + "AF";
        log.info("进出口临显第2行="+s4);
        log.info(s4);
        byte[] bytes2 = Hex.toByteArray(s4);
        int length1 = bytes2.length;
        byte base64_data[] = Base64.getEncoder().encode(bytes2);
        String base64_str = new String(base64_data);
        return base64_str+','+length1;
    }
    //进出口临显第1行
    private static String getString1(String data) {

        String test=null;
        try {
            test = Hex.test( data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //转换为byte数组
        byte[] bytes = Hex.toByteArray(test);
        //长度
        int length = bytes.length+2;
        //长度转换16进制
        String s3 = numToHex16(length);
        log.info("长度="+s3);
        String s = "01640020"+ s3+"010A"+test+"0000";
        log.info(s);
        String s1 = "01640020"+ s3+"010A"+test;
        byte[] bytes1 = Hex.toByteArray(s);
        int i = CRCUtil.calcCrc16(bytes1);
        String crc = String.format("%04x", i);
        System.out.println("校验位"+crc);
        String s4 = "AA55" + s1 + crc + "AF";
        log.info("进出口临显第1行="+s4);
        byte[] bytes2 = Hex.toByteArray(s4);
        int length1 = bytes2.length;
        byte base64_data[] = Base64.getEncoder().encode(bytes2);
        String base64_str = new String(base64_data);
        return base64_str+','+length1;
    }

    private static String getStringOne(String data,String math) {
        String test=null;
        try {
            test = Hex.test( data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //转换为byte数组
        byte[] bytes = Hex.toByteArray(test);
        //长度
        int length = bytes.length+2;
        //长度转换16进制
        String s3 = numToHex16(length);
        log.info("长度="+s3);
        String s = "01640020"+ s3+math+test+"0000";
        log.info(s);
        String s1 = "01640020"+ s3+math+test;
        byte[] bytes1 = Hex.toByteArray(s);
        int i = CRCUtil.calcCrc16(bytes1);
        String crc = String.format("%04x", i);
        System.out.println("校验位"+crc);
        String s4 = "AA55" + s1 + crc + "AF";
        log.info("进出口临显第1行="+s4);
        byte[] bytes2 = Hex.toByteArray(s4);
        int length1 = bytes2.length;
        byte base64_data[] = Base64.getEncoder().encode(bytes2);
        String base64_str = new String(base64_data);
        return base64_str+','+length1;

    }
    //语音播放进口
    private static String voicePlayback2(Long l,Long l1,Long l2,String data,String math,Long money) {
        String test="";
        String test1="";
        String test5="";
        try {
            test = Hex.test(data);
            if (l!=0) {
                //天数长度
                String s1 = String.valueOf(l);
                int[] arr = new int[s1.length()];
                for (int i = 0; i < s1.length(); i++) {
                    arr[i] = Integer.parseInt(s1.substring(i, i + 1));//substring是找出包含起始位置，不包含结束位置，到结束位置的前一位的子串
                }
                if (arr.length == 4) {
                    test1 = Hex.test(String.valueOf(arr[0]));
                    String lengh = Hex.test("千");
                    String test2 = Hex.test(String.valueOf(arr[1]));
                    String test3 = Hex.test(String.valueOf(arr[2]));
                    String test4 = Hex.test(String.valueOf(arr[3]));
                    if (arr[1] == 0 && arr[2] == 0 && arr[3] == 0) {
                        test1 = test1 + lengh + "CCEC";
                    } else {
                        if (arr[1] != 0) {
                            test1 = test1 + lengh + test2 + "B0D9";
                        }
                        if (arr[1] == 0) {
                            test1 = test1 + lengh + "30";
                        }
                        if (arr[2] != 0) {
                            test1 = test1 + test3 + "CAAE";
                        } else {
                            if (arr[1] != 0) {
                                test1 = test1 + "30";
                            }
                        }
                        if (arr[3] != 0) {
                            test1 = test1 + test4 + "CCEC";
                        } else {
                            if (arr[2] == 0) {
                                test1 = test1.substring(0, test1.length() - 2) + "CCEC";   //截掉
                            } else {
                                test1 = test1 + "CCEC";
                            }
                        }
                    }
                    System.out.println("test1=" + test1);
                }
                if (arr.length == 3) {
                    test1 = Hex.test(String.valueOf(arr[0]));
                    String test2 = Hex.test(String.valueOf(arr[1]));
                    String test3 = Hex.test(String.valueOf(arr[2]));
                    if (arr[1] == 0 && arr[2] == 0) {
                        test1 = test1 + "b0d9" + "CCEC";
                    } else {
                        if (arr[1] != 0) {
                            test1 = test1 + "b0d9" + test2 + "CAAE";
                        }
                        if (arr[1] == 0) {
                            test1 = test1 + "b0d9" + "30";
                        }
                        if (arr[2] != 0) {
                            test1 = test1 + test3 + "CCEC";
                        } else {
                            if (arr[1] == 0) {
                                test1 = test1.substring(0, test1.length() - 2) + "CCEC";
                            } else {
                                test1 = test1 + "CCEC";
                            }
                        }
                    }
                    System.out.println("test1=" + test1);
                }
                if (arr.length == 2) {
                    test1 = Hex.test(String.valueOf(arr[0]));
                    String test2 = Hex.test(String.valueOf(arr[1]));
                    if (arr[1] == 0) {
                        test1 = test1 + "CAAE" + "CCEC";
                    } else {
                        test1 = test1 + "CAAE" + test2 + "CCEC";
                    }
                    System.out.println("test1=" + test1);
                }
                if (arr.length == 1) {
                    test1 = Hex.test(String.valueOf(arr[0])) + "CCEC";
                    System.out.println("test1=" + test1);
                }
            }
            if (l1!=0) {
                //小时
                String s2 = String.valueOf(l1);
                int length = s2.length();
                int[] ints = new int[length];
                for (int i = 0; i < s2.length(); i++) {
                    ints[i] = Integer.parseInt(s2.substring(i, i + 1));//substring是找出包含起始位置，不包含结束位置，到结束位置的前一位的子串
                }
                if (ints.length == 2) {
                    String test3 = Hex.test(String.valueOf(ints[0]));
                    String test2 = Hex.test(String.valueOf(ints[1]));
                    if (ints[1] == 0) {
                        test1 = test1 + test3 + "CAAE" + "2F";
                    } else {
                        test1 = test1 + test3 + "CAAE" + test2 + "2F";
                    }
                    System.out.println("test1=" + test1);
                }
                if (ints.length == 1) {
                    test1 = test1 + Hex.test(String.valueOf(ints[0])) + "2F";
                    System.out.println("test1=" + test1);
                }
            }
                //分钟
                String s = String.valueOf(l2);
                int[] ints1 = new int[s.length()];
                for (int i = 0; i < s.length(); i++) {
                    ints1[i] = Integer.parseInt(s.substring(i, i + 1));//substring是找出包含起始位置，不包含结束位置，到结束位置的前一位的子串
                }
                if (ints1.length == 2) {
                    String test3 = Hex.test(String.valueOf(ints1[0]));
                    String test2 = Hex.test(String.valueOf(ints1[1]));
                    if (ints1[1] == 0) {
                        test1 = test1 + test3 + "CAAE" + "B7D6";
                    } else {
                        test1 = test1 + test3 + "CAAE" + test2 + "B7D6";
                    }
                    System.out.println("test1=" + test1);
                }
                if (ints1.length == 1) {
                    test1 = test1 + Hex.test(String.valueOf(ints1[0])) + "B7D6";
                    System.out.println("test1=" + test1);
                }
            /*上面是时间下面是金额*/
            String s3 = NumberTo.num2Ch(money);
            String s4 = s3.replace("零", "0");
            String s5 = s4.replace("一", "1");
            String s6 =s5.replace("二", "2");
            String s7 =s6.replace("三", "3");
            String s8 =s7.replace("四", "4");
            String s9 =s8.replace("五", "5");
            String s10 =s9.replace("六", "6");
            String s11 =s10.replace("七", "7");
            String s12 =s11.replace("八", "8");
            String s13 =s12.replace("九", "9");
            test5 = Hex.test(s13+"元");
            log.info("test5="+test5);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String s2 = test + "6d6e6f" + test1+math+test5;
        //转换为byte数组
        byte[] bytes = Hex.toByteArray(s2);
        //长度
        int length = bytes.length;
        //长度转换16进制
        String s3 = numToHex16(length);
        log.info("长度="+s3);
        String s = "00640022"+ s3+s2+"0000";
        log.info(s);
        String s1 = "00640022"+ s3+s2;
        byte[] bytes1 = Hex.toByteArray(s);
        int i = CRCUtil.calcCrc16(bytes1);
        String crc = String.format("%04x", i);
        System.out.println("校验位"+crc);
        String s4 = "AA55" + s1 + crc + "AF";
        log.info("语音播放指令="+s4);
        byte[] bytes2 = Hex.toByteArray(s4);
        int length1 = bytes2.length;
        byte base64_data[] = Base64.getEncoder().encode(bytes2);
        String base64_str = new String(base64_data);
        return base64_str+','+length1;
    }
    private static String voicePlayback1(Long l,Long l1,Long l2,String data,String math,Long money) {
        String test="";
        String test1="";
        String test5="";
        try {
            test = Hex.test( data);
            if (l!=0) {
                //天数长度
                String s1 = String.valueOf(l);
                int[] arr = new int[s1.length()];
                for (int i = 0; i < s1.length(); i++) {
                    arr[i] = Integer.parseInt(s1.substring(i, i + 1));//substring是找出包含起始位置，不包含结束位置，到结束位置的前一位的子串
                }
                if (arr.length == 4) {
                    test1 = Hex.test(String.valueOf(arr[0]));
                    String lengh = Hex.test("千");
                    String test2 = Hex.test(String.valueOf(arr[1]));
                    String test3 = Hex.test(String.valueOf(arr[2]));
                    String test4 = Hex.test(String.valueOf(arr[3]));
                    if (arr[1] == 0 && arr[2] == 0 && arr[3] == 0) {
                        test1 = test1 + lengh + "CCEC";
                    } else {
                        if (arr[1] != 0) {
                            test1 = test1 + lengh + test2 + "B0D9";
                        }
                        if (arr[1] == 0) {
                            test1 = test1 + lengh + "30";
                        }
                        if (arr[2] != 0) {
                            test1 = test1 + test3 + "CAAE";
                        } else {
                            if (arr[1] != 0) {
                                test1 = test1 + "30";
                            }
                        }
                        if (arr[3] != 0) {
                            test1 = test1 + test4 + "CCEC";
                        } else {
                            if (arr[2] == 0) {
                                test1 = test1.substring(0, test1.length() - 2) + "CCEC";   //截掉
                            } else {
                                test1 = test1 + "CCEC";
                            }
                        }
                    }
                    System.out.println("test1=" + test1);
                }
                if (arr.length == 3) {
                    test1 = Hex.test(String.valueOf(arr[0]));
                    String test2 = Hex.test(String.valueOf(arr[1]));
                    String test3 = Hex.test(String.valueOf(arr[2]));
                    if (arr[1] == 0 && arr[2] == 0) {
                        test1 = test1 + "b0d9" + "CCEC";
                    } else {
                        if (arr[1] != 0) {
                            test1 = test1 + "b0d9" + test2 + "CAAE";
                        }
                        if (arr[1] == 0) {
                            test1 = test1 + "b0d9" + "30";
                        }
                        if (arr[2] != 0) {
                            test1 = test1 + test3 + "CCEC";
                        } else {
                            if (arr[1] == 0) {
                                test1 = test1.substring(0, test1.length() - 2) + "CCEC";
                            } else {
                                test1 = test1 + "CCEC";
                            }
                        }
                    }
                    System.out.println("test1=" + test1);
                }
                if (arr.length == 2) {
                    test1 = Hex.test(String.valueOf(arr[0]));
                    String test2 = Hex.test(String.valueOf(arr[1]));
                    if (arr[1] == 0) {
                        test1 = test1 + "CAAE" + "CCEC";
                    } else {
                        test1 = test1 + "CAAE" + test2 + "CCEC";
                    }
                    System.out.println("test1=" + test1);
                }
                if (arr.length == 1) {
                    test1 = Hex.test(String.valueOf(arr[0])) + "CCEC";
                    System.out.println("test1=" + test1);
                }
            }
            if (l1!=0){
                //小时
                String s2 = String.valueOf(l1);
                int length = s2.length();
                int[] ints = new int[length];
                for (int i = 0; i < s2.length(); i++) {
                    ints[i] = Integer.parseInt(s2.substring(i, i + 1));//substring是找出包含起始位置，不包含结束位置，到结束位置的前一位的子串
                }
                if (ints.length==2){
                    String test3 = Hex.test(String.valueOf(ints[0]));
                    String test2 = Hex.test(String.valueOf(ints[1]));
                    if (ints[1]==0){
                        test1=test1+test3 + "CAAE"+"2F";
                    }else {
                        test1=test1+test3 + "CAAE"+test2+"2F";
                    }
                    System.out.println("test1="+test1);
                }
                if (ints.length==1){
                    test1 = test1+Hex.test(String.valueOf(ints[0]))+"2F";
                    System.out.println("test1="+test1);
                }
            }

            //分钟
            String s = String.valueOf(l2);
            int[] ints1 = new int[s.length()];
            for (int i = 0; i < s.length(); i++) {
                ints1[i] = Integer.parseInt(s.substring(i, i + 1));//substring是找出包含起始位置，不包含结束位置，到结束位置的前一位的子串
            }
            if (ints1.length==2){
                String test3 = Hex.test(String.valueOf(ints1[0]));
                String test2 = Hex.test(String.valueOf(ints1[1]));
                if (ints1[1]==0){
                    test1=test1+test3 + "CAAE"+"B7D6";
                }else {
                    test1=test1+test3 + "CAAE"+test2+"B7D6";
                }
                System.out.println("test1="+test1);
            }
            if (ints1.length==1){
                test1 =test1+ Hex.test(String.valueOf(ints1[0]))+"B7D6";
                System.out.println("test1="+test1);
            }
            /*上面是时间下面是金额*/
            String s3 = NumberTo.num2Ch(money);
            String s4 = s3.replace("零", "0");
            String s5 = s4.replace("一", "1");
            String s6 =s5.replace("二", "2");
            String s7 =s6.replace("三", "3");
            String s8 =s7.replace("四", "4");
            String s9 =s8.replace("五", "5");
            String s10 =s9.replace("六", "6");
            String s11 =s10.replace("七", "7");
            String s12 =s11.replace("八", "8");
            String s13 =s12.replace("九", "9");
            test5 = Hex.test(s13+"元");
             log.info("test5="+test5);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String s2 = test + "2e" + test1+math+test5;
        //转换为byte数组
        byte[] bytes = Hex.toByteArray(s2);
        //长度
        int length = bytes.length;
        //长度转换16进制
        String s3 = numToHex16(length);
        log.info("长度="+s3);
        String s = "00640022"+ s3+s2+"0000";
        log.info(s);
        String s1 = "00640022"+ s3+s2;
        byte[] bytes1 = Hex.toByteArray(s);
        int i = CRCUtil.calcCrc16(bytes1);
        String crc = String.format("%04x", i);
        System.out.println("校验位"+crc);
        String s4 = "AA55" + s1 + crc + "AF";
        log.info("语音播放指令="+s4);
        byte[] bytes2 = Hex.toByteArray(s4);
        int length1 = bytes2.length;
        byte base64_data[] = Base64.getEncoder().encode(bytes2);
        String base64_str = new String(base64_data);
        return base64_str+','+length1;
    }
    private static String voicePlayback(String data,String math) {
        String test=null;
        try {
            test = Hex.test( data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //转换为byte数组
        byte[] bytes = Hex.toByteArray(test);
        //长度
        int length = bytes.length;
        //长度转换16进制
        String s3 = numToHex16(length+1);
        log.info("长度="+s3);
        String s = "00640022"+ s3+test+math+"0000";
        log.info(s);
        String s1 = "00640022"+ s3+test+math;
        byte[] bytes1 = Hex.toByteArray(s);
        int i = CRCUtil.calcCrc16(bytes1);
        String crc = String.format("%04x", i);
        System.out.println("校验位"+crc);
        String s4 = "AA55" + s1 + crc + "AF";
        log.info("语音播放指令="+s4);
        byte[] bytes2 = Hex.toByteArray(s4);
        int length1 = bytes2.length;
        byte base64_data[] = Base64.getEncoder().encode(bytes2);
        String base64_str = new String(base64_data);
        return base64_str+','+length1;
    }
    //支付后语音播放
    private static String payAfter(String data,String math) {
        String test=null;
        try {
            test = Hex.test( data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //转换为byte数组
        byte[] bytes = Hex.toByteArray(test);
        //长度
        int length = bytes.length;
        //长度转换16进制
        String s3 = numToHex16(length+1);
        log.info("长度="+s3);
        String s = "00640022"+ s3+test+math+"0000";
        log.info(s);
        String s1 = "00640022"+ s3+test+math;
        byte[] bytes1 = Hex.toByteArray(s);
        int i = CRCUtil.calcCrc16(bytes1);
        String crc = String.format("%04x", i);
        System.out.println("校验位"+crc);
        String s4 = "AA55" + s1 + crc + "AF";
        log.info("支付后语音播放指令="+s4);
        byte[] bytes2 = Hex.toByteArray(s4);
        int length1 = bytes2.length;
        byte base64_data[] = Base64.getEncoder().encode(bytes2);
        String base64_str = new String(base64_data);
        return base64_str+','+length1;

    }
    //需要使用2字节表示b
    public static String numToHex16(int b) {
        return String.format("%04x", b);
    }

    public static String advertisement(ParkingLotEquipment parkingLotEquipment) {
        String onedisplay = parkingLotEquipment.getOnedisplay();
        String s1 = advertisement1(onedisplay,"01");
        String twodisplay = parkingLotEquipment.getTwodisplay();
        String s2 = advertisement1(twodisplay,"02");
        String threedisplay = parkingLotEquipment.getThreedisplay();
        String s3 = advertisement1(threedisplay,"03");
        String fourdisplay = parkingLotEquipment.getFourdisplay();
        String s4 = advertisement1(fourdisplay,"04");
        String s5 = advertisement5(String.valueOf(parkingLotEquipment.getVolume()));
        String[] split1 = s1.split(",");
        String[] split2 = s2.split(",");
        String[] split3 = s3.split(",");
        String[] split4 = s4.split(",");
        String[] split5 = s5.split(",");

        String a="{\n" +
                "\"Response_AlarmInfoPlate\": {\n" +
                "\"info\":\"no\",\n" +
                "\"channelNum\" : 0, \n" +
                "\"manualTrigger\" : \"no\",\n" +
                "\"is_pay\":\"false\",\n" +
                "\"serialData\" :[\n" +
                "{\n" +
                // TODO: 2023/2/7   .......................................... 
                "\"serialChannel\":0,\n" +
                "\"data\" : \""+split1[0]+"\",\n" +
                "\"dataLen\" : "+split1[1]+"\n" +
                "},\n" +
                "{ \n" +
                "\"serialChannel\":1,\n" +
                "\"data\" : \""+split2[0]+"\",\n" +
                "\"dataLen\" : "+split2[1]+"\n" +
                "},\n" +
                "{ \n" +
                "\"serialChannel\":0,\n" +
                "\"data\" : \""+split3[0]+"\",\n" +
                "\"dataLen\" : "+split3[1]+"\n" +
                "},\n" +
                "{ \n" +
                "\"serialChannel\":1,\n" +
                "\"data\" : \""+split4[0]+"\",\n" +
                "\"dataLen\" : "+split4[1]+"\n" +
                "},\n" +
                "{ \n" +
                "\"serialChannel\":0,\n" +
                "\"data\" : \""+split5[0]+"\",\n" +
                "\"dataLen\" : "+split5[1]+"\n" +
                "},\n" +
                "{ \n" +
                "\"serialChannel\":0,\n" +
                "\"data\" : \""+split2[0]+"\",\n" +
                "\"dataLen\" : "+split2[1]+"\n" +
                "},\n" +
                "{ \n" +
                "\"serialChannel\":0,\n" +
                "\"data\" : \""+split2[0]+"\",\n" +
                "\"dataLen\" : "+split2[1]+"\n" +
                "},\n" +
                "{ \n" +
                "\"serialChannel\":0,\n" +
                "\"data\" : \""+split4[0]+"\",\n" +
                "\"dataLen\" : "+split4[1]+"\n" +
                "},\n" +
                "{ \n" +
                "\"serialChannel\":0,\n" +
                "\"data\" : \""+split4[0]+"\",\n" +
                "\"dataLen\" : "+split4[1]+"\n" +
                "}\n" +
                "]\n" +
                "}\n" +
                "}";
        return a;
    }
   /* //广告位第4行
    private static String advertisement4(String data) {
        String test=null;
        try {
            test = Hex.test( data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //转换为byte数组
        byte[] bytes = Hex.toByteArray(test);
        //长度
        int length = bytes.length+1;
        //长度转换16进制
        String s3 = numToHex16(length);
        //  AA55 25 64 0011 0009 04 BCF5CBD9C2FDD0D060F5AF
        log.info("长度="+s3);
        String s = "03640011"+ s3+"04"+test+"0000";
        log.info(s);
        String s1 = "03640011"+ s3+"04"+test;
        byte[] bytes1 = Hex.toByteArray(s);
        int i = CRCUtil.calcCrc16(bytes1);
        String crc = String.format("%04x", i);
        System.out.println("校验位"+crc);
        String s4 = "AA55" + s1 + crc + "AF";
        log.info("广告位第四行指令集="+s4);
        byte[] bytes2 = Hex.toByteArray(s4);
        int length1 = bytes2.length;
        byte base64_data[] = Base64.getEncoder().encode(bytes2);
        String base64_str = new String(base64_data);
        return base64_str+','+length1;
    }
    //广告位第3行
    private static String advertisement3(String data) {
        String test=null;
        try {
            test = Hex.test( data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //转换为byte数组
        byte[] bytes = Hex.toByteArray(test);
        //长度
        int length = bytes.length+1;
        //长度转换16进制
        String s3 = numToHex16(length);
        log.info("长度="+s3);
        String s = "02640011"+ s3+"03"+test+"0000";
        log.info(s);
        String s1 = "02640011"+ s3+"03"+test;
        byte[] bytes1 = Hex.toByteArray(s);
        int i = CRCUtil.calcCrc16(bytes1);
        String crc = String.format("%04x", i);
        System.out.println("校验位"+crc);
        String s4 = "AA55" + s1 + crc + "AF";
        log.info("广告位第三行指令集="+s4);
        byte[] bytes2 = Hex.toByteArray(s4);
        int length1 = bytes2.length;
        byte base64_data[] = Base64.getEncoder().encode(bytes2);
        String base64_str = new String(base64_data);
        return base64_str+','+length1;
    }
    //广告位第2行
    private static String advertisement2(String data) {
        String test=null;
        try {
            test = Hex.test( data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //转换为byte数组
        byte[] bytes = Hex.toByteArray(test);
        //长度
        int length = bytes.length+1;
        //长度转换16进制
        String s3 = numToHex16(length);
        log.info("长度="+s3);
        String s = "00640011"+ s3+"02"+test+"0000";
        log.info(s);
        String s1 = "00640011"+ s3+"02"+test;
        byte[] bytes1 = Hex.toByteArray(s);
        int i = CRCUtil.calcCrc16(bytes1);
        String crc = String.format("%04x", i);
        System.out.println("校验位"+crc);
        String s4 = "AA55" + s1 + crc + "AF";
        log.info("广告位第2行指令集="+s4);
        byte[] bytes2 = Hex.toByteArray(s4);
        int length1 = bytes2.length;
        byte base64_data[] = Base64.getEncoder().encode(bytes2);
        String base64_str = new String(base64_data);
        return base64_str+','+length1;
    }*/
    //广告位第1行
    private static String advertisement1(String data,String math) {
        String test=null;
        try {
            test = Hex.test( data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //转换为byte数组
        byte[] bytes = Hex.toByteArray(test);
        //长度
        int length = bytes.length+1;
        //长度转换16进制
        String s3 = numToHex16(length);

        String s = "00640011"+ s3+math+test+"0000";

        String s1 = "00640011"+ s3+math+test;
        byte[] bytes1 = Hex.toByteArray(s);
        int i = CRCUtil.calcCrc16(bytes1);
        String crc = String.format("%04x", i);
        System.out.println("校验位"+crc);
        String s4 = "AA55" + s1 + crc + "AF";
        log.info("广告位指令集="+s4);
        byte[] bytes2 = Hex.toByteArray(s4);
        int length1 = bytes2.length;
        byte base64_data[] = Base64.getEncoder().encode(bytes2);
        String base64_str = new String(base64_data);
        return base64_str+','+length1;
    }
    /*--16:30:47:476 发送   AA55 05 6400 11000901BBB6D3ADB9E2C1D99517AF

       --16:30:49:551 发送  AA55 06 6400 11 00 09 02 B3 B5 C5 C6 CA B6 B1 F0 1CD7AF
                           AA5500640011000902b3b5c5c6cab6b1f0bd34AF
AA55 0E 6400 11 00 02 02 33 4602AF
AA55 00 6400 11 00 02 02 33 b363AF
       --16:30:51:616 发送  AA55 07 6400 11 00 09 03 D2 BB B3 B5 D2 BB B8 CB EA05AF
                           AA5500640011000903d2bbb3b5d2bbb8cbdbb7AF
     --16:30:53:692 发送    AA55 08 6400 11 00 09 04BCF5CBD9C2FDD0D03978AF

     */
    //设置音量
    private static String advertisement5(String data) {
        String s = "006400F000010"+data+"0000";
        log.info(s);
        String s1 = "006400F000010"+data;
        byte[] bytes1 = Hex.toByteArray(s);
        int i = CRCUtil.calcCrc16(bytes1);
        String crc = String.format("%04x", i);
        System.out.println("校验位"+crc);
        String s4 = "AA55" + s1 + crc + "AF";
        log.info("音量指令集="+s4);
        byte[] bytes2 = Hex.toByteArray(s4);
        int length1 = bytes2.length;
        byte base64_data[] = Base64.getEncoder().encode(bytes2);
        String base64_str = new String(base64_data);
        return base64_str+','+length1;
    }
    //心跳
    private static String heartbeat() {

        String s = "006400010000"+"0000";
        log.info(s);
        String s1 = "006400010000";
        byte[] bytes1 = Hex.toByteArray(s);
        int i = CRCUtil.calcCrc16(bytes1);
        String crc = String.format("%04x", i);
        System.out.println("校验位"+crc);
        String s4 = "AA55"+s1+crc+"AF";
        log.info("心跳指令集="+s4);
        byte[] bytes2 = Hex.toByteArray(s4);
        int length1 = bytes2.length;
        byte base64_data[] = Base64.getEncoder().encode(bytes2);
        String base64_str = new String(base64_data);
        return base64_str+','+length1;
    }
    //心跳
    public static String returnHeartbeat() {
        //调用上一个方法
        String s1 = heartbeat();
        String[] split1 = s1.split(",");
        String a="{\n" +
                "\"Response_AlarmInfoPlate\": {\n" +
                "\"serialData\" :[\n" +
                "{\n" +
                "\"serialChannel\":0,\n" +
                "\"data\" : \""+split1[0]+"\",\n" +
                "\"dataLen\" : "+split1[1]+"\n" +
                "}\n" +
                "]\n" +
                "}\n" +
                "}";
        return a;
    }
    //测试
    public static void main(String[] args)  {
        //AA5500640011000902d2bbb3b5d2bbb8cb27b3AF
        //voicePlayback1(23L,15L,56L,"粤B12345","0b",22L);
         advertisement1("一车一杆","01");
        ///addSerialPort("欢迎光临");
        //addSerialPort("ss");
        //getString2("23天15小时56分");
    }

}
