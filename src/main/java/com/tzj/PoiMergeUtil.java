package com.tzj;

import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * @author yzw
 * @date 2022/02/19 00:30
 */
@Slf4j
public class PoiMergeUtil {

    public static void merge(List<Workbook> workbooks, String destFile) {

        try (
            FileOutputStream os = new FileOutputStream(destFile);
        ) {
            Workbook wb = workbooks.get(0);
            Sheet toSheet = wb.getSheetAt(0);
            int lastRowNum = toSheet.getLastRowNum();

            for (int i = 1; i < workbooks.size(); i++) {
                Workbook fromWb = workbooks.get(i);
                Sheet fromSheet = fromWb.getSheetAt(0);

                int rowNum = fromSheet.getLastRowNum() - fromSheet.getFirstRowNum();
                for (int j = 0; j <= rowNum; j++) {
                    Row toRow = toSheet.createRow(lastRowNum + j + 1);
                    Row fromRow = fromSheet.getRow(j);

                    copyRow(wb, fromRow, toRow, true, lastRowNum);
                }

                lastRowNum += rowNum + 1;
            }

            wb.write(os);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 行复制功能
     *
     * @param wb            工作簿
     * @param fromRow       从哪行开始
     * @param toRow         目标行
     * @param copyValueFlag true则连同cell的内容一起复制
     */
    public static void copyRow(Workbook wb, Row fromRow, Row toRow, boolean copyValueFlag, int lastRowNum) {
        toRow.setHeight(fromRow.getHeight());

        for (Iterator<Cell> cellIt = fromRow.cellIterator(); cellIt.hasNext(); ) {
            Cell tmpCell = cellIt.next();
            Cell newCell = toRow.createCell(tmpCell.getColumnIndex());
            copyCell(wb, tmpCell, newCell, copyValueFlag);
        }

        Sheet fromSheet = fromRow.getSheet();
        Sheet toSheet = toRow.getSheet();

        for (int i = 0; i < fromSheet.getNumMergedRegions(); i++) {
            CellRangeAddress cellRangeAddress = fromSheet.getMergedRegion(i);
            if (cellRangeAddress.getFirstRow() == fromRow.getRowNum()) {
                CellRangeAddress newCellRangeAddress = new CellRangeAddress(toRow.getRowNum(),
                    (toRow.getRowNum() + (cellRangeAddress.getLastRow() - cellRangeAddress.getFirstRow())),
                    cellRangeAddress.getFirstColumn(), cellRangeAddress.getLastColumn());
                toSheet.addMergedRegionUnsafe(newCellRangeAddress);
            }
        }
    }

    /**
     * 复制单元格
     *
     * @param srcCell
     * @param distCell
     * @param copyValueFlag true则连同cell的内容一起复制
     */
    public static void copyCell(Workbook wb, Cell srcCell, Cell distCell, boolean copyValueFlag) {
        CellStyle newStyle = wb.createCellStyle();
        CellStyle srcStyle = srcCell.getCellStyle();

        newStyle.cloneStyleFrom(srcStyle);
        newStyle.setFont(wb.getFontAt(srcStyle.getFontIndex()));

        // 样式
        distCell.setCellStyle(newStyle);

        // 内容
        if (srcCell.getCellComment() != null) {
            distCell.setCellComment(srcCell.getCellComment());
        }

        // 不同数据类型处理
        CellType srcCellType = srcCell.getCellTypeEnum();
        distCell.setCellType(srcCellType);

        if (copyValueFlag) {
            if (srcCellType == CellType.NUMERIC) {
                if (DateUtil.isCellDateFormatted(srcCell)) {
                    distCell.setCellValue(srcCell.getDateCellValue());
                } else {
                    distCell.setCellValue(srcCell.getNumericCellValue());
                }
            } else if (srcCellType == CellType.STRING) {
                distCell.setCellValue(srcCell.getRichStringCellValue());
            } else if (srcCellType == CellType.BLANK) {

            } else if (srcCellType == CellType.BOOLEAN) {
                distCell.setCellValue(srcCell.getBooleanCellValue());
            } else if (srcCellType == CellType.ERROR) {
                distCell.setCellErrorValue(srcCell.getErrorCellValue());
            } else if (srcCellType == CellType.FORMULA) {
                distCell.setCellFormula(srcCell.getCellFormula());
            }
        }
    }

}
