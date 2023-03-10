package com.tzj;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.RowRenderData;
import com.deepoove.poi.data.Rows;
import com.deepoove.poi.plugin.table.LoopRowTableRenderPolicy;

import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName ProductProcessService
 * @Description TODO
 * @Author QiBin
 * @Date 2022/2/2217:46
 * @Version 1.0
 **/
public class ProductProcessService {
    static String resource = "/Users/qibin/Downloads/productProcess.docx";

    public static void main(String[] args) throws Exception{

        RowRenderData good = Rows.of("碳输入","3","墙纸", "33").center().create();
        List<RowRenderData> goods = Arrays.asList(good, good, good);

        RowRenderData good1 = Rows.of("碳输出","4","墙纸", "44").center().create();
        List<RowRenderData> goods1 = Arrays.asList(good1, good1, good1);

        ProductProcessData productProcessData = new ProductProcessData();

        productProcessData.setCarbonIns(goods);
        productProcessData.setCarbonOuts(goods1);

        ProductProcess productProcess = new ProductProcess();
        productProcess.setProductProcessData(productProcessData);


//        MergeCellRule build = MergeCellRule.builder().map(MergeCellRule.Grid.of(1, 0), MergeCellRule.Grid.of(1, 1)).build();

//        put("table3", Tables.of(row0, row1).mergeRule(rule).create());

        ProductPolicy productPolicy = new ProductPolicy();

        LoopRowTableRenderPolicy hackLoopTableRenderPolicy = new LoopRowTableRenderPolicy();
        //碳酸盐的总消费量
        Configure config = Configure.builder()
                .bind("productProcessData", productPolicy)
                .bind("parameters", hackLoopTableRenderPolicy)
                .bind("responses", hackLoopTableRenderPolicy)
                .bind("properties", hackLoopTableRenderPolicy).useSpringEL().build();
        XWPFTemplate template = XWPFTemplate.compile(resource, config).render(productProcess);
        FileOutputStream out = null;

        out = new FileOutputStream("/Users/qibin/Downloads/out3.docx");
        template.write(out);

        out.flush();
        out.close();
        template.close();

    }


}
