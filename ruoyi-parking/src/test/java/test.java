import com.ruoyi.common.sdk.LPRDemo;
import com.ruoyi.common.sdk.LPRSDK;
import com.ruoyi.common.utils.ip.IpUtils;
import com.sun.jna.Pointer;

import java.util.List;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * @Author: yangyang
 * @Date: 2022/11/22/9:58
 * @Description:
 */
public class test {
    public static void main(String[] args) {
        LPRDemo lprDemo = new LPRDemo();

        int handle = lprDemo.InitClient("192.168.55.52");
        //开杆
        int i1 = lprDemo.switchOn(handle, 0, 500);
        lprDemo.VzLPRClient_Close(handle);
        lprDemo.VzLPRClient_Cleanup();

        //删除白名单
        // DeleteWlistByPlate(handle, "川A12345");
        //查询白名单
        //boolean queryWlistByPlate = lprDemo.QueryWlistByPlate(handle, "川B12346");
       // boolean b = lprDemo.QueryWlistByPage(handle, 0);
      //  System.out.println( queryWlistByPlate);

        //开杆

        //


    }

}
