package com.tzj;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName ProductProcess
 * @Description TODO
 * @Author QiBin
 * @Date 2022/2/2217:38
 * @Version 1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductProcess {

    String companyName;
    String companyId;
    String nature;
    String industryId;
    String linkedName;
    String linkedNum;
    String registryTime;
    String capital;
    String reAdress;
    String comAdress;
    String postCode;
    String userName;
    String email;
    String userPhone;
    String classify;
    String comFlag;


    ProductProcessData productProcessData;

    String  consumption = "/";

    String purity = "/";

    String emissionFactor = "/";

    String year;

    String downloadDateTime;

    String reportCompany;

    List<MainPro> mainPro;

    List<SixProduct> sixProduct;

    ActivityLevel detailTable;

    ActivityLevel fourFactor;

    List<EightRecFac> eightRecFac;

    /**
     * 化石燃料燃烧CO2排放
     * 工业生产过程CO2排放
     * 其中：原材料CO2排放
     *           碳酸盐使用CO2排放
     * 工业生产过程N2O排放
     * 其中：硝酸生产N2O排放
     *         己二酸生产N2O排放
     * CO2回收利用量
     * 企业净购入的电力消费引起的CO2排放
     * 企业净购入的热力消费引起的CO2排放
     * 企业温室气体排放总量（吨CO2当量）
     */
    String burnMass;
    String burnCmass;

    String proCmass;
    String proCcmass;
    //原材料CO2排放
    String maMass;
    String maCmass;
    //碳酸盐使用CO2排放
    String cbnMass;
    String cbnCmass;

    String proNmass;
    String proNcmass;

    //硝酸生产N2O排放
    String glonMass;
    String glonNmass;

    //己二酸生产N2O排放
    String hdMass;
    String hdNmass;

    String  recMass;
    String recCmass;

    String eleMass;
    String eleCmass;

    String heatMass;
    String heatCmass;

    String totalDischarge;

    /**
     * 石灰石
     * 消耗量（吨）	纯度（%）	CO2排放因子
     * （吨CO2/吨碳酸盐）
     */
    String limestoneConsumer;
    String limestonePurity;
    String limestoneFactor;


    /**
     * 高压法
     * 中压法
     * 常压法
     * 双加压法
     * 综合法
     * 低压法
     * 硝酸氧化
     * 其它
     */
    /**
     * 硝酸产量
     * 	N2O生成因子
     * 	N2O去除率（%）
     * 	尾气处理设备使用率（%）
     */


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

    String eleInfoTwo;

    String eleInfoThree;
    /**
     * 热力净购入量
     */
    String heating;
    /**
     * 来源说明
     */
    String heatInfo;

    String heatInfoTwo;

    String heatInfoThree;
    /**
     * co2回收利用量
     */
    String steam;

    /**
     * 来源说明
     */
    String stInfo;

    String stInfoTwo;

    String stInfoThree;


}
