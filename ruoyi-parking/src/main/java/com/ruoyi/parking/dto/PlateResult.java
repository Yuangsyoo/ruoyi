package com.ruoyi.parking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 *
 * @Author: yangyang
 * @Date: 2022/11/22/16:08
 * @Description:车牌识别结果信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlateResult {
        //车牌真实宽度
        private String plate_true_width;
        //车牌距离
        private String plate_distance;
        //是否伪车牌，0：真实车牌，1：伪车牌
        private String is_fake_plate;
        //车头位置
        private CarLocation car_location;
        //车辆品牌
        private CarBrand car_brand;
        //车辆特征码
        private String feature_Code;
        //亮度评价（预留）
        private String bright;
        //车身亮度（预留）
        private int carBright;
        //车身颜色（预留）
        private int carColor;
        //车牌颜色 0：未知、1：蓝色、2：黄色、3：白色、4：黑
        //色、5：绿色
        private int colorType;
        //（预留）
        private int colorValue;
        //车的行进方向，0：未知，1：左，2：右，3：上， 4：下
        private int confidence;
        //：推送的结果中有"imagePath": "/snapshot/lpr/tri_snap_24.jpg"，后面是访问截图的 http 路
        //径，前面加上一体机的网址，就可以得到截图的地址如http://192.168.1.100:8080/snapshot/lpr/tri_snap_24.jpg
        //识别大图片的路径,开启推送大图片后，没有此字段
        private String imagePath;
        //车牌号字符串，如“京 AAAAAA”
        private String license;
        //车牌在图片中位置
        private Location location;
        //识别结果对应帧的时间戳
        private TimeStamp timeStamp;
        //识别所用时间
        private int timeUsed;
        //当前结果的触发类型：1：自动触发类型、2：外部输入触发（IO 输入）、4：软件触发（SDK）、8：虚拟线圈触发
        private int triggerType;
        //车牌类型 0：未知车牌:、1：蓝牌小汽车、2：:黑牌小汽车、3：单排黄牌、4：双排黄牌、 5：警车车牌、6：武警车牌、
        // 7：个性化车牌、8：单排军车牌、9：双排军车牌、10：使馆车牌、11：香港进出中国大陆车牌、12：农用车
        private int type;
        //识别结果车牌 ID
        private int plateid;
        //设备离线状态，0：在线，1：离线
        private int isoffline;
        private List<IC>gioouts=new ArrayList();




}
