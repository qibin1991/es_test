package com.report.entity;

import com.deepoove.poi.data.ChartMultiSeriesRenderData;
import com.deepoove.poi.data.TableRenderData;
import com.deepoove.poi.data.TextRenderData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @ClassName DocxModelData
 * @Description TODO
 * @Author QiBin
 * @Date 2023/1/31 14:45
 * @Version 1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocxModelData {

    /**
     * 纯文本简单替换，例如标题、作者、内容等
     */
    private Map<String, TextRenderData> textMod;

    /**
     * 超链接替换 value必须是一个url
     */
    private Map<String, String> linkMod;

    /**
     * 图片替换
     */
    private Map<String, DocxImgDto> imgMod;

    /**
     * 表格替换
     */
    private Map<String, TableRenderData> tableMod;

    /**
     * 图表替换
     */
    private Map<String, ChartMultiSeriesRenderData> chartMod;

    /**
     * 区块控制
     */
    private Map<String, Boolean> blockMod;
}
