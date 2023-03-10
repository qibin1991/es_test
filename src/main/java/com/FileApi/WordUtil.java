package com.FileApi;

/**
 * @ClassName WordUtil
 * @Description TODO
 * @Author QiBin
 * @Date 2021/7/2217:30
 * @Version 1.0
 **/


import org.apache.poi.ooxml.POIXMLDocument;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Word工具类(带导出)
 * 依赖外部jar ：poi version 3.9
 * 支持 word 2003+
 * @author liyuzhuang
 *
 */
public class WordUtil {

    public static String tempFilePath = "/Users/qibin/Downloads/text/";

    public static void main(String[] args) throws Exception{
        Map<String, Object> param = new HashMap<String, Object>();
        Map<String,Object> header = new HashMap<String, Object>();
        header.put("width", 100);
        header.put("height", 50);
        header.put("type", "jpg");
        header.put("content", "f:\\test\\head.jpg");
        param.put("${12}$",header);
        param.put("${1}$", "左侧段落");
        param.put("${2}$", "text");
        param.put("${3}$", "such");
        param.put("${4}$", "男");
        param.put("${5}$", "2017/08/04");
        param.put("${6}$", "头像.jpg");
        param.put("${7}$", "14584df545656");
        param.put("${8}$", "36°");
        param.put("${9}$", "28mol");
        param.put("${10}$", "36mol");
        param.put("${11}$", "78g");
        param.put("${date}$", "2018");
        String result = replaceAndGenerateWord("/Users/qibin/Downloads/МИ-26Т2_rus的副本.docx", param, "text替换测试.docx", null, null);

//        POIFSFileSystem pfs = new POIFSFileSystem(in);
//        HWPFDocument hwpf = new HWPFDocument(pfs);

    }

    /**
     * 替换word中的自定义字符串以及图片（适用于word2003+ 版本）
     *
     * 注：2003版本word不支持替换图片，2007版本以上可以替换图片
     *
     * @param filePath
     * @param param
     * @param fileName
     * @param request
     * @param response
     * @return
     */
    public static String replaceAndGenerateWord(String filePath, Map<String, Object> param, String fileName, HttpServletRequest request, HttpServletResponse response){
        String[] sp = filePath.split("\\.");
        //判断文件是否有后缀名
        if(sp.length > 0){
            try{
                //处理docx文档 2007-2013
                if(sp[sp.length - 1].equalsIgnoreCase("docx")){
//                    CustomXWPFDocument document = null;
                    OPCPackage pack = POIXMLDocument.openPackage(filePath);
//                    document = new CustomXWPFDocument(pack);
                    XWPFDocument document = new XWPFDocument(pack);


                    if (param != null && param.size() > 0) {
                        //处理段落
                        List<XWPFParagraph> paragraphList = document.getParagraphs();
                        processParagraphs(paragraphList, param, document);
                        //处理表格
                        Iterator<XWPFTable> it = document.getTablesIterator();
                        while (it.hasNext()) {
                            XWPFTable table = it.next();
                            List<XWPFTableRow> rows = table.getRows();
                            for (XWPFTableRow row : rows) {
                                List<XWPFTableCell> cells = row.getTableCells();
                                for (XWPFTableCell cell : cells) {
                                    List<XWPFParagraph> paragraphListTable =  cell.getParagraphs();
                                    processParagraphs(paragraphListTable, param, document);
                                }
                            }
                        }
                        createDir(tempFilePath);
                        FileOutputStream fos = new FileOutputStream(new File(tempFilePath.concat(fileName)));
                        document.write(fos);
                        fos.flush();
                        fos.close();
//                        doExport(fileName, tempFilePath.concat(fileName), request, response);
                        return tempFilePath.concat(fileName);
                    }
                    //处理doc文档 97-2003
                }
//                else if(sp[sp.length - 1].equalsIgnoreCase("doc")){
//                    HWPFDocument document = null;
//                    document = new HWPFDocument(new FileInputStream(filePath));
//                    Range range = document.getRange();
//                    for (Map.Entry<String, Object> entry : param.entrySet()) {
//                        Object value = entry.getValue();
//                        if(value instanceof String){
//                            range.replaceText(entry.getKey(), entry.getValue().toString());
//                        }else if(value instanceof Map){
//                            //TODO word2003暂时不能处理图片
//                        }
//                    }
//                    createDir(tempFilePath);
//                    FileOutputStream fos = new FileOutputStream(new File(tempFilePath.concat(fileName)));
//                    document.write(fos);
//                    fos.flush();
//                    fos.close();
//                    doExport(fileName, tempFilePath.concat(fileName), request, response);
//                    return tempFilePath.concat(fileName);
//                }
            }catch(Exception e){
                return "fail";
            }
        }else{
            return "fail";
        }
        return "success";
    }

    /**
     * 处理段落
     * @param paragraphList
     * @throws FileNotFoundException
     * @throws InvalidFormatException
     */
    public static void processParagraphs(List<XWPFParagraph> paragraphList,Map<String, Object> param,XWPFDocument doc) throws InvalidFormatException, FileNotFoundException{
        if(paragraphList != null && paragraphList.size() > 0){
            //首选循环段落
            for(XWPFParagraph paragraph:paragraphList){
                //获取段落的text
                boolean needDel = false;

                XWPFDocument document = paragraph.getDocument();
                    List<XWPFTable> tables1 = document.getTables();
                if (tables1.size() > 0){
                    XWPFTable tableArray = document.getTableArray(0);
                    List<XWPFTableRow> rows = tableArray.getRows();
                    System.out.println(rows.size());
                }


                List<XWPFRun> runs = paragraph.getRuns();
                for (XWPFRun run : runs) {
                    if (run.isItalic())

                    for (int i = 0; i < run.getCTR().sizeOfTArray(); i++) {
                        String text = run.getText(i);

                        if ("Длина, м".equals(text)){
                            System.out.println("======Длина, м=====");
                        }
                    }
                    if (run.text().equals("Длина, м")){
                        System.out.println("===============");
                    }

                }



                BodyType partType = paragraph.getPartType();
                 if ("TABLECELL".equals(partType)){
                    IBody body = paragraph.getBody();
                    List<XWPFTable> tables = body.getTables();
                    System.out.println(tables.size());
                    for (XWPFTable table : tables) {
                        List<XWPFTableRow> rows = table.getRows();
                        for (XWPFTableRow row : rows) {
                            List<XWPFTableCell> tableICells = row.getTableCells();
                            for (XWPFTableCell tableICell : tableICells) {
                                String text = tableICell.getText();
                                System.out.println(text);
                                List<XWPFParagraph> paragraphs = tableICell.getParagraphs();

                                tableICell.removeParagraph(0);
                                tableICell.setText("替换text");

                            }

                        }
                    }


                }

//                String text = paragraph.getText();
//                if(text != null){
//                    text = "替换text";
//                    for (Entry<String, Object> entry : param.entrySet()) {
//                        String key = entry.getKey();
//                        Object value = entry.getValue();
//                        //替换
//                        if(value instanceof String){
//                            String text2 = text.replace(key, value.toString());
//                            if(!text2.equals(text)){
//                                needDel = true;
//                            }
//                            text = text2;
//                        }
//                        else if(value instanceof Map){
//                            if(text.indexOf(key) != -1){
//                                //特殊处理图片
//                                int length = paragraph.getRuns().size();
//                                //将原有的Run去掉
//                                if (length > 0) {
//                                    for (int i = (length - 1); i >= 0; i--) {
//                                        paragraph.removeRun(i);
//                                    }
//                                }
//                                String imgurl = (String)((Map<?, ?>) value).get("content");
//                                String type = (String)((Map<?, ?>) value).get("type");
//                                int width = (Integer) ((Map<?, ?>) value).get("width");
//                                int height = (Integer) ((Map<?, ?>) value).get("height");
//                                String blipId = doc.addPictureData(new FileInputStream(new File(imgurl)), getPictureType(type));
//                                doc.createPicture(blipId,doc.getNextPicNameNumber(getPictureType(type)), width, height,paragraph);
//                            }
//                        }
//                    }
//                }
//                int length = paragraph.getRuns().size();
//                //将原有的Run去掉（原因是paragraph将XWPFRun分割成了一个乱七八糟的数组，例：${1}$，这个获取出来的是[$,{,1,},$],不能满足我们替换的要求，这里进行特殊处理）
//                if(needDel){
//                    if (length > 0) {
//                        for (int i = (length - 1); i >= 0; i--) {
//                            paragraph.removeRun(i);
//                        }
//                        //在段落里面插入我们替换过后的文本
//                        XWPFRun newRun = paragraph.insertNewRun(0);
//                        newRun.setText(text, 0);
//                        paragraph.addRun(newRun);
//                    }
//                }
            }
        }
    }

    /**
     * 根据图片类型，取得对应的图片类型代码
     * @param picType
     * @return int
     */
    private static int getPictureType(String picType){
        int res = CustomXWPFDocument.PICTURE_TYPE_PICT;
        if(picType != null){
            if(picType.equalsIgnoreCase("png")){
                res = CustomXWPFDocument.PICTURE_TYPE_PNG;
            }else if(picType.equalsIgnoreCase("dib")){
                res = CustomXWPFDocument.PICTURE_TYPE_DIB;
            }else if(picType.equalsIgnoreCase("emf")){
                res = CustomXWPFDocument.PICTURE_TYPE_EMF;
            }else if(picType.equalsIgnoreCase("jpg") || picType.equalsIgnoreCase("jpeg")){
                res = CustomXWPFDocument.PICTURE_TYPE_JPEG;
            }else if(picType.equalsIgnoreCase("wmf")){
                res = CustomXWPFDocument.PICTURE_TYPE_WMF;
            }
        }
        return res;
    }

    /**
     * 导出
     *
     * @param fileName
     * @param filePath
     * @param request
     * @param response
     */
    public static void doExport(String fileName, String filePath, HttpServletRequest request, HttpServletResponse response){
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        File file = null;
//        HttpServletResponse response = (HttpServletResponse)RpcContext.getContext().getResponse(HttpServletResponse.class);
        try {
            file = new File(filePath);
//        HttpServletRequest request = (HttpServletRequest)RpcContext.getContext().getRequest(HttpServletRequest.class);
            request.setCharacterEncoding("UTF-8");
            String agent = request.getHeader("User-Agent").toUpperCase();
            if ((agent.indexOf("MSIE") > 0) || ((agent.indexOf("RV") != -1) && (agent.indexOf("FIREFOX") == -1)))
                fileName = URLEncoder.encode(fileName, "UTF-8");
            else {
                fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
            }
            response.setContentType("application/x-msdownload;");
            response.setHeader("Content-disposition", "attachment; filename=" + fileName);
            response.setHeader("Content-Length", String.valueOf(file.length()));
            bis = new BufferedInputStream(new FileInputStream(file));
            bos = new BufferedOutputStream(response.getOutputStream());

            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length)))
                bos.write(buff, 0, bytesRead);
        }
        catch (Exception e) {
//          System.out.println("导出文件失败！");
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (bos != null) {
                    bos.close();
                }
                file.delete();
            } catch (Exception e) {
//            LOGGER.error("导出文件关闭流出错！", e);
            }
        }
    }

    /**
     * 创建目录
     * @param basePath
     */
    public static void createDir(String basePath)
    {
        File file = new File(basePath);
        if (!file.exists())
            file.mkdirs();
    }

}

