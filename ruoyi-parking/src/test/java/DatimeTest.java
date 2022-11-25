import com.fasterxml.jackson.annotation.JsonFormat;

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

            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            try{
                Date date=sdf.parse(2018+"-"+2+"-"+2+" "+2+":"+2+":"+22);
                Date date1 = new Date();
                System.out.println(date1);
                System.out.println(date1);
            }catch (Exception e){

            }


        }






}
