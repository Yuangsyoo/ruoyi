package com.ruoyi.parking.utils;

import com.ruoyi.common.utils.CRC16Util;
import com.ruoyi.common.utils.Hex;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Test;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: yangyang
 * @Date: 2023/01/04/9:30
 * @Description:
 */
@Slf4j
public class SerialPortUtils {
    public static String addSerialPort(String data){
        // TODO: 2023/1/4 需要测试
        ChangeCharset test = new ChangeCharset();
        String s1 = null;
        try {
            s1 = test.toASCII(data);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //16进制字符串
        String s2 = Hex.str2HexStr(data);
        //转换为byte数组
        byte[] bytes = Hex.toByteArray(s2);

        //长度
        int length = bytes.length;
        System.out.println(length);
        //长度转换16进制
        String s3 = numToHex16(length);
        String s = "00640025"+ s3+"010100"+s2;
        byte[] bytes1 = Hex.toByteArray(s);
        String crc = CRC16Util.getCRC(bytes1);
        System.out.println(crc);
        String s4 = "AA55" + s + crc + "AF";
        System.out.println(s4);
        byte[] bytes2 = Hex.toByteArray(s4);
        byte base64_data[] = Base64.getEncoder().encode(bytes2);
        String base64_str = new String(base64_data);
        System.out.println(base64_str);
        return base64_str;
    }

    public static void main(String[] args) {

        addSerialPort("王义鑫欢饮光临");
        /*AA55 21 6400 11 0009 01 BBB6D3ADC8FDC1D9 BDD3 AF
          AA55 22640011000902B3B5C1BDCAB6B1F0 E346 AF
          AA55 00 6400 27 0012 01 EFBFBDEFBFBDEFBFBDEFBFBDEFBFBDEFBFBD 1A26AF
          AA55 00 6400 27 0006 01 01050200E59388E596BD 99AA AF
          */
      //  addSerialPort("ss");
    }

    //需要使用2字节表示b
    public static String numToHex16(int b) {
        return String.format("%04x", b);
    }


}
