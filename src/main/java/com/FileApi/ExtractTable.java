//package com.FileApi;
//
///**
// * @ClassName ExtractTable
// * @Description TODO
// * @Author QiBin
// * @Date 2021/7/2215:55
// * @Version 1.0
// **/
//
//
//import com.spire.doc.*;
//import com.spire.doc.collections.SectionCollection;
//import com.spire.doc.documents.*;
//import com.spire.doc.fields.DocPicture;
//import com.spire.doc.fields.TextBox;
//import com.spire.doc.fields.TextRange;
//
//import java.awt.*;
//import java.io.IOException;
//
//
//public class ExtractTable {
//    public static void main(String[] args) throws IOException {
//        //加载Word测试文档
//        Document doc = new Document();
//        doc.loadFromFile("/Users/qibin/Downloads/МИ-26Т2_rus的副本.docx");
//
//        SectionCollection sections = doc.getSections();
//        Section section = sections.get(0);
//
//        Table table = section.getTables().get(0);
//
//        //获取文本框
////        TextBox textbox = doc.getTextBoxes().get(0);
//
//        //获取文本框中的表格
////        Table table = textbox.getBody().getTables().get(0);
//
//        //保存到文本文件
//        //遍历表格中的段落并提取文本
//        for (int i = 0; i < table.getRows().getCount(); i++) {
//            TableRow row = table.getRows().get(i);
//            for (int j = 0; j < row.getCells().getCount(); j++) {
//                TableCell cell = row.getCells().get(j);
//                for (int k = 0; k < cell.getParagraphs().getCount(); k++) {
//                    Paragraph paragraph = cell.getParagraphs().get(k);
//                    String text = paragraph.getText();
//                    System.out.println(text);
//                    cell.getChildObjects().clear();
////                    paragraph.clearRunAttrs();
//                    paragraph.appendText("替换text");
////                    bw.write(paragraph.getText() + "\t");
//                }
//            }
////            bw.write("\r\n");
//        }
//
////        bw.flush();
////        bw.close();
////        fw.close();
//    }
//
//
//
//
//
//
//
//    /**
//     * 添加文本框中的表格
//     * <p>
//     * <p>
//     * //插入文字到文本框
//     * <p>
//     * para = tb.getBody().addParagraph();
//     * <p>
//     * TextRange textRange = para.appendText("圣诞老人，是一位身穿红袍、头戴红帽的白胡子老头。" +
//     * <p>
//     * "每年圣诞节他驾着鹿拉的雪橇从北方而来，由烟囱进入各家，把圣诞礼物装在袜子里挂在孩子们的床头上或火炉前。 ");
//     * <p>
//     * textRange.getCharacterFormat().setFontName("宋体");
//     * <p>
//     * textRange.getCharacterFormat().setFontSize(12f);
//     * <p>
//     * para.getFormat().setHorizontalAlignment(HorizontalAlignment.Center);
//     *
//     * @param
//     */
//    public static void writeTable() {
//        //创建文档
//        Document doc = new Document();
//
//        //添加指定大小的文本框
//        TextBox tb = doc.addSection().addParagraph().appendTextBox(380, 275);
//        //设置文字环绕方式
//        tb.getFormat().setTextWrappingStyle(TextWrappingStyle.Square);
//
//        //设置文本框的相对位置
//        tb.getFormat().setHorizontalOrigin(HorizontalOrigin.Left_Margin_Area);
//        tb.getFormat().setHorizontalPosition(120f);
//        tb.getFormat().setVerticalOrigin(VerticalOrigin.Page);
//        tb.getFormat().setVerticalPosition(100f);
//
//        //设置文本框边框样式
//        tb.getFormat().setLineStyle(TextBoxLineStyle.Thin_Thick);
//        tb.getFormat().setLineColor(Color.gray);
//
//        //插入图片到文本框
//        Paragraph para = tb.getBody().addParagraph();
//        DocPicture picture = para.appendPicture("5G.png");
//        picture.setHeight(120f);
//        picture.setWidth(180f);
////        para.getFormat().setHorizontalAlignment(HorizontalAlignment.Center);
////        para.getFormat().setAfterSpacing(13f);
//
//        //插入文字到文本框
//        para = tb.getBody().addParagraph();
//        TextRange textRange = para.appendText("中美贸易争端，又称中美贸易战，也叫中美贸易摩擦，是中美经济关系中的重要问题。 "
//                + "贸易争端主要发生在两个方面：一是中国具有比较优势的出口领域；"
//                + "二是中国没有优势的进口和技术知识领域。");
//        textRange.getCharacterFormat().setFontName("楷体");
//        textRange.getCharacterFormat().setFontSize(11f);
////        para.getFormat().setHorizontalAlignment(HorizontalAlignment.Center);
//
//        //添加表格到文本框
//        //声明数组内容
//        String[][] data = new String[][]{
//                new String[]{"中美进出口差额"},
//                new String[]{"国家", "年份", "出口额（美元）", "进口额（美元）"},
//                new String[]{"中国", "2017", "125468", "101109"},
//                new String[]{"美国", "2017", "86452", "124298"},
//        };
//        //添加表格
//        Table table = tb.getBody().addTable();
//        //指定表格行数、列数
//        table.resetCells(4, 4);
//        //将数组内容填充到表格
//        for (int i = 0; i < data.length; i++) {
//            TableRow dataRow = table.getRows().get(i);
//            dataRow.getCells().get(i).setWidth(70);
//            dataRow.setHeight(22);
//            dataRow.setHeightType(TableRowHeightType.Exactly);
//            for (int j = 0; j < data[i].length; j++) {
//                dataRow.getCells().get(j).getCellFormat().setVerticalAlignment(VerticalAlignment.Middle);
//                TextRange range2 = dataRow.getCells().get(j).addParagraph().appendText(data[i][j]);
//                range2.getCharacterFormat().setFontName("楷体");
//                range2.getCharacterFormat().setFontSize(11f);
////                range2.getOwnerParagraph().getFormat().setHorizontalAlignment(HorizontalAlignment.Center);
////                range2.getCharacterFormat().setBold(true);
//            }
//        }
//        TableRow row = table.getRows().get(1);
//        for (int z = 0; z < row.getCells().getCount(); z++) {
//            row.getCells().get(z).getCellFormat().setBackColor(new Color(176, 224, 238));
//        }
//        //横向合并单元格
//        table.applyHorizontalMerge(0, 0, 3);
//        //应用表格样式
//        table.applyStyle(DefaultTableStyle.Table_Grid_5);
//
//        //保存文档
////        doc.saveToFile("AddTextbox.docx", FileFormat.Docx_2013);
//        doc.dispose();
//    }
//}
