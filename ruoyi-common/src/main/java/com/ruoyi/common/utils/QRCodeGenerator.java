package com.ruoyi.common.utils;
 
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.ruoyi.common.utils.sign.Base64;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
 
/**
 * 生产二维码 工具类
 *
 * @author tangwang
 */
public class QRCodeGenerator {
    // 生成二维码高度
    public static final int width = 350;
 
    // 生成二维码宽度
    public static final int height = 350;
 
    /**
     * 生成二维码
     *
     * @param text 二维码内容
     * @return 返回二维码的base64编码
     */
    public static String generateQRCodeImage(String text) {
        try {
            return generateQRCodeImage(text, width, height);
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
 
    /**
     * 生成二维码
     *
     * @param text   二维码内容
     * @param width  二维码高度
     * @param height 二维码宽度
     * @return 返回二维码的base64编码
     * @throws WriterException
     * @throws IOException
     */
    public static String generateQRCodeImage(String text, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
        String imgByte = Base64.encode(outputStream.toByteArray());
        return "data:image/png;base64," + imgByte;
    }
}