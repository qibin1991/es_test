package com.tzj;

/**
 * @ClassName ExtractTablePdf
 * @Description TODO
 * @Author QiBin
 * @Date 2022/2/2510:19
 * @Version 1.0
 **/
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import java.io.File;
import java.io.IOException;

public class ExtractTablePdf {
    public static void main(String[] args) throws IOException {
//        //加载PDF文档
//        PdfDocument pdf = new PdfDocument();
//        pdf.loadFromFile("/Users/qibin/Downloads/a.pdf");
//
//        //创建StringBuilder类的实例
//        StringBuilder builder = new StringBuilder();
//
//        //抽取表格
//        PdfTableExtractor extractor = new PdfTableExtractor(pdf);
//        com.spire.pdf.utilities.PdfTable[] tableLists;
//        for (int page = 0; page < pdf.getPages().getCount(); page++) {
//
//            tableLists = extractor.extractTable(page);
//            if (tableLists != null && tableLists.length > 0) {
//                for (PdfTable table : tableLists) {
//                    int row = table.getRowCount();
//                    int column = table.getColumnCount();
//                    for (int i = 0; i < row; i++) {
//                        for (int j = 0; j < column; j++) {
//                            String text = table.getText(i, j);
//                            builder.append(text + " ");
//                        }
//                        builder.append("\r\n");
//                    }
//                }
//            }
//
//        }
//
//        //将提取的表格内容写入txt文档
//        FileWriter fileWriter = new FileWriter("/Users/qibin/Downloads/c.txt");
//        fileWriter.write(builder.toString());
//        fileWriter.flush();
//        fileWriter.close();
//
//
        try {

            PDDocument document = PDDocument.load(new File("/Users/qibin/Downloads/a.pdf"));

            PDFTextStripper textStripper = new PDFTextStripper();

            textStripper.setSortByPosition(true);

            String text = textStripper.getText(document);

            System.out.println(text);

            document.close();

        } catch (IOException e) {

            e.printStackTrace();

        }


    }
}
