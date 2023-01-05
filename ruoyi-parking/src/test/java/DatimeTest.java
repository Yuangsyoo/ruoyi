import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.utils.Hex;

import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: yangyang
 * @Date: 2022/11/24/13:48
 * @Description:
 */
class DataaimeTest {
    public static void main(String[] args) {

        String s1 = Hex.byte2HexStr("王一新n".getBytes(StandardCharsets.UTF_8));
        byte[] bytes = Hex.hexStringToBytes(s1);
        for (byte aByte : bytes) {
            System.out.println(aByte);
        }

    }






}
