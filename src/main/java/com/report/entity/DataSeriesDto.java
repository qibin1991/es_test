package com.report.entity;

/**
 * @ClassName DataSeriesDto
 * @Description TODO
 * @Author QiBin
 * @Date 2023/1/31 14:46
 * @Version 1.0
 **/

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 对应excel中的一列数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataSeriesDto {

    /**
     * 表头
     */
    private String seriesName;

    /**
     * 列数据
     */
    private Double[] seriesData;
}
