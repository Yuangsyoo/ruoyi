package com.ruoyi.parking.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: yangyang
 * @Date: 2023/01/05/16:19
 * @Description:
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class SerialData {
        private String channel;
        private String serialno;
        private String  ipaddr;
        private String   deviceName;
        private String  serialChannel;
        private String   data;
        private String  dataLen;

}
