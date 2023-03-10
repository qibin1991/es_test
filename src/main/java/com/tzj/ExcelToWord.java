package com.tzj;


import cn.hutool.core.collection.CollUtil;
import lombok.Data;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;


/**
 * @ClassName ExcelToWord
 * @Description TODO
 * @Author QiBin
 * @Date 2022/2/1810:20
 * @Version 1.0
 **/
public class ExcelToWord {
    Logger logger = LoggerFactory.getLogger(ExcelToWord.class);

    public static void main(String[] args) throws IOException {

//
//        XWPFDocument document = new XWPFDocument();
//
//        PoiUtils.createTable(document, new PoiUtils.TableValue());
//
//        PoiUtils.writeFile(new File("/Users/qibin/Downloads/1234.docx"), document);
        cp();

    }


    /**
     * 替换 模板
     */
    public static void cp() throws IOException {
        FileInputStream in = new FileInputStream("/Users/qibin/Downloads/单位名称.docx");//载入文档


        XWPFDocument document = new XWPFDocument(in);
        //表格
        List<XWPFTable> tables = document.getTables();

        Map<String, String> map = new HashMap<>();
        Set<String> set = map.keySet();

        for (XWPFTable table : tables) {
            List<XWPFTableRow> rows = table.getRows();//获取行
            for (XWPFTableRow row : rows) {
                List<XWPFTableCell> cells = row.getTableCells();//获取单元格
                for (XWPFTableCell cell : cells) {
                    String text = cell.getText();

                    System.out.print(text +":");

//                    for (String s : set) {
//                        //判断行是否包含map中的key，若包含，则用value替换行中key的内容
//                        if (cell.getText().contains(s)) {
//                            //用来存储替换后的行数据
//                            String replace = "";
//                            replace = cell.getText().replace(s, map.get(s));
//                            //删除原来的行
//                            cell.removeParagraph(0);
//                            //新增一行
//                            XWPFParagraph para = cell.addParagraph();
//                            para.setSpacingBetween(1);
//                            //填充数据
//                            cell.setText(replace);
//                        }
//                    }
                }
                System.out.println("\n");
            }
        }

        //段落
        List<XWPFParagraph> paragraphs = document.getParagraphs();




    }



    public static void test() {
        // 表头   标题 1 2 报告主题名称  统一社会信用代码
        XWPFDocument document = new XWPFDocument();

        //车间号  list
        List<chiled> list = new ArrayList<>();




        XWPFParagraph p1 = document.createParagraph();
        // 设置居中
//            p1.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun r1 = p1.createRun();

        int localMax = 0;

        for (chiled chiled : list) {
            int a = 0;
            int b = 0;
            int c = 0;

            List<String> details = chiled.getDetails();
            if (CollUtil.isNotEmpty(details)) {
                a = details.size();
            }
            List<String> values = chiled.getValues();
            if (CollUtil.isNotEmpty(values)) {
                b = values.size();
            }
            List<String> demands = chiled.getDemands();
            if (CollUtil.isNotEmpty(demands)) {
                c = demands.size();
            }
            //replenishs 合并的单元格
            int max = ((a > b ? a : b) > c) ? (a > b ? a : b) : c;
            localMax += max;

        }

        XWPFTable table = document.createTable(localMax, 5);
        // 设置居中
//            p1.setAlignment(ParagraphAlignment.CENTER);


    }

    @Data
    class chiled {

        //补充数据
        String replenishs;
        //详情
        List<String> details;
        //数值
        List<String> values;
        //要求
        List<String> demands;
    }

    private void getParagraph(XWPFTableCell cell, String cellText) {
        CTP ctp = CTP.Factory.newInstance();
        XWPFParagraph p = new XWPFParagraph(ctp, cell);
        p.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun run = p.createRun();
        run.setText(cellText);
        CTRPr rpr = run.getCTR().isSetRPr() ? run.getCTR().getRPr() : run.getCTR().addNewRPr();
        CTFonts fonts = rpr.isSetRFonts() ? rpr.getRFonts() : rpr.addNewRFonts();
        fonts.setAscii("仿宋");
        fonts.setEastAsia("仿宋");
        fonts.setHAnsi("仿宋");
        cell.setParagraph(p);
    }

    public void writeFileFromArray(ArrayList<String> text, String destPath) {
        XWPFDocument document = new XWPFDocument();
        OutputStream stream = null;
        BufferedOutputStream bufferStream = null;
        try {
            int pos = destPath.lastIndexOf(File.separator);
            if (pos > 0) {
                File dir = new File(destPath.substring(0, pos));
                if (!dir.exists()) {
                    dir.mkdirs();
                }
            }
            stream = new FileOutputStream(new File(destPath));
            bufferStream = new BufferedOutputStream(stream, 1024);
            // 创建一个段落
            XWPFParagraph p1 = document.createParagraph();
            // 设置居中
//            p1.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun r1 = p1.createRun();

            // 字体大小
            r1.setFontSize(18);// 字体大小
            // 增加换行
            r1.addCarriageReturn();
            // 创建段落
            XWPFParagraph p2 = document.createParagraph();
            XWPFRun r2 = p2.createRun();
            for (int i = 0; i < text.size(); i++) {
                r2.setText(text.get(i));
                r2.addCarriageReturn();
            }
            // 设置字体
            r2.setFontFamily("仿宋");
            r2.setFontSize(14);// 字体大小
            document.write(stream);
            stream.close();
            bufferStream.close();
        } catch (Exception ex) {
            logger.error("写word或Excel错误文件失败：{}", ex.getMessage());
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.error("写word文件失败stream：{}", e.getMessage());
                }
            }
            if (bufferStream != null) {
                try {
                    bufferStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.error("写word文件失败bufferStream：{}", e.getMessage());
                }
            }
        }
    }
}
