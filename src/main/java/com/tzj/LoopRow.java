package com.tzj;

import lombok.Builder;
import lombok.Data;

/**
 * @ClassName LoopRow
 * @Description TODO
 * @Author QiBin
 * @Date 2022/2/2217:02
 * @Version 1.0
 **/
@Data
@Builder
public class LoopRow {

    /**
     * 燃料品种
     */
    String fuelBand;
    /**
     * 净消耗量
     */
    String consumption;
    /**
     * 含碳量
     */
    String carbonContent;
    /**
     * 低位发热量
     */
    String lowerHeating;
    /**
     * 单位热值含碳量
     */
    String perUnit;
    /**
     * 碳氧化率
     */
    String oxidationRate;

    public LoopRow() {
    }

    public LoopRow(String fuelBand, String consumption, String carbonContent, String lowerHeating, String perUnit, String oxidationRate) {
        this.fuelBand = fuelBand;
        this.consumption = consumption;
        this.carbonContent = carbonContent;
        this.lowerHeating = lowerHeating;
        this.perUnit = perUnit;
        this.oxidationRate = oxidationRate;
    }
}
