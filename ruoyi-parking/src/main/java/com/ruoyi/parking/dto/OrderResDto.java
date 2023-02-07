package com.ruoyi.parking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: yangyang
 * @Date: 2023/02/04/15:37
 * @Description:
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderResDto {
    private Boolean res;
    private String msg;
}
