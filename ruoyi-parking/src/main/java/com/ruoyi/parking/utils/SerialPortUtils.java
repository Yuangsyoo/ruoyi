package com.ruoyi.parking.utils;
import com.ruoyi.common.utils.Hex;
import com.ruoyi.parking.domain.ParkingLotEquipment;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: yangyang
 * @Date: 2023/01/04/9:30
 * @Description:
 */
@Slf4j
public class SerialPortUtils {
    //出口支付后临显指令
    public static  List<byte[]> payAfter(String data) {
        byte[] s4 = getStringOne("减速慢行", "040A");
        byte[] s3 = getStringOne("一车一杆", "030A");
        byte[] s2 = getStringOne(data, "020A");
        byte[] s1 = getStringOne("一路顺风","010A");
        byte[] s5 = payAfter(data, "02");
        List<byte[]> list = new ArrayList<>();
        list.add(s1);
        list.add(s2);
        list.add(s3);
        list.add(s4);
        list.add(s5);
        return list;

    }
    //出口待缴费临显指令
    public static String ExitOne(String data,Long time,Long money)  {

        //停车小时
        long l = time /60;
        //停车剩余分钟
        long l1 = time % 60;
        String s4 = getString4("待支付金额:"+money);
        String s3 = getString3("请扫码缴费");
        String s2 = getString2(data);
        String s1 = getString1("停车时长:"+l+"小时"+l1+"分钟");
        String[] split2 = s2.split(",");
        String[] split1 = s1.split(",");
        String[] split3 = s3.split(",");
        String[] split4 = s4.split(",");
        String s5 = voicePlayback(data, "0b");
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
    //超出出场时间补费
    public static String overtime(String data,Long money) {

        String s4 = getString4("减速慢行");
        String s3 = getString3("需要补缴费用:"+money+"元");
        String s2 = getString2(data);
        String s1 = getString1("超出出场时间");
        String[] split2 = s2.split(",");
        String[] split1 = s1.split(",");
        String[] split3 = s3.split(",");
        String[] split4 = s4.split(",");
        String s5 = voicePlayback(data, "0b");
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
        String s1 = getString1("黑名单车辆拒绝进入");
        String[] split2 = s2.split(",");
        String[] split1 = s1.split(",");
        String s4 = getString4("减速慢行");
        String s3 = getString3("一车一杆");
        String[] split3 = s3.split(",");
        String[] split4 = s4.split(",");
        String s5 = voicePlayback(data, "09");
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
        log.info("ss="+s4);
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
        log.info("ss="+s4);
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
        log.info("ss2="+s4);
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
        log.info("ss="+s4);
        byte[] bytes2 = Hex.toByteArray(s4);
        int length1 = bytes2.length;
        byte base64_data[] = Base64.getEncoder().encode(bytes2);
        String base64_str = new String(base64_data);
        return base64_str+','+length1;
    }

    private static byte[] getStringOne(String data,String math) {
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
        log.info("ss="+s4);
        byte[] bytes2 = Hex.toByteArray(s4);
        return bytes2;
    }
    //语音播放进口
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
        log.info("ss="+s4);
        byte[] bytes2 = Hex.toByteArray(s4);
        int length1 = bytes2.length;
        byte base64_data[] = Base64.getEncoder().encode(bytes2);
        String base64_str = new String(base64_data);
        return base64_str+','+length1;
    }
    //支付后语音播放进口
    private static byte[] payAfter(String data,String math) {
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
        log.info("ss="+s4);
        byte[] bytes2 = Hex.toByteArray(s4);
        return bytes2;
    }
    //需要使用2字节表示b
    public static String numToHex16(int b) {
        return String.format("%04x", b);
    }
    //测试
    public static void main(String[] args)  {
        voicePlayback("粤B12345","01");
        // addSerialPort("一车一杆");
        ///addSerialPort("欢迎光临");
        //addSerialPort("ss");
    }


    public static List<byte[]> advertisement(ParkingLotEquipment parkingLotEquipment) {
        ArrayList<byte[]> list = new ArrayList<>();
        String onedisplay = parkingLotEquipment.getOnedisplay();
        if (onedisplay!=null||!onedisplay.equals("")){
            byte[] bytes = advertisement1(onedisplay);
            list.add(bytes);
        }
        String twodisplay = parkingLotEquipment.getTwodisplay();
        if (twodisplay!=null||!twodisplay.equals("")){
            byte[] bytes1 = advertisement2(twodisplay);
            list.add(bytes1);
        }
        String threedisplay = parkingLotEquipment.getThreedisplay();
        if (threedisplay!=null||!threedisplay.equals("")){
            byte[] bytes2 = advertisement3(threedisplay);
            list.add(bytes2);
        }
        String fourdisplay = parkingLotEquipment.getFourdisplay();
        if (fourdisplay!=null||!fourdisplay.equals("")){
            byte[] bytes3 = advertisement4(fourdisplay);
            list.add(bytes3);
        }
        byte[] bytes4 = advertisement5(String.valueOf(parkingLotEquipment.getVolume()));
        list.add(bytes4);
        return list;
    }

    //广告位第4行
    private static byte[] advertisement4(String data) {
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
        log.info("ss="+s4);
        byte[] bytes2 = Hex.toByteArray(s4);
        return bytes2;
    }
    //广告位第3行
    private static byte[] advertisement3(String data) {
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
        log.info("ss="+s4);
        byte[] bytes2 = Hex.toByteArray(s4);
        return bytes2;
    }
    //广告位第2行
    private static byte[] advertisement2(String data) {
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
        String s =  "01640011"+ s3+"02"+test+"0000";
        log.info(s);
        String s1 = "01640011"+ s3+"02"+test;
        byte[] bytes1 = Hex.toByteArray(s);
        int i = CRCUtil.calcCrc16(bytes1);
        String crc = String.format("%04x", i);
        System.out.println("校验位"+crc);
        String s4 = "AA55" + s1 + crc + "AF";
        log.info("ss2="+s4);
        log.info(s4);
        byte[] bytes2 = Hex.toByteArray(s4);

        return bytes2;
    }
    //广告位第1行
    private static byte[] advertisement1(String data) {
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
        String s = "00640011"+ s3+"01"+test+"0000";
        log.info(s);
        String s1 = "00640011"+ s3+"01"+test;
        byte[] bytes1 = Hex.toByteArray(s);
        int i = CRCUtil.calcCrc16(bytes1);
        String crc = String.format("%04x", i);
        System.out.println("校验位"+crc);
        String s4 = "AA55" + s1 + crc + "AF";
        log.info("ss="+s4);
        byte[] bytes2 = Hex.toByteArray(s4);
        return bytes2;
    }
    //设置音量
    private static byte[] advertisement5(String data) {
        String s = "006400F000010"+data+"0000";
        log.info(s);
        String s1 = "006400F000010"+data;
        byte[] bytes1 = Hex.toByteArray(s);
        int i = CRCUtil.calcCrc16(bytes1);
        String crc = String.format("%04x", i);
        System.out.println("校验位"+crc);
        String s4 = "AA55" + s1 + crc + "AF";
        log.info("ss="+s4);
        byte[] bytes2 = Hex.toByteArray(s4);
        return bytes2;
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
        log.info("ss="+s4);
        byte[] bytes2 = Hex.toByteArray(s4);
        int length1 = bytes2.length;
        byte base64_data[] = Base64.getEncoder().encode(bytes2);
        String base64_str = new String(base64_data);
        return base64_str+','+length1;
    }
    //心跳
    public static String returnHeartbeat() {
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

}
