package com.ruoyi.parking.dto;

import com.ruoyi.common.core.page.TableDataInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: yangyang
 * @Date: 2023/02/04/21:19
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkingRecordDDo {
   private TableDataInfo dataTable;
   private Long count;
}
