package com.tzj;

import cn.hutool.core.collection.CollUtil;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.RowRenderData;
import com.deepoove.poi.data.Rows;
import com.deepoove.poi.plugin.table.LoopRowTableRenderPolicy;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName ActiveLevelService
 * @Description TODO
 * @Author QiBin
 * @Date 2022/2/2117:11
 * @Version 1.0
 **/

public class ActiveLevelService {

    static String resource = "/Users/qibin/Downloads/燃料燃烧.docx";

    public static void main(String[] args) throws Exception {

        List<RowRenderData> burns = new ArrayList<>();
        List<RowRenderData> processInputs = new ArrayList<>();
        List<RowRenderData> processOutputs = new ArrayList<>();
        List<RowRenderData> consumption = new ArrayList<>();


        List<ActiveEntity> list = new ArrayList<>();
        if (CollUtil.isNotEmpty(list)) {
            for (ActiveEntity activeEntity : list) {
                RowRenderData good = Rows.of("", activeEntity.variety, activeEntity.getConsumeInfo(), activeEntity.getBurnInfo()).center().create();
                switch (activeEntity.getName()) {
                    case "燃料燃烧":
                        burns.add(good);
                        break;
                    case "工业生产过程碳输入":
                        processInputs.add(good);
                        break;
                    case "工业生产过程碳输出":
                        processOutputs.add(good);
                        break;
                    case "碳酸盐的总消费量":
                        consumption.add(good);
                        break;
                    default:
                        break;
                }
            }
        }


        DetailData data = DetailData.builder().consumption(consumption).burns(burns).processInputs(processInputs).processOutputs(processOutputs).build();

        ActivityLevel build = ActivityLevel.builder().hyperpiesis("1111").detailTable(data).build();


        LoopRowTableRenderPolicy hackLoopTableRenderPolicy = new LoopRowTableRenderPolicy();

        ActivityLevelPolicy activityLevelPolicy = new ActivityLevelPolicy();
        //碳酸盐的总消费量
        Configure config = Configure.builder()
                .bind("detailTable", activityLevelPolicy)
                .bind("parameters", hackLoopTableRenderPolicy)
                .bind("responses", hackLoopTableRenderPolicy)
                .bind("properties", hackLoopTableRenderPolicy).useSpringEL().build();
        XWPFTemplate template = XWPFTemplate.compile(resource, config).render(build);
        FileOutputStream out = null;

        out = new FileOutputStream("/Users/qibin/Downloads/out1.docx");
        template.write(out);

        out.flush();
        out.close();
        template.close();
    }


}
