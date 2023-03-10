package com.report;


import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.*;
import com.deepoove.poi.data.style.Style;
import com.google.common.collect.ImmutableMap;
import com.report.entity.DataSeriesDto;
import com.report.entity.DocxImgDto;
import com.report.entity.DocxModelData;
import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;
import java.util.List;

/**
 * @ClassName ReportGenerateUtil
 * @Description TODO
 * @Author QiBin
 * @Date 2023/1/31 14:44
 * @Version 1.0
 **/
@Component
public class ReportGenerateUtil {

    private static final Logger logger = LoggerFactory.getLogger(ReportGenerateUtil.class);


    /**
     * 初始化表格数据
     *
     * @param titleArray 表头行，不能为空
     * @param dataArrar  行数据列表，可以为空
     * @return
     */
    public static TableRenderData initTable(String[] titleArray, List<String[]> dataArrar) {
        if (titleArray == null || titleArray.length <= 0) {
            throw new RuntimeException("表头不能为空");
        }
        RowRenderData row0 = Rows.of(titleArray).textColor("FFFFFF").bgColor("4472C4").center().create();
        RowRenderData[] raws = new RowRenderData[]{row0};
        int columnLength = titleArray.length;
        if (CollectionUtils.isNotEmpty(dataArrar)) {
            raws = new RowRenderData[dataArrar.size() + 1];
            raws[0] = row0;
            for (int i = 0; i < dataArrar.size(); i++) {
                if (dataArrar.get(i).length != columnLength) {
                    throw new RuntimeException("数据列数与表头列数不一致");
                }
                RowRenderData row1 = Rows.create(dataArrar.get(i));
                raws[i + 1] = row1;
            }
        }
        return Tables.create(raws);
    }

    /**
     * 初始化图表数据
     *
     * @param chartTitle，不能为空
     * @param dataCategory    数据类别，列头，这里有几个，图表中的就有几组数据，不能为空
     * @param dataSeriesList  数据系列列表，相当于图例，数组中每增加一个实体，在图表的各组数据中就会增加一个柱，不能为空
     * @return
     */
    public static ChartMultiSeriesRenderData initChart(String chartTitle, String[] dataCategory, List<DataSeriesDto> dataSeriesList) {
        if (StringUtils.isBlank(chartTitle) || dataCategory == null || dataCategory.length <= 0 || CollectionUtils.isEmpty(dataSeriesList)) {
            throw new RuntimeException("图表标题、数据系列、数据类别不能为空");
        }
        Charts.ChartMultis chartMultis = Charts.ofMultiSeries(chartTitle, dataCategory);
        int categorySize = dataCategory.length;
        for (int i = 0; i < dataSeriesList.size(); i++) {
            DataSeriesDto dataSeries = dataSeriesList.get(i);
            if (dataSeries == null || StringUtils.isBlank(dataSeries.getSeriesName())
                    || dataSeries.getSeriesData() == null || dataSeries.getSeriesData().length != categorySize) {
                throw new RuntimeException("数据系列、系列名称不能为空，且系列的数据长度与类别的长度要相同");
            }
            chartMultis.addSeries(dataSeries.getSeriesName(), dataSeries.getSeriesData());
        }
        return chartMultis.create();
    }

    /**
     * 生成word
     *
     * @param tempAbsPath 模板文件绝对路径
     * @param model       待渲染数据
     * @param destAbsPath 生成目标文件绝对路径
     * @return
     */
    public static Boolean generateWord(String tempAbsPath, DocxModelData model, String destAbsPath) {
        if (StringUtils.isBlank(tempAbsPath) || StringUtils.isBlank(destAbsPath) || model == null) {
            throw new RuntimeException("模板文件绝对路径、待渲染数据、生成目标文件绝对路径不能为空");
        }
        File fileTemp = new File(tempAbsPath);
        HashMap<String, Object> modelMap = new HashMap<>();
        //处理文本替换
        Map<String, TextRenderData> textMod = model.getTextMod();
        if (textMod != null && CollectionUtils.isNotEmpty(textMod.keySet())) {
            for (String key : textMod.keySet()) {
                modelMap.put(key, textMod.get(key));
            }
        }

        //超链接处理
        Map<String, String> linkMod = model.getLinkMod();
        if (linkMod != null && CollectionUtils.isNotEmpty(linkMod.keySet())) {
            for (String key : linkMod.keySet()) {
                modelMap.put(key, Texts.of("website").link(linkMod.get(key)).create());
            }
        }

        //图片处理
        Map<String, DocxImgDto> imgMod = model.getImgMod();
        if (imgMod != null && CollectionUtils.isNotEmpty(imgMod.keySet())) {
            for (String key : imgMod.keySet()) {
                DocxImgDto img = imgMod.get(key);
                modelMap.put(key, Pictures.ofUrl(img.getUrl()).size(img.getImgWidth(), img.getImgHeight()).create());
            }
        }

        //表格处理
        Map<String, TableRenderData> tableMod = model.getTableMod();
        if (tableMod != null && CollectionUtils.isNotEmpty(tableMod.keySet())) {
            for (String key : tableMod.keySet()) {
                modelMap.put(key, tableMod.get(key));
            }
        }

        //图表处理
        Map<String, ChartMultiSeriesRenderData> chartMod = model.getChartMod();
        if (chartMod != null && CollectionUtils.isNotEmpty(chartMod.keySet())) {
            for (String key : chartMod.keySet()) {
                modelMap.put(key, chartMod.get(key));
            }
        }

        //区块对控制
        Map<String, Boolean> blockMod = model.getBlockMod();
        if (blockMod != null && CollectionUtils.isNotEmpty(blockMod.keySet())) {
            for (String key : blockMod.keySet()) {
                modelMap.put(key, blockMod.get(key));
            }
        }

        XWPFTemplate template = null;
        try {
            //加载模板渲染数据
            template = XWPFTemplate.compile(fileTemp).render(modelMap);
            //输出结果
            template.write(new FileOutputStream(destAbsPath));
            return true;
        } catch (Exception e) {
            logger.error("加载模板渲染数据/输出结果，异常", e);
        } finally {
            if (template != null) {
                try {
                    template.close();
                } catch (IOException e) {
                    logger.error("模板关闭异常", e);
                }
            }
        }
        return false;
    }


    @SneakyThrows
    public static void main(String[] args) {
        String tempAbsPath = "/Users/qibin/Downloads/京东方/ISO产品碳足迹评价报告模板.docx";
        String destAbsPath = "/Users/qibin/Downloads/boe";
        //文本
//        ImmutableMap<String, TextRenderData> of = ImmutableMap.of("author", Texts.of("Sayi").color("000000").create());
        Style build = Style.builder().buildColor("FF0000").buildUnderlineColor("FF0000").buildUnderlinePatterns(UnderlinePatterns.SINGLE).build();
        ImmutableMap<String, TextRenderData> textMod = ImmutableMap.of("productionName", new TextRenderData("Sayi",build),
                "productionCode",new TextRenderData("Sayi",build));


//        Map<String, String> textMod = ImmutableMap.of("title", "_此处文字是代码生成的（标题）_", "content1", "_此处文字是代码生成的(内容)_");
        //超链接
        Map<String, String> linkMod = ImmutableMap.of("link", "https://bz.zzzmh.cn/index");
        //图片
        Map<String, DocxImgDto> imgMod = ImmutableMap.of("urlImg", new DocxImgDto("http://keyun-file.oss-cn-beijing.aliyuncs.com/20211005/%E6%97%A0%E6%A0%87%E9%A2%98.png", 300, 300));
        //表格
        TableRenderData table = initTable(new String[]{"姓名", "学历"}, Arrays.asList(new String[]{"张三", "硕士"}, new String[]{"李四", "博士"}));
        Map<String, TableRenderData> tableMod = ImmutableMap.of("table", table);
        //图表
        List<DataSeriesDto> dataSeriesList = new ArrayList<>();
        dataSeriesList.add(new DataSeriesDto("系列1", new Double[]{223.0, 119.0, 100.0}));
        dataSeriesList.add(new DataSeriesDto("系列2", new Double[]{445.0, 336.0, 20.0}));
        ChartMultiSeriesRenderData chart1 = initChart("这是图表标题", new String[]{"类别1", "类别2", "类别3"}, dataSeriesList);
        Map<String, ChartMultiSeriesRenderData> chartMod = ImmutableMap.of("barChart", chart1);

        //区块对
        Map<String, Boolean> blockMod = ImmutableMap.of("person", true);
        DocxModelData model = new DocxModelData(textMod, linkMod, imgMod, tableMod, chartMod, blockMod);

        Boolean result = generateWord(tempAbsPath, model, destAbsPath);
        System.out.println("生成结果：" + result);

        XWPFDocument doc = new XWPFDocument(new FileInputStream("src/test/resources/out/out_markdown2.docx"));
        doc.enforceUpdateFields(); // 更新目录

        doc.write(new FileOutputStream("src/test/resources/out/out_markdown3.docx"));
        doc.close();


    }

    //刷新目录
    private static void restWord(String docFilePath) {

        Document doc1 = new Document(docFilePath);

        doc1.updateTableOfContents();

        doc1.saveToFile(docFilePath, FileFormat.Docx_2010);

        try (FileInputStream in = new FileInputStream(docFilePath)) {
            XWPFDocument doc = new XWPFDocument(OPCPackage.open(in));
            List<XWPFParagraph> paragraphs = doc.getParagraphs();
            if (paragraphs.size() < 1) return;
            XWPFParagraph firstParagraph = paragraphs.get(0);
            if (firstParagraph.getText().contains("Spire.Doc")) {
                doc.removeBodyElement(doc.getPosOfParagraph(firstParagraph));
            }
            OutputStream out = new FileOutputStream(docFilePath);
            doc.write(out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
