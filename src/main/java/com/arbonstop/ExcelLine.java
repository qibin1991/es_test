package com.arbonstop;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.io.IOException;

/*
 * @ClassName ExcelLine
 * @Description TODO
 * @Author QiBin
 * @Date 2022/3/110:00
 * @Version 1.0
 **/
public class ExcelLine {


    public static void main(String[] args) throws IOException {
//        ExcelReader reader = ExcelUtil.getReader(new File("/Users/qibin/Downloads/蒸汽转换计算工具(1).xlsm"));

        FileInputStream fis = new FileInputStream("/Users/qibin/Downloads/蒸汽转换计算工.xlsm");
        Workbook wb = new XSSFWorkbook(fis); //or new XSSFWorkbook("c:/temp/test.xls")
        Sheet sheet = wb.getSheetAt(0);
        FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

// suppose your formula is in B3
        CellReference cellReference = new CellReference("B8");
        Row row = sheet.getRow(cellReference.getRow());
        Cell cell = row.getCell(cellReference.getCol());

        CellValue cellValue = evaluator.evaluate(cell);

        CellType cellType = cell.getCellType();
        String name = cellType.name();
        System.out.println(name);

        System.out.println(cellValue.getStringValue());


    }

}
