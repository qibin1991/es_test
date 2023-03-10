package com.tzj;

import cn.hutool.core.collection.CollUtil;
import com.deepoove.poi.data.RowRenderData;
import com.deepoove.poi.policy.DynamicTableRenderPolicy;
import com.deepoove.poi.policy.TableRenderPolicy;
import com.deepoove.poi.util.TableTools;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName ActivityLevelPolicy
 * @Description TODO
 * @Author QiBin
 * @Date 2022/2/2116:45
 * @Version 1.0
 **/
@Component
public class ActivityLevelPolicy extends DynamicTableRenderPolicy {

    // 燃料燃烧 填充数据所在行数
    int a = 2;
    // 工业生产过程碳输入 填充数据所在行数
    int b = 4;
    // 工业生产过程碳输出 填充数据所在行数
    int c = 6;
    // 碳酸盐的总消费量 填充数据所在行数
    int d = 8;

    @Override
    public void render(XWPFTable table, Object data) throws Exception {
        System.out.println("==========");
        if (null == data) return;
        //

        DetailData detailData = (DetailData) data;
        List<RowRenderData> consumption = detailData.getConsumption();

        List<RowRenderData> processOutputs = detailData.getProcessOutputs();

        List<RowRenderData> processInputs = detailData.getProcessInputs();

        List<RowRenderData> burns = detailData.getBurns();

        if (CollUtil.isNotEmpty(consumption)){
            table.removeRow(d);
            // 循环插入行
            for (int i = 0; i < consumption.size(); i++) {
                XWPFTableRow insertNewTableRow = table.insertNewTableRow(d);

                for (int j = 0; j < 4; j++) insertNewTableRow.createCell();

                //单行渲染
                TableRenderPolicy.Helper.renderRow(table.getRow(d), consumption.get(i));
            }
            // 合并单元格   同一列单元格合并某几行
            TableTools.mergeCellsVertically(table, 0, 7,7+ consumption.size());
            TableTools.mergeCellsVertically(table, 3, 7,7+ consumption.size());

        }

        if (CollUtil.isNotEmpty(processOutputs)){
            table.removeRow(c);

            for (int i = 0; i < processOutputs.size(); i++) {
                XWPFTableRow insertNewTableRow = table.insertNewTableRow(c);

                for (int j = 0; j < 4; j++) insertNewTableRow.createCell();

                //单行渲染
                TableRenderPolicy.Helper.renderRow(table.getRow(c), processOutputs.get(i));
            }
            // 合并单元格   同一列单元格合并某几行
            TableTools.mergeCellsVertically(table, 0, 5,5+ consumption.size());

            TableTools.mergeCellsVertically(table, 3, 5,5+ consumption.size());
        }

        if (CollUtil.isNotEmpty(processInputs)){
            table.removeRow(b);
            for (int i = 0; i < processInputs.size(); i++) {
                XWPFTableRow insertNewTableRow = table.insertNewTableRow(b);

                for (int j = 0; j < 4; j++) insertNewTableRow.createCell();

                //单行渲染
                TableRenderPolicy.Helper.renderRow(table.getRow(b), processInputs.get(i));
            }
            // 合并单元格   同一列单元格合并某几行
            TableTools.mergeCellsVertically(table, 0, 3,3+ consumption.size());

            TableTools.mergeCellsVertically(table, 3, 3,3+ consumption.size());
        }
        if (CollUtil.isNotEmpty(burns)){
            table.removeRow(a);
            for (int i = 0; i < burns.size(); i++) {
                XWPFTableRow insertNewTableRow = table.insertNewTableRow(a);

                for (int j = 0; j < 4; j++) insertNewTableRow.createCell();

                //单行渲染
                TableRenderPolicy.Helper.renderRow(table.getRow(a), processInputs.get(i));
            }
            // 合并单元格   同一列单元格合并某几行
            TableTools.mergeCellsVertically(table, 0, 1,1+ consumption.size());

            TableTools.mergeCellsVertically(table, 3, 1,1+ consumption.size());

        }

        table.removeRow(0);
    }


}
