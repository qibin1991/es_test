package com.tzj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName ActivityLevel
 * @Description TODO
 * @Author QiBin
 * @Date 2022/2/2116:37
 * @Version 1.0
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityLevel {


    DetailData detailTable;

    /**
     * 高压法
     */
    String hyperpiesis = "/";

    /**
     * 来源说明
     */
    String hyInfo;
    /**
     * 源说明
     */
    String hyInfoTwo;
    /**
     * 用率来源说明
     */
    String hyInfoThree;
    /**
     * 中压法
     */
    String mediumPressure;
    /**
     * 来源说明
     */
    String medInfo;

    /**
     * 源说明
     */
    String medInfoTwo;

    /**
     * 用率来源说明
     */
    String medInfoThree;

    /**
     * 常压法
     */
    String ordinaryPressure;
    /**
     * 来源说明
     */
    String orInfo;
    /**
     * 源说明
     */
    String orInfoTwo;

    /**
     * 用率来源说明
     */
    String orInfoThree;
    /**
     * 双压加法
     */
    String dualPressure;
    /**
     * 来源说明
     */
    String deInfo;
    /**
     * 源说明
     */
    String deInfoTwo;
    /**
     * 用率来源说明
     */
    String deInfoThree;
    /**
     * 综合法
     */
    String synthetic;
    /**
     * 来源说明
     */
    String suInfo;
    /**
     * 源说明
     */
    String suInfoTwo;
    /**
     * 用率来源说明
     */
    String suInfoThree;
    /**
     * 低压法
     */
    String lowTension;
    /**
     * 来源说明
     */
    String lowInfo;
    /**
     * 源说明
     */
    String lowInfoTwo;
    /**
     * 用率来源说明
     */
    String lowInfoThree;
    /**
     * 硝酸氧化
     */
    String nitrateOxidization;
    /**
     * N2O来源说明
     */
    String niInfo;
    /**
     * N2O去除率源说明
     */
    String niInfoTwo;
    /**
     * 尾气处理用率来源说明
     */
    String niInfoThree;
    /**
     * 其他
     */
    String rest;
    /**
     * N2O来源说明
     */
    String restInfo;
    /**
     * N2O去除率源说明
     */
    String restInfoTwo;
    /**
     * 尾气处理用率来源说明
     */
    String restInfoThree;
    /**
     * 电力净购入量
     */
    String electricity;
    /**
     * 来源说明
     */
    String eleInfo;
    /**
     * 热力净购入量
     */
    String heating;
    /**
     * 来源说明
     */
    String heatInfo;
    /**
     * co2回收利用量
     */
    String reclamation;

    /**
     * 来源说明
     */
    String recInfo;



    public ActivityLevel(DetailData detailTable, String hyperpiesis) {
        this.detailTable = detailTable;
        this.hyperpiesis = hyperpiesis;
    }

    public ActivityLevel(DetailData detailTable, String hyperpiesis, String mediumPressure, String ordinaryPressure, String dualPressure, String synthetic, String lowTension, String nitrateOxidization, String rest, String electricity, String heating, String reclamation) {
        this.detailTable = detailTable;
        this.hyperpiesis = hyperpiesis;
        this.mediumPressure = mediumPressure;
        this.ordinaryPressure = ordinaryPressure;
        this.dualPressure = dualPressure;
        this.synthetic = synthetic;
        this.lowTension = lowTension;
        this.nitrateOxidization = nitrateOxidization;
        this.rest = rest;
        this.electricity = electricity;
        this.heating = heating;
        this.reclamation = reclamation;
    }
}
