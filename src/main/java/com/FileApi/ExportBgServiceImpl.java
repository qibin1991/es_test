package com.FileApi;

/**
 * @ClassName ExportBgServiceImpl
 * @Description TODO
 * @Author QiBin
 * @Date 2022/2/1814:01
 * @Version 1.0
 **/


import org.apache.poi.ooxml.POIXMLDocument;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;

import java.io.File;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class ExportBgServiceImpl {

    private static final String bookmark = "tpBookmark";// 报告图片位置的书签名


    public void exportBg(OutputStream out) {
        String srcPath = "D:/tp/fx.docx";
        String targetPath = "D:/tp/fx2.docx";
        String key = "$key";// 在文档中需要替换插入表格的位置
        XWPFDocument doc = null;
        File targetFile = null;
        try {
            doc = new XWPFDocument(POIXMLDocument.openPackage(srcPath));
            List<XWPFParagraph> paragraphList = doc.getParagraphs();
            if (paragraphList != null && paragraphList.size() > 0) {
                for (XWPFParagraph paragraph : paragraphList) {
                    List<XWPFRun> runs = paragraph.getRuns();
                    for (int i = 0; i < runs.size(); i++) {
                        String text = runs.get(i).getText(0).trim();
                        if (text != null) {
                            if (text.indexOf(key) >= 0) {
                                runs.get(i).setText(text.replace(key, ""), 0);
                                XmlCursor cursor = paragraph.getCTP().newCursor();
                                // 在指定游标位置插入表格
                                XWPFTable table = doc.insertNewTbl(cursor);

                                CTTblPr tablePr = table.getCTTbl().getTblPr();
                                CTTblWidth width = tablePr.addNewTblW();
                                width.setW(BigInteger.valueOf(8500));

                                this.inserInfo(table);

                                break;
                            }
                        }
                    }
                }
            }

            doc.write(out);
            out.flush();
            out.close();
        } catch (Exception e) {
//            throw new SysException(ERRORConstants.COMMON_SYSTEM_ERROR, e);
        }
    }

    /**
     * 把信息插入表格
     *
     * @param table
     */
    private void inserInfo(XWPFTable table) {
        List<DTO> data = new ArrayList<>();//需要插入的数据
        XWPFTableRow row = table.getRow(0);
        XWPFTableCell cell = null;
        for (int col = 1; col < 6; col++) {//默认会创建一列，即从第2列开始
            // 第一行创建了多少列，后续增加的行自动增加列
            CTTcPr cPr = row.createCell().getCTTc().addNewTcPr();
            CTTblWidth width = cPr.addNewTcW();
            if (col == 1 || col == 2 || col == 4) {
                width.setW(BigInteger.valueOf(2000));
            }
        }
        row.getCell(0).setText("指标");
        row.getCell(1).setText("指标说明");
        row.getCell(2).setText("公式");
        row.getCell(3).setText("参考值");
        row.getCell(4).setText("说明");
        row.getCell(5).setText("计算值");
//        for(DTO item : data){
//            row = table.createRow();
//            row.getCell(0).setText(item.getZbmc());
//            cell = row.getCell(1);
//            cell.getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(2000));
//            cell.setText(item.getZbsm());
//            cell = row.getCell(2);
//            cell.getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(2000));
//            cell.setText(item.getJsgs());
//            if(item.getCkz()!=null&&!item.getCkz().contains("$")){
//                row.getCell(3).setText(item.getCkz());
//            }
//            cell = row.getCell(4);
//            cell.getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(2000));
//            cell.setText(item.getSm());
//            row.getCell(5).setText(item.getJsjg()==null?"无法计算":item.getJsjg());
//        }
    }

    // word跨列合并单元格
    public void mergeCellsHorizontal(XWPFTable table, int row, int fromCell, int toCell) {
        for (int cellIndex = fromCell; cellIndex <= toCell; cellIndex++) {
            XWPFTableCell cell = table.getRow(row).getCell(cellIndex);
            if (cellIndex == fromCell) {
                // The first merged cell is set with RESTART merge value
                cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.RESTART);
            } else {
                // Cells which join (merge) the first one, are set with CONTINUE
                cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
            }
        }
    }

    // word跨行并单元格
    public void mergeCellsVertically(XWPFTable table, int col, int fromRow, int toRow) {
        for (int rowIndex = fromRow; rowIndex <= toRow; rowIndex++) {
            XWPFTableCell cell = table.getRow(rowIndex).getCell(col);
            if (rowIndex == fromRow) {
                // The first merged cell is set with RESTART merge value
                cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.RESTART);
            } else {
                // Cells which join (merge) the first one, are set with CONTINUE
                cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.CONTINUE);
            }
        }
    }

//    public static void main(String[] args) {
//// TODO code application logic here
//
//        try{
//            Document doc = new Document("template.docx");
//
//            Table table = (Table) doc.getChild(NodeType.TABLE, 1, true); //第2个表格
//
//// We want to merge the range of cells found in between these two cells.
//
//            Cell cellStartRange = table.getRows().get(1).getCells().get(0); //第2行第1列
//
//            Cell cellEndRange = table.getRows().get(2).getCells().get(0); //第3行第1列
//
//// Merge all the cells between the two specified cells into one.
//
//            mergeCells(cellStartRange, cellEndRange);
//
//            cellStartRange = table.getRows().get(3).getCells().get(0); //第4行第1列
//
//            cellEndRange = table.getRows().get(5).getCells().get(0); //第6行第1列
//
//// Merge all the cells between the two specified cells into one.
//
//            mergeCells(cellStartRange, cellEndRange);
//
//            doc.save(“out.docx”);
//
//            System.out.println(“Done”);
//
//        }
//
//        catch(Exception e) {
//            System.out.println(“error:”+e.getMessage() );
//
//        }
//
//    }
    /**

     * Merges the range of cells found between the two specified cells both

     * horizontally and vertically. Can span over multiple rows.

     * @param startCell

     * @param endCell

     */

//    public static void mergeCells(Cell startCell, Cell endCell) {
//        Table parentTable = startCell.getParentRow().getParentTable();
//
//// Find the row and cell indices for the start and end cell.
//
//        Point startCellPos = new Point(startCell.getParentRow().indexOf(startCell), parentTable.indexOf(startCell.getParentRow()));
//
//        Point endCellPos = new Point(endCell.getParentRow().indexOf(endCell), parentTable.indexOf(endCell.getParentRow()));
//
//// Create the range of cells to be merged based off these indices. Inverse each index if the end cell if before the start cell.
//
//        Rectangle mergeRange = new Rectangle(Math.min(startCellPos.x, endCellPos.x), Math.min(startCellPos.y, endCellPos.y), Math.abs(endCellPos.x – startCellPos.x) + 1,
//
//                Math.abs(endCellPos.y – startCellPos.y) + 1);
//
//        for (Row row : parentTable.getRows()) {
//            for (Cell cell : row.getCells()) {
//                Point currentPos = new Point(row.indexOf(cell), parentTable.indexOf(row));
//
//// Check if the current cell is inside our merge range then merge it.
//
//                if (mergeRange.contains(currentPos)) {
//                    if (currentPos.x == mergeRange.x)
//
//                        cell.getCellFormat().setHorizontalMerge(CellMerge.FIRST);
//
//                    else
//
//                        cell.getCellFormat().setHorizontalMerge(CellMerge.PREVIOUS);
//
//                    if (currentPos.y == mergeRange.y)
//
//                        cell.getCellFormat().setVerticalMerge(CellMerge.FIRST);
//
//                    else
//
//                        cell.getCellFormat().setVerticalMerge(CellMerge.PREVIOUS);
//
//                }
//
//            }
//
//        }
//
//    }



}
