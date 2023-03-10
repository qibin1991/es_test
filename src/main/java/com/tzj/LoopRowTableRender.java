package com.tzj;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.plugin.table.LoopRowTableRenderPolicy;

import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @ClassName LoopRowTableRender
 * @Description TODO
 * @Author QiBin
 * @Date 2022/2/2216:56
 * @Version 1.0
 **/
public class LoopRowTableRender {
    static String resource = "/Users/qibin/Downloads/二氧化碳排放量报告.docx";

    public static void main(String[] args) throws Exception {
        LoopRow loopRow = LoopRow.builder()
                .carbonContent("33").consumption("22")
                .fuelBand("烟煤").lowerHeating("11").oxidationRate("44")
                .perUnit("55").build();
        List<LoopRow> loopRows = Arrays.asList(loopRow, loopRow, loopRow);


        LoopRowTableRenderPolicy loopRowTableRenderPolicy = new LoopRowTableRenderPolicy();

        Configure config = Configure.builder().bind("loopRow", loopRowTableRenderPolicy).build();
        XWPFTemplate template = XWPFTemplate.compile(resource, config).render(new HashMap(){{put("loopRow",loopRows);}});
        FileOutputStream out = null;

        out = new FileOutputStream("/Users/qibin/Downloads/out2.docx");
        template.write(out);

        out.flush();
        out.close();
        template.close();


    }


}
