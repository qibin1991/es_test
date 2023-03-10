package com.tzj;


import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.policy.DocumentRenderPolicy;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTInline;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * poi 相关工具类
 *
 * @author: yxc
 * @time: 2022/1/10 11:04
 **/
public class PoiUtils {

    /**
     * 图数据对象
     * 仅代表每一个数据轴
     */
    @Data
    public static class ChartValue {
        /**
         * 标题
         */
        private String title;
        /**
         * 值列表，与下面intList二选一
         */
        private List<String> strList = new ArrayList<>();
        /**
         * 值列表，与上面strList二选一
         */
        private List<Integer> intList = new ArrayList<>();

        public ChartValue(String title) {
            this.title = title;
        }

    }


    /**
     * 创建标题
     *
     * @param document: word的document对象
     * @param title:    标题
     * @param subTitle: 副标题
     * @return void
     * @author yxc
     * @date 2022/1/18 9:26
     **/
    public static void createTitle(XWPFDocument document, String title, String subTitle) {
        if (StringUtils.isEmpty(title)) {
//            throw new BussinessException("标题不能为空");
        }
        // 创建段落
        XWPFParagraph titleParagraph = document.createParagraph();
        XWPFRun titleRun = titleParagraph.createRun();
        titleRun.setText(title);
        // 字号
        titleRun.setFontSize(20);
        // 加粗
        titleRun.setBold(true);
        // 居中
        titleParagraph.setAlignment(ParagraphAlignment.CENTER);
        if (StringUtils.isNotEmpty(subTitle)) {
            // 处理父标题
            XWPFParagraph subTitleParagraph = document.createParagraph();
            XWPFRun subTitleRun = subTitleParagraph.createRun();
            subTitleRun.setText(subTitle);
            // 字号
            subTitleRun.setFontSize(14);
            // 加粗
            subTitleRun.setBold(true);
            // 居中
            subTitleParagraph.setAlignment(ParagraphAlignment.CENTER);
        }
    }

    /**
     * 创建段落
     *
     * @param document:     word的document对象
     * @param title:        段落标题，可以为空
     * @param paragraphStr: 段落内容
     * @return void
     * @author yxc
     * @date 2022/1/18 10:14
     **/
    public static void createParagraph(XWPFDocument document, String title, String paragraphStr) {
        if (StringUtils.isEmpty(paragraphStr)) {
//            throw new BussinessException("段落不能为空");
        }
        if (StringUtils.isNotEmpty(title)) {
            // 段落标题
            // 创建段落
            XWPFParagraph titleParagraph = document.createParagraph();
            XWPFRun titleRun = titleParagraph.createRun();
            titleRun.setText(title);
            // 字号
            titleRun.setFontSize(16);
            // 加粗
            titleRun.setBold(true);
            // 居中
            titleParagraph.setAlignment(ParagraphAlignment.LEFT);
        }
        // 创建段落
        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun paragraphRun = paragraph.createRun();
        // 设置文字
        paragraphRun.setText(paragraphStr);
        // 字号
        paragraphRun.setFontSize(14);
        // 两端对齐
        paragraph.setAlignment(ParagraphAlignment.BOTH);
        // 首行缩进
        paragraph.setFirstLineIndent(600);
//        paragraph.setIndentFromLeft(14 * 2);
    }

    /**
     * 表格属性对象
     */
    @Data
    public static class TableValue {
        /**
         * 列数
         */
        private int colNum;
        /**
         * 行数
         */
        private int rowNum;
        /**
         * 标题
         */
        private List<String> titleList = new ArrayList<>();
        /**
         * 内容
         */
        private List<List<String>> valueList = new ArrayList<>();
        /**
         * 表格标题
         */
        private String tableTitle;

        public TableValue() {
        }

        public TableValue(int colNum, int rowNum) {
            this.colNum = colNum;
            this.rowNum = rowNum;
        }

        public TableValue(int colNum, int rowNum, String tableTitle) {
            this.colNum = colNum;
            this.rowNum = rowNum;
            this.tableTitle = tableTitle;
        }
    }

    /**
     * 创建表格
     *
     * @param document:   word对应的document对象
     * @param tableValue: 表格属性对象
     * @return void
     * @author 杨旭晨
     * @date 2022/1/18 17:38
     **/
    public static void createTable(XWPFDocument document, TableValue tableValue) {
        if (StringUtils.isNotEmpty(tableValue.getTableTitle())) {
            // 创建段落
            XWPFParagraph titleParagraph = document.createParagraph();
            XWPFRun titleRun = titleParagraph.createRun();
            titleRun.setText(tableValue.tableTitle);
            // 字号
            titleRun.setFontSize(16);
            // 加粗
            titleRun.setBold(true);
            // 居中
            titleParagraph.setAlignment(ParagraphAlignment.CENTER);
        }
        XWPFTable table = document.createTable(tableValue.rowNum, tableValue.colNum);
        // 表格宽度
        table.setWidth(100);
        // 宽度类型 百分比
//        table.setWidthType(TableWidthType.PCT);
        // 水平居中
//        table.setTableAlignment(TableRowAlign.CENTER);
        if (tableValue.getTitleList() != null && !tableValue.getTitleList().isEmpty()) {
            // 标题行不为空，添加标题行
            XWPFTableRow titleRow = table.getRow(0);
            // 设置高度
            titleRow.setHeight(300);
            // 设置标题行
            List<String> titleList = tableValue.getTitleList();
            for (int i = 0; i < titleList.size(); i++) {
                XWPFTableCell cell = titleRow.getCell(i);
                // 以段落的形式添加文字
                XWPFParagraph cellParagraph = cell.getParagraphArray(0);
                XWPFRun cellRun = cellParagraph.createRun();
                // 文字
                cellRun.setText(titleList.get(i));
                // 居中
                cellParagraph.setAlignment(ParagraphAlignment.CENTER);
                // 字号
                cellRun.setFontSize(12);
                // 加粗
                cellRun.setBold(true);
            }
        }
        // 内容行
        List<List<String>> valueList = tableValue.getValueList();
        for (int i = 0; i < valueList.size(); i++) {
            // 排除第一行，标题行
            XWPFTableRow valueRow = table.getRow(i + 1);
            // 设置高度
            valueRow.setHeight(300);
            List<String> values = valueList.get(i);
            for (int j = 0; j < values.size(); j++) {
                XWPFTableCell cell = valueRow.getCell(j);
                // 用段落形式添加文字
                XWPFParagraph cellParagraph = cell.getParagraphArray(0);
                XWPFRun cellRun = cellParagraph.createRun();
                // 文字
                cellRun.setText(values.get(j));
                // 居中
                cellParagraph.setAlignment(ParagraphAlignment.CENTER);
                // 字号
                cellRun.setFontSize(12);
                // 不加粗
                cellRun.setBold(false);
            }
        }
        setTableHeight(table, 600, STVerticalJc.CENTER);
    }

    /**
     * 设置表格高度
     *
     * @param infoTable: 表格
     * @param heigth:    设置的高度
     * @param vertical:  xxx
     * @return void
     * @author 杨旭晨
     * @date 2022/1/18 17:38
     **/
    private static void setTableHeight(XWPFTable infoTable, int heigth, STVerticalJc.Enum vertical) {
        List<XWPFTableRow> rows = infoTable.getRows();
        for (XWPFTableRow row : rows) {
            CTTrPr trPr = row.getCtRow().addNewTrPr();
            CTHeight ht = trPr.addNewTrHeight();
            ht.setVal(BigInteger.valueOf(heigth));
            List<XWPFTableCell> cells = row.getTableCells();
            for (XWPFTableCell tableCell : cells) {
                CTTcPr cttcpr = tableCell.getCTTc().addNewTcPr();
                cttcpr.addNewVAlign().setVal(vertical);
            }
        }
    }

    /**
     * 添加下一页分隔符
     *
     * @param document: word的document对象
     * @return void
     * @author yxc
     * @date 2022/1/18 11:21
     **/
    public static void createNextPageChar(XWPFDocument document) {
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setPageBreak(true);
    }

    /**
     * 将word输出为文件，返回文件路径
     *
     * @param outFile:  输出文件
     * @param document: word的document对象
     * @return java.lang.String
     * @author yxc
     * @date 2022/1/18 9:12
     **/
    public static String writeFile(File outFile, XWPFDocument document) {
        if (outFile == null) {
//            throw new BussinessException("文件为空");
        }
        if (!outFile.getParentFile().exists()) {
            // 文件夹不存在
            outFile.getParentFile().mkdirs();
        }
        try (FileOutputStream fos = new FileOutputStream(outFile)) {
            document.write(fos);
        } catch (Exception e) {
//            throw new BussinessException("文件写入失败");
        }
        return outFile.getPath();
    }


    public static void addTable() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("aaa", "122");//map的key为模板中替换的对象，value为替换的值
        InputStream is = PoiUtils.class.getResourceAsStream("/demo.docx");
        XWPFDocument document = new XWPFDocument(is);

        List<XWPFTable> tables = document.getTables();//获取表格
        Set<String> set = map.keySet();
        for (XWPFTable table : tables) {
            List<XWPFTableRow> rows = table.getRows();//获取行
            for (XWPFTableRow row : rows) {
                List<XWPFTableCell> cells = row.getTableCells();//获取单元格
                for (XWPFTableCell cell : cells) {
                    for (String s : set) {
                        //判断行是否包含map中的key，若包含，则用value替换行中key的内容
                        if (cell.getText().contains(s)) {
                            //用来存储替换后的行数据
                            String replace = "";
                            replace = cell.getText().replace(s, map.get(s));
                            //删除原来的行
                            cell.removeParagraph(0);
                            //新增一行
                            XWPFParagraph para = cell.addParagraph();
                            para.setSpacingBetween(1);
                            //填充数据
                            cell.setText(replace);
                        }
                    }
                }
            }
        }

        //表格中的段落
        for (XWPFTable table : tables) {
            List<XWPFTableRow> rows = table.getRows();//获取行
            for (XWPFTableRow row : rows) {
                List<XWPFTableCell> cells = row.getTableCells();//获取单元格
                for (XWPFTableCell cell : cells) {
                    //用以判断是否有值更改
                    int a = 0;
                    //记录单元格原值
                    String replace = cell.getText();
                    for (String s : set) {
                        //判断单元格的值是否包含key
                        if (cell.getText().contains(s)) {
                            a = 1;
                            //替换key
                            replace = replace.replace(s, map.get(s));
                        }
                    }
                    //有替换值
                    if (a == 1) {
                        //获取当前单元格行数
                        int size = cell.getParagraphs().size();
                        for (int i = 0; i < size; i++) {
                            //删除原数据
                            cell.removeParagraph(0);
                        }
                        //这个字符截取需要提前在word模板中在需要回车的地方加上'|'，不然的话是不会换行的
                        String[] jdcl = replace.split("\\|");
                        for (int i = 0; i < jdcl.length; i++) {
                            XWPFParagraph xwpfParagraph = cell.addParagraph();//新建段落
                            XWPFRun run = xwpfParagraph.createRun();//新建行
                            run.setText(jdcl[i]);
                        }
                    }
                }
            }
        }

//        //导出文件存在本地
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        document.write((OutputStream) byteArrayOutputStream);
//        String filePath = "D://uploads/demo/";//存储目的地
//        String fileName = "demo";//文件名称
//        File file = new File(filePath + fileName + ".docx");
//        //如果不存在文件夹则创建
//        if (!file.getParentFile().exists()) {
//            file.getParentFile().mkdirs();
//        }
//        FileOutputStream outputStream = new FileOutputStream(filePath + fileName +  ".docx");
//        outputStream.write(byteArrayOutputStream.toByteArray());
//        outputStream.flush();
//        outputStream.close();

    }

    public static void paragraphInTable() throws IOException {
        Map<String, String> map = new HashMap<>();
        map.put("aaa", "122");//map的key为模板中替换的对象，value为替换的值
        InputStream is = PoiUtils.class.getResourceAsStream("/demo.docx");
        XWPFDocument document = new XWPFDocument(is);

        Set<String> set = map.keySet();
        Iterator<XWPFParagraph> itPara = document.getParagraphsIterator();//获取word段落
        while (itPara.hasNext()) {
            XWPFParagraph paragraph = itPara.next();//获取段落
            Iterator<String> iterator = set.iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();//获取key
                List<XWPFRun> run = paragraph.getRuns();//获取行
                for (int i = 0; i < run.size(); i++) {
                    //如果key等于word中的值，就替换
                    if (run.get(i).getText(run.get(i).getTextPosition()) != null &&
                            run.get(i).getText(run.get(i).getTextPosition()).equals(key)) {
                        run.get(i).setText(map.get(key), 0);//0指从第一个字符开始替换
                    }
                }
            }
        }
        //导出文件存在本地


    }

    //页眉
    public static void header() throws IOException {
        Map<String, String> map = new HashMap<>();
        map.put("${a}", "123");
        InputStream is = PoiUtils.class.getResourceAsStream("/demo.docx");
        XWPFDocument document = new XWPFDocument(is);
        List<XWPFHeader> headerList = document.getHeaderList();

        //创建一个set
        Set<String> set = new HashSet<>();

        for (XWPFHeader xwpfHeader : headerList) {
            List<XWPFParagraph> paragraphs = xwpfHeader.getParagraphs();
            for (XWPFParagraph paragraph : paragraphs) {
                List<XWPFRun> runs = paragraph.getRuns();
                for (int i = 0; i < runs.size(); i++) {

                    for (String s : set) {
                        if (runs.get(i).getText(runs.get(i).getTextPosition()) != null &&
                                runs.get(i).getText(runs.get(i).getTextPosition()).contains(s)) {
                            String replace = runs.get(i).getText(runs.get(i).getTextPosition()).replace(s, map.get(s));
                            runs.get(i).setText(replace, 0);
                        }
                    }
                }
            }
        }

    }

    //页脚
    public static void foot() throws IOException {

        Map<String, String> map = new HashMap<>();
        map.put("${a}", "123");
        InputStream is = PoiUtils.class.getResourceAsStream("/demo.docx");
        XWPFDocument document = new XWPFDocument(is);


        //创建一个set
        Set<String> set = new HashSet<>();

        List<XWPFFooter> footerList = document.getFooterList();
        for (XWPFFooter xwpfFooter : footerList) {
            List<XWPFParagraph> paragraphs = xwpfFooter.getParagraphs();
            for (XWPFParagraph paragraph : paragraphs) {
                List<XWPFRun> runs = paragraph.getRuns();
                for (int i = 0; i < runs.size(); i++) {
                    for (String s : set) {
                        if (runs.get(i).getText(runs.get(i).getTextPosition()) != null &&
                                runs.get(i).getText(runs.get(i).getTextPosition()).contains(s)) {
                            String replace = runs.get(i).getText(runs.get(i).getTextPosition()).replace(s, map.get(s));
                            runs.get(i).setText(replace, 0);
                        }
                    }
                }
            }
        }

    }


    public static void mergeTable() throws IOException {
        //创建一个2X5的表格
        InputStream is = PoiUtils.class.getResourceAsStream("/demo.docx");
        XWPFDocument document = new XWPFDocument(is);

        XWPFTable table = document.createTable(2, 5);

        //使用第0行
        List<XWPFTableCell> tableName = table.getRow(0).getTableCells();
        tableName.get(0).setText("表名"); //为第0行第0列设置内容
        //将第一列到第四列合并
        for (int i = 1; i <= 4; i++) {
            //对单元格进行合并的时候,要标志单元格是否为起点,或者是否为继续合并
            if (i == 1)
                tableName.get(i).getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.RESTART);//这是起点
            else
                tableName.get(i).getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);//继续合并
        }
        tableName.get(1).setText("这是合并之后的表格");//为第1行1到4合并之后的单元格设置内容

//使用第1行, 有五个单元格
        List<XWPFTableCell> heads = table.getRow(1).getTableCells();
        for (int i = 0; i < 5; i++) {
            heads.get(i).setText("" + i);
        }

    }

    public void replaceParagraph(XWPFParagraph xWPFParagraph, Map<String, Object> parametersMap) {
        List<XWPFRun> runs = xWPFParagraph.getRuns();
        String xWPFParagraphText = xWPFParagraph.getText();
        String regEx = "\\{.+?\\}";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(xWPFParagraphText);//正则匹配字符串{****}

        if (matcher.find()) {
            // 查找到有标签才执行替换
            int beginRunIndex = xWPFParagraph.searchText("{", new PositionInParagraph()).getBeginRun();// 标签开始run位置
            int endRunIndex = xWPFParagraph.searchText("}", new PositionInParagraph()).getEndRun();// 结束标签
            StringBuffer key = new StringBuffer();

            if (beginRunIndex == endRunIndex) {
                // {**}在一个run标签内
                XWPFRun beginRun = runs.get(beginRunIndex);
                String beginRunText = beginRun.text();

                int beginIndex = beginRunText.indexOf("{");
                int endIndex = beginRunText.indexOf("}");
                int length = beginRunText.length();

                if (beginIndex == 0 && endIndex == length - 1) {
                    // 该run标签只有{**}
                    XWPFRun insertNewRun = xWPFParagraph.insertNewRun(beginRunIndex);
                    insertNewRun.getCTR().setRPr(beginRun.getCTR().getRPr());
                    // 设置文本
                    key.append(beginRunText.substring(1, endIndex));
//                    insertNewRun.setText(getValueBykey(key.toString(),parametersMap));
                    xWPFParagraph.removeRun(beginRunIndex + 1);
                } else {
                    // 该run标签为**{**}** 或者 **{**} 或者{**}**，替换key后，还需要加上原始key前后的文本
                    XWPFRun insertNewRun = xWPFParagraph.insertNewRun(beginRunIndex);
                    insertNewRun.getCTR().setRPr(beginRun.getCTR().getRPr());
                    // 设置文本
                    key.append(beginRunText.substring(beginRunText.indexOf("{") + 1, beginRunText.indexOf("}")));
//                    String textString=beginRunText.substring(0, beginIndex) + getValueBykey(key.toString(),parametersMap)
//                            + beginRunText.substring(endIndex + 1);

//                    insertNewRun.setText(textString);
                    xWPFParagraph.removeRun(beginRunIndex + 1);
                }

            } else {
                // {**}被分成多个run

                //先处理起始run标签,取得第一个{key}值
                XWPFRun beginRun = runs.get(beginRunIndex);
                String beginRunText = beginRun.text();
                int beginIndex = beginRunText.indexOf("{");
                if (beginRunText.length() > 1) {
                    key.append(beginRunText.substring(beginIndex + 1));
                }
                ArrayList<Integer> removeRunList = new ArrayList<Integer>();//需要移除的run
                //处理中间的run
                for (int i = beginRunIndex + 1; i < endRunIndex; i++) {
                    XWPFRun run = runs.get(i);
                    String runText = run.text();
                    key.append(runText);
                    removeRunList.add(i);
                }

                // 获取endRun中的key值
                XWPFRun endRun = runs.get(endRunIndex);
                String endRunText = endRun.text();
                int endIndex = endRunText.indexOf("}");
                //run中**}或者**}**
                if (endRunText.length() > 1 && endIndex != 0) {
                    key.append(endRunText.substring(0, endIndex));
                }


                //*******************************************************************
                //取得key值后替换标签

                //先处理开始标签
                if (beginRunText.length() == 2) {
                    // run标签内文本{
                    XWPFRun insertNewRun = xWPFParagraph.insertNewRun(beginRunIndex);
                    insertNewRun.getCTR().setRPr(beginRun.getCTR().getRPr());
                    // 设置文本
//                    insertNewRun.setText(getValueBykey(key.toString(),parametersMap));
                    xWPFParagraph.removeRun(beginRunIndex + 1);//移除原始的run
                } else {
                    // 该run标签为**{**或者 {** ，替换key后，还需要加上原始key前的文本
                    XWPFRun insertNewRun = xWPFParagraph.insertNewRun(beginRunIndex);
                    insertNewRun.getCTR().setRPr(beginRun.getCTR().getRPr());
                    // 设置文本

                    String textString = "";
//                            beginRunText.substring(0,beginRunText.indexOf("{"))+getValueBykey(key.toString(),parametersMap);
                    //	System.out.println(">>>>>"+textString);
                    //分行处理
                    if (textString.contains("@")) {
                        String[] textStrings = textString.split("@");
                        for (int i = 0; i < textStrings.length; i++) {
                            //System.out.println(">>>>>textStrings>>"+textStrings[i]);
                            insertNewRun.setText(textStrings[i]);
                            //insertNewRun.addCarriageReturn();
                            insertNewRun.addBreak();//换行
                        }
                    } else if (textString.endsWith(".png")) {
                        CTInline inline = insertNewRun.getCTR().addNewDrawing().addNewInline();
                        try {
//                            insertPicture(document,textString,
//                                    inline, 500,
//                                    200);
//                            document.createParagraph();// 添加回车换行
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    } else {
                        insertNewRun.setText(textString);
                    }


                    xWPFParagraph.removeRun(beginRunIndex + 1);//移除原始的run
                }

                //处理结束标签
                if (endRunText.length() == 1) {
                    // run标签内文本只有}
                    XWPFRun insertNewRun = xWPFParagraph.insertNewRun(endRunIndex);
                    insertNewRun.getCTR().setRPr(endRun.getCTR().getRPr());
                    // 设置文本
                    insertNewRun.setText("");
                    xWPFParagraph.removeRun(endRunIndex + 1);//移除原始的run

                } else {
                    // 该run标签为**}**或者 }** 或者**}，替换key后，还需要加上原始key后的文本
                    XWPFRun insertNewRun = xWPFParagraph.insertNewRun(endRunIndex);
                    insertNewRun.getCTR().setRPr(endRun.getCTR().getRPr());
                    // 设置文本
                    String textString = endRunText.substring(endRunText.indexOf("}") + 1);
                    insertNewRun.setText(textString);
                    xWPFParagraph.removeRun(endRunIndex + 1);//移除原始的run
                }

                //处理中间的run标签
                for (int i = 0; i < removeRunList.size(); i++) {
                    XWPFRun xWPFRun = runs.get(removeRunList.get(i));//原始run
                    XWPFRun insertNewRun = xWPFParagraph.insertNewRun(removeRunList.get(i));
                    insertNewRun.getCTR().setRPr(xWPFRun.getCTR().getRPr());
                    insertNewRun.setText("");
                    xWPFParagraph.removeRun(removeRunList.get(i) + 1);//移除原始的run
                }

            }// 处理${**}被分成多个run

            replaceParagraph(xWPFParagraph, parametersMap);

        }//if 有标签

    }

    public void forTables() {
//        XWPFTable newCreateTable = document.createTable();// 创建新表格,默认一行一列
//        List<XWPFTableRow> TempTableRows = templateTable.getRows();// 获取模板表格所有行
//        int tagRowsIndex = 0;// 标签行indexs
//        for (int i = 0, size = TempTableRows.size(); i < size; i++) {
//            String rowText = TempTableRows.get(i).getCell(0).getText();// 获取到表格行的第一个单元格
//            if (rowText.indexOf("##{foreachRows}##") > -1) {
//                tagRowsIndex = i;
//                break;
//            }
//        }
//
//        /* 复制模板行和标签行之前的行  即第一行的内容*/
//        for (int i = 1; i < tagRowsIndex; i++) {
//            XWPFTableRow newCreateRow = newCreateTable.createRow();
//            CopyTableRow(newCreateRow, TempTableRows.get(i));// 复制行
//            replaceTableRow(newCreateRow, parametersMap);// 处理不循环标签的替换
//        }
//
//        /* 循环生成模板行 */
//        XWPFTableRow tempRow = TempTableRows.get(tagRowsIndex + 1);// 获取到模板行
//        for (int i = 0; i < list.size(); i++) {
//            XWPFTableRow newCreateRow = newCreateTable.createRow();
//            CopyTableRow(newCreateRow, tempRow);// 复制模板行
//            replaceTableRow(newCreateRow, list.get(i));// 处理标签替换
//        }
//
//        /* 复制模板行和标签行之后的行 */
//        for (int i = tagRowsIndex + 2; i < TempTableRows.size(); i++) {
//            XWPFTableRow newCreateRow = newCreateTable.createRow();
//            CopyTableRow(newCreateRow, TempTableRows.get(i));// 复制行
//            replaceTableRow(newCreateRow, parametersMap);// 处理不循环标签的替换
//        }
//
//        newCreateTable.removeRow(0);// 移除多出来的第一行
//        document.createParagraph();// 添加回车换行
//
//
//        for (Map<String, Object> map : list) {
//            List<XWPFTableRow> templateTableRows = templateTable.getRows();// 获取模板表格所有行
//            XWPFTable newCreateTable = document.createTable();// 创建新表格,默认一行一列
//            for (int i = 1; i < templateTableRows.size(); i++) {
//                XWPFTableRow newCreateRow = newCreateTable.createRow();
//                CopyTableRow(newCreateRow, templateTableRows.get(i));// 复制模板行文本和样式到新行
//
//                /*表格复制时，边框不显示*/
//                for (int j = 0; j < newCreateRow.getTableCells().size(); j++) {
//                    CTTcBorders tblBorders = newCreateRow.getCell(j).getCTTc().getTcPr().addNewTcBorders();
//                    tblBorders.addNewLeft().setVal(STBorder.NIL);
//                    tblBorders.addNewRight().setVal(STBorder.NIL);
//                    tblBorders.addNewBottom().setVal(STBorder.NIL);
//                    tblBorders.addNewTop().setVal(STBorder.NIL);
//                    newCreateRow.getCell(j).getCTTc().getTcPr().setTcBorders(tblBorders);
//                }
//            }
//            newCreateTable.removeRow(0);// 移除多出来的第一行
//            document.createParagraph();// 添加回车换行
//            replaceTable(newCreateTable, map);//替换标签
//        }
//
//
//        @SuppressWarnings("unchecked")
//        Map<String,List<Map<String, Object>>> tableDataList = (Map<String,List<Map<String, Object>>>) dataMap
//                .get(dataSource);
//        table.getRow(1).getCell(0).setText("aaa1111");
//        //XWPFRun endRun = runs.get(0);
//        Map<String,Object> dd = new HashMap<String,Object>();
//        dd.put("aa","ssssssssss");
//
//        List<XWPFTableCell> cells = table.getRow(1).getTableCells();
//        for (XWPFTableCell xWPFTableCell : cells) {
//            List<XWPFParagraph> paragraphs = xWPFTableCell.getParagraphs();
//            for (XWPFParagraph xwpfParagraph : paragraphs) {
//
//                replaceParagraph(xwpfParagraph, dd);
//            }
//        }
//        addTableInDocFooter(table, tableDataList, parametersMap, "aaa");
//        addTableInDocFooter(table, tableDataList, parametersMap, "bbb");
    }

    //模板路径
    private final static String WORD_PATH = "static/docs/assistPolice.docx";

    private final String defaultStr = "-";
    private final BigDecimal defaultDecimal = new BigDecimal(0);

    /**
     * 导出个人档案Word
     * @param id
     * @return
     */
    public void exportAssisPoliceWordById(String id){
        //获取导出对象
//        AssistPolice assistPolice = (AssistPolice) setEmpty(selectExportWordAssistPoliceById(id));
        XWPFTemplate xwpfTemplate = null;
        FileOutputStream out = null;
        try {
            ClassPathResource template = new ClassPathResource(WORD_PATH);
            // Spring EL 无法容忍变量不存在，直接抛出异常，表达式计算引擎为Spring Expression Language

//            Configure config = Configure.newBuilder().setElMode(ELMode.SPEL_MODE).build();
            Configure config = Configure.builder().bind("title", new DocumentRenderPolicy()).build();


            xwpfTemplate = XWPFTemplate.compile(template.getInputStream()).render("assistPolice");
//            String filename = encodingWordFilename(assistPolice.getName()+"档案");
            String filename = "encodingWordFilename(assistPolice.getName())";
            out = new FileOutputStream(getAbsoluteFile(filename));
            // 输出流
            xwpfTemplate.write(out);
            out.flush();
            out.close();
            xwpfTemplate.close();
//            return AjaxResult.success(filename);

        }catch (Exception e){
            e.printStackTrace();
//            log.info(e.toString());
//            throw new BusinessException("导出个人档案失败，请联系网站管理员！");
        }finally{
            if (xwpfTemplate != null){
                try{
                    xwpfTemplate.close();
                }catch (IOException e1){
                    e1.printStackTrace();
                }
            }
            if (out != null){
                try{
                    out.close();
                }catch (IOException e1){
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取下载路径
     *
     * @param filename 文件名称
     */
    public String getAbsoluteFile(String filename) {
        //KcConfig.getDownloadPath()取配置文件中的下载路径  profile: C:/profile/
        //String downloadPath = KcConfig.getDownloadPath() + filename;
        String downloadPath = " profile: C:/profile/" + filename;
        File desc = new File(downloadPath);
        if (!desc.getParentFile().exists()) {
            desc.getParentFile().mkdirs();
        }
        return downloadPath;
    }

    /**
     * 编码word文件名
     */
    public String encodingWordFilename(String filename) {
        filename = UUID.randomUUID().toString().replaceAll("-", "") + "_" + replaceBlank(filename.trim()) + ".docx";
        return filename;
    }

    /**
     * 去掉字符串中的制表符
     *
     * @param str
     * @return
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    /**
     * 设置AssistPolice中为空的属性为''
     *
     * @param object
     * @return
     */
    @SuppressWarnings(value = {"rawtypes"})
    public Object setEmpty(Object object) {
        try {
            Class clazz = object.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                String fieldName = field.getName();
                Class fieldClass = field.getType();
                field.setAccessible(true); //设置访问权限
                if (fieldClass == Integer.class) {
                    field.set(object, defaultDecimal.intValue());
                } else if (fieldClass == Long.class) {
                    field.set(object, defaultDecimal.longValue());
                } else if (fieldClass == Float.class) {
                    field.set(object, defaultDecimal.doubleValue());
                } else if (fieldClass == BigDecimal.class) {
                    field.set(object, defaultDecimal);
                } else if (fieldClass == String.class) {
                    field.set(object, defaultStr); // 设置值
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return object;
    }


}


