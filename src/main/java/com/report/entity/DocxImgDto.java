package com.report.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName DocxImgDto
 * @Description TODO
 * @Author QiBin
 * @Date 2023/1/31 14:46
 * @Version 1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocxImgDto {

    /**
     * 图片地址
     */
    private String url;

    /**
     * 图片宽度
     */
    private int imgWidth;

    /**
     * 图片高度
     */
    private int imgHeight;

}
