package com.ruoyi.parking.utils;

import com.ruoyi.common.utils.CRC16Util;
import com.ruoyi.common.utils.Hex;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Test;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
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
    //下发临显指令
    public static String addSerialPort(String data) {
        /*
        临显
        AA55   2A 64 00 20 00 0A 02 00 D2 BB      B3 B5  D2 BB  B8 CB F71D AF
        *      2A 64 00 20 00 0a 02 00 d2 bb      b3 b5  d2 bb  b8 cb
        * AA55 3B 64 00 20 00 0A 01 00 BB B6      D3 AD B9 E2 C1 D9 6D F4 AF
        *      3B 64 00 20 00 0a 01 00 bb b6      d3 ad b9 e2 c1 d9
        * AA55 3E 64 00 20 00 0A 04 00 BC F5 CB D9 C2 FD D0 D0 5F 02 AF
          AA55 3E 64 00 20 00 0A 04 00 BC F5 CB D9 C2 FD D0 D0 5F 02 AF
        *      3E 64 00 20 00 0a 04 00 bc f5      cb d9 c2 fd d0 d0
        * 广告
        * AA55 08 64 00 11 00 09 04         BC F5       CB D9 C2 FD D0 D0     39 78 AF
        *
        * */
        String s = getString(data);
        String[] split = s.split(",");
        String a="{\n" +
                "\"Response_AlarmInfoPlate\": {\n" +
                "\"info\":\"ok\",\n" +
                "\"channelNum\" : 0, \n" +
                "\"manualTrigger\" : \"no\",\n" +
                "\"is_pay\":\"false\",\n" +
                "\"serialData\" :[\n" +
                "{\n" +
                "\"serialChannel\":0,\n" +
                "\"data\" : \""+split[0]+"\",\n" +
                "\"dataLen\" : "+split[1]+"\n" +
                "},\n" +
                "{ \n" +
                "\"serialChannel\":1,\n" +
                "\"data\" : \"....\",\n" +
                "\"dataLen\" : 123\n" +
                "}\n" +
                "]\n" +
                "}\n" +
                "}";
        return a;
    }
    //临显第4行
    private static String getString(String data) {
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
          /*
        临显
        AA55   2A 64 00 20 00 0A 02 00 D2 BB      B3 B5  D2 BB  B8 CB F71D AF
        *      2A 64 00 20 00 0a 02 00 d2 bb      b3 b5  d2 bb  b8 cb
        * AA55 3B 64 00 20 00 0A 01 00 BB B6      D3 AD B9 E2 C1 D9 6D F4 AF
        *      3B 64 00 20 00 0a 01 00 bb b6      d3 ad b9 e2 c1 d9
        * AA55 3E 64 00 20 00 0A 04 00 BC F5 CB D9 C2 FD D0 D0 5F 02 AF
          AA55 3E 64 00 20 00 0A 04 00 BC F5 CB D9 C2 FD D0 D0 5F 02 AF
        *      3E 64 00 20 00 0a 04 00 bc f5      cb d9 c2 fd d0 d0
        * 广告
        * AA55 08 64 00 11 00 09 04         BC F5       CB D9 C2 FD D0 D0     39 78 AF
        *
        * */
        log.info("长度="+s3);
        String s = "3E640020"+ s3+"0400"+test+"0000";
        log.info(s);
        String s1 = "3E640020"+ s3+"0400"+test;
        byte[] bytes1 = Hex.toByteArray(s);
        int i = CRCUtil.calcCrc16(bytes1);
        String crc = String.format("%04x", i);
        System.out.println("校验位"+crc);
        String s4 = "AA55" + s1 + crc + "AF";
        log.info(s4);
        byte[] bytes2 = Hex.toByteArray(s4);
        int length1 = bytes2.length;
        byte base64_data[] = Base64.getEncoder().encode(bytes2);
        String base64_str = new String(base64_data);
        return base64_str+','+length1;
    }

    public static void main(String[] args)  {
        addSerialPort("减速慢行");
    // addSerialPort("一车一杆");
     ///addSerialPort("欢迎光临");
       //addSerialPort("ss");
    }

    //需要使用2字节表示b
    public static String numToHex16(int b) {
        return String.format("%04x", b);
    }

 /*   AA 55 2A 64 00 20 00 0E 02 00 D2 BB CD A3 B3 B5 B3 A1 B2 BB BF C9 D3 C3 6D 2F AF
            AA 55 2A 64 00 20 00 0e 02 00 D2 BB cd a3 b3 b5 b3 a1 b2 bb bf c9 d3 c3 6d 2f AF*/
}
