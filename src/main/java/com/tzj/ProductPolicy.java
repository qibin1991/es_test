package com.tzj;

import cn.hutool.core.collection.CollUtil;

import com.deepoove.poi.data.RowRenderData;
import com.deepoove.poi.policy.DynamicTableRenderPolicy;
import com.deepoove.poi.policy.TableRenderPolicy;
import com.deepoove.poi.util.TableTools;
import org.apache.poi.xwpf.usermodel.*;

import java.util.List;


/**
 * @ClassName ProductPolicy
 * @Description TODO
 * @Author QiBin
 * @Date 2022/2/2218:02
 * @Version 1.0
 **/
public class ProductPolicy extends DynamicTableRenderPolicy {

    // 输入
    int a = 1;
    // 输出
    int b = 2;

    @Override
    public void render(XWPFTable table, Object data) throws Exception {

        System.out.println("==========");
        if (null == data) return;
        //

        ProductProcessData detailData = (ProductProcessData) data;
        List<RowRenderData> consumption = detailData.getCarbonOuts();
        System.out.println("========" + consumption.size());

        if (CollUtil.isNotEmpty(consumption)) {
            table.removeRow(b);
            // 循环插入行
            for (int i = 0; i < consumption.size(); i++) {
                XWPFTableRow insertNewTableRow = table.insertNewTableRow(b);

                for (int j = 0; j < 4; j++) insertNewTableRow.createCell();

                //单行渲染
                TableRenderPolicy.Helper.renderRow(table.getRow(b), consumption.get(i));
            }
            // 合并单元格   同一列单元格合并某几行
            TableTools.mergeCellsVertically(table, 0, b, b + consumption.size() - 1);
//
//            XWPFTableCell cell = table.getRow(b).getCell(0);
//
//            XWPFParagraph paragraphArray = cell.getParagraphArray(0);
//
//            XWPFRun run = paragraphArray.getRuns().get(0);
//
//            run.setText("碳输出", 0);


//            table.getRow(0).getCell(b).getParagraphArray(0).getRuns().get(0).setText("碳输出", 0);
//            String text = table.getRow(7).getCell(0).getText();
//
//            table.getRow(7).getCell(0).setText("");
//            table.getRow(7).getCell(0).setText("测试");

//            XWPFTableCell cell = table.getRow(7).getCell(0);

//            XWPFParagraph paragraphArray = cell.getParagraphArray(0);
//
//            XWPFRun run = paragraphArray.getRuns().get(0);

//            run.setText("测试", 0);

//            run.setText("测试");


//            TableRenderPolicy.Helper.renderRow(table.getRow(d), "碳酸盐的总消费量");
//碳酸盐的总消费量
//            TableTools.mergeCellsVertically(table, 3, 7,7+ consumption.size());

        }

        List<RowRenderData> carbonIns = detailData.getCarbonIns();
        if (CollUtil.isNotEmpty(carbonIns)) {
            table.removeRow(a);
            // 循环插入行
            for (int i = 0; i < carbonIns.size(); i++) {
                XWPFTableRow insertNewTableRow = table.insertNewTableRow(a);

                for (int j = 0; j < 4; j++) insertNewTableRow.createCell();

                //单行渲染
                TableRenderPolicy.Helper.renderRow(table.getRow(a), carbonIns.get(i));
            }
            // 合并单元格   同一列单元格合并某几行
            TableTools.mergeCellsVertically(table, 0, a, a + carbonIns.size() - 1);


        }

        table.getRow(0).getCell(1).getParagraphArray(0).getRuns().get(0).setText("碳输入");
        table.getRow(0).getCell(2).getParagraphArray(0).getRuns().get(0).setText("碳输出");
    }
}
