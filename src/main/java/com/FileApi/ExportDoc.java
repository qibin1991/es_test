package com.FileApi;

/**
 * @ClassName ExportDoc
 * @Description TODO
 * @Author QiBin
 * @Date 2021/7/2219:44
 * @Version 1.0
 **/

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.io.FileInputStream;
import java.util.Iterator;
import java.util.List;

/**
 *
 * 读取word文档中表格数据，支持doc、docx
 * @author Fise19
 *
 */
public class ExportDoc {
    public static void main(String[] args) {
        ExportDoc test = new ExportDoc();
        String filePath = "/Users/qibin/Downloads/МИ-26Т2_rus的副本.docx";
//		String filePath = "D:\\new\\测试.doc";
        test.testWord(filePath);
    }
    /**
     * 读取文档中表格
     * @param filePath
     */
    public void testWord(String filePath){
        try{
            FileInputStream in = new FileInputStream(filePath);//载入文档
            // 处理docx格式 即office2007以后版本
            if(filePath.toLowerCase().endsWith("docx")){
                //word 2007 图片不会被读取， 表格中的数据会被放在字符串的最后
                XWPFDocument xwpf = new XWPFDocument(in);//得到word文档的信息
                Iterator<XWPFTable> it = xwpf.getTablesIterator();//得到word中的表格
                // 设置需要读取的表格  set是设置需要读取的第几个表格，total是文件中表格的总数
//                int set = 1, total = 4;
//                int num = set;
                // 过滤前面不需要的表格
//                for (int i = 0; i < set-1; i++) {
//                    it.hasNext();
//                    it.next();
//                }
                while(it.hasNext()){
                    XWPFTable table = it.next();
                    System.out.println("这是第" + 1 + "个表的数据");
                    List<XWPFTableRow> rows = table.getRows();
                    //读取每一行数据
                    for (int i = 0; i < rows.size(); i++) {
                        XWPFTableRow  row = rows.get(i);
                        //读取每一列数据
                        List<XWPFTableCell> cells = row.getTableCells();
                        for (int j = 0; j < cells.size(); j++) {
                            XWPFTableCell cell = cells.get(j);
                            //输出当前的单元格的数据
                            System.out.print(cell.getText() + "\t");
                        }
                        System.out.println();
                    }
                    // 过滤多余的表格
//                    while (num < total) {
//                        it.hasNext();
//                        it.next();
//                        num += 1;
//                    }
                }
            }
//            else{
//                // 处理doc格式 即office2003版本
//                POIFSFileSystem pfs = new POIFSFileSystem(in);
//                HWPFDocument hwpf = new HWPFDocument(pfs);
//                Range range = hwpf.getRange();//得到文档的读取范围
//                TableIterator it = new TableIterator(range);
//                // 迭代文档中的表格
//                // 如果有多个表格只读取需要的一个 set是设置需要读取的第几个表格，total是文件中表格的总数
//                int set = 1, total = 4;
//                int num = set;
//                for (int i = 0; i < set-1; i++) {
//                    it.hasNext();
//                    it.next();
//                }
//                while (it.hasNext()) {
//                    Table tb = (Table) it.next();
//                    System.out.println("这是第" + num + "个表的数据");
//                    //迭代行，默认从0开始,可以依据需要设置i的值,改变起始行数，也可设置读取到那行，只需修改循环的判断条件即可
//                    for (int i = 0; i < tb.numRows(); i++) {
//                        TableRow tr = tb.getRow(i);
//                        //迭代列，默认从0开始
//                        for (int j = 0; j < tr.numCells(); j++) {
//                            TableCell td = tr.getCell(j);//取得单元格
//                            //取得单元格的内容
//                            for(int k = 0; k < td.numParagraphs(); k++){
//                                Paragraph para = td.getParagraph(k);
//                                String s = para.text();
//                                //去除后面的特殊符号
//                                if(null != s && !"".equals(s)){
//                                    s = s.substring(0, s.length()-1);
//                                }
//                                System.out.print(s + "\t");
//                            }
//                        }
//                        System.out.println();
//                    }
//                    // 过滤多余的表格
//                    while (num < total) {
//                        it.hasNext();
//                        it.next();
//                        num += 1;
//                    }
//                }
//            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

