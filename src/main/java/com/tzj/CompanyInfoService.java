package com.tzj;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.plugin.table.LoopRowTableRenderPolicy;

import java.io.FileOutputStream;

/**
 * @ClassName CompanyInfoService
 * @Description TODO
 * @Author QiBin
 * @Date 2022/2/2114:41
 * @Version 1.0
 **/
public class CompanyInfoService {

    static String resource = "/Users/qibin/Downloads/单位名称.docx";




    public static void main(String[] args) throws Exception {
        CompanyInfo companyInfo  = CompanyInfo.builder().companyId("911406003954822857").companyName("山西中煤平朔能源化工有限公司").comAdress("朔州市平鲁区北坪循环经济工业园区")
                .comFlag("资子公司，属化工企业，主要生产、经营液氨、LNG、硝酸铵等化工产品，2017年12月底平朔能化公司进行考核验收工作，2018年正式按生产模式进行经营。包")
                .reAdress("山西中煤平朔能源化工有限公司").capital("5000").linkedName("赵峰").linkedNum("0349-2202002")
                .classify("化工").email("ddmwxm@163.com").industryId("化工（行业代码：2621），属于核算指南中的“化工企业")
                .nature("有限责任公司").postCode("036800").registryTime("2014年8月20日").userName("杜冬梅").userPhone("13403496033").build();
        testPaymentExample(companyInfo);

    }

    public static void testPaymentExample(CompanyInfo datas) throws Exception {
//        Configure config = Configure.builder().bind("detail_table", new DetailTablePolicy()).build();
        LoopRowTableRenderPolicy hackLoopTableRenderPolicy = new LoopRowTableRenderPolicy();
        Configure config = Configure.builder().bind("parameters", hackLoopTableRenderPolicy)
                .bind("responses", hackLoopTableRenderPolicy)
                .bind("properties", hackLoopTableRenderPolicy)
                .useSpringEL().build();

//        Configure config = Configure.newBuilder().useSpringEL(true).build();

        XWPFTemplate template = XWPFTemplate.compile(resource, config).render(datas);
        FileOutputStream out = null;

        out = new FileOutputStream("/Users/qibin/Downloads/out.docx");
        template.write(out);

        out.flush();
        out.close();
        template.close();
    }

}
