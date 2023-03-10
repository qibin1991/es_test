package com.tzj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName SixProduct
 * @Description TODO
 * @Author QiBin
 * @Date 2022/2/2315:36
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SixProduct {

    String num;
    String name;
    String model;
    String location;
    String promodel;
    String accuracy;
    String series;
    String frequency;
    String situation;



}
