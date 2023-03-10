package com.FileApi;


import org.apache.poi.ooxml.POIXMLDocument;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

/**
 * @ClassName PoiDoc
 * @Description TODO
 * @Author QiBin
 * @Date 2021/7/2217:14
 * @Version 1.0
 **/
public class PoiDoc {
    public static void main(String[] args) {

        String filePath = "/Users/qibin/Downloads/МИ-26Т2_rus的副本.docx";
        File isExist = new File(filePath);
        /**判断源文件是否存在*/
//        if(!isExist.exists()){
//            return "源文件不存在！";
//        }
        XWPFDocument document;
        try {
            /**打开word2007的文件*/
            OPCPackage opc = POIXMLDocument.openPackage(filePath);
            document = new XWPFDocument(opc);
            /**替换word2007的纯文本内容*/
//            List<XWPFRun> listRun;
//            List<XWPFParagraph> listParagraphs = document.getParagraphs();
//            for (int i = 0; i < listParagraphs.size(); i++) {
//                listRun = listParagraphs.get(i).getRuns();
//                for (int j = 0; j < listRun.size(); j++) {
//                    if("#{text}#".equals(listRun.get(j).getText(0))){
//                        listRun.get(j).setText("替换的纯文本内容！",0);
//                    }
//                }
//            }
            /**取得文本的所有表格*/
            Iterator<XWPFTable> it = document.getTablesIterator();
            while(it.hasNext()){/**循环操作表格*/
                XWPFTable table = it.next();
                List<XWPFTableRow> rows = table.getRows();
                for(XWPFTableRow row:rows){/**取得表格的行*/
                    List<XWPFTableCell> cells = row.getTableCells();
                    for(XWPFTableCell cell:cells){/**取得单元格*/
//                        if("#{img}#".equals(cell.getText())){/**判断单元格的内容是否为需要替换的图片内容*/
//                            File pic = new File("E:\\test\\xiaosimm.png");
//                            FileInputStream is = new FileInputStream(pic);
//                            cell.removeParagraph(0);
//                            XWPFParagraph pargraph = cell.addParagraph();
//                            document.addPictureData(is, XWPFDocument.PICTURE_TYPE_PNG);
//                            document.createPicture(document.getAllPictures().size()-1, 600, 395, pargraph);
//                            if(is != null){
//                                is.close();
//                            }
//                        }
//                        List<XWPFParagraph> pars = cell.getParagraphs();
//                        for(XWPFParagraph par:pars){
//                            List<XWPFRun> runs = par.getRuns();
//                            for(XWPFRun run:runs){
//                                run.removeBreak();
//                            }
//                        }
                        String text = cell.getText();
                        System.out.println(text+"\t");
                        cell.removeParagraph(0);
                        cell.setText("替换表格中的文本内容！");
                        if("#{table}#".equals(cell.getText())){/**判断单元格中是否为需要替换的文本内容*/
                            cell.removeParagraph(0);
                            cell.setText("替换表格中的文本内容！");
                        }
                    }
                }
            }
            String downloadPath = "/Users/qibin/Downloads/textDoc测试.doc";
            OutputStream os = new FileOutputStream(downloadPath);
            document.write(os);
            if(os != null){
                os.close();
            }
            if(opc != null){
                opc.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
