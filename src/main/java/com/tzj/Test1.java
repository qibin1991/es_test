package com.tzj;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.RowRenderData;
import com.deepoove.poi.data.Rows;
import com.deepoove.poi.plugin.table.LoopRowTableRenderPolicy;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName Test1
 * @Description TODO
 * @Author QiBin
 * @Date 2022/2/2311:11
 * @Version 1.0
 **/
public class Test1 {

    static String resource = "/Users/qibin/Downloads/productProcess.docx";

    public static void main(String[] args) throws  Exception{
//        String path = Test1.class.getResource("").getPath();
//        System.out.println(path);
        String path = Test1.class.getClassLoader().getResource("/lang.xlsx").getFile();//注意getResource("")里面是空字符串
        System.out.println(path);
        String property = System.getProperty("user.dir");
        System.out.println(property);

        List<CarbonProFac> list = new ArrayList<>();
        CarbonProFac a = CarbonProFac.builder().carbon("碳输入").count("11").level("11").name("11").build();


        CarbonProFac b = CarbonProFac.builder().carbon("碳输出").count("22").level("22").name("22").build();
        list = Arrays.asList(a,a,a,b,b,b);

        List<RowRenderData> goods = new ArrayList<>();
        List<RowRenderData> goods1 = new ArrayList<>();


        for (CarbonProFac carbonProFac : list) {
            if (carbonProFac.getCarbon().equals("碳输入")){
                RowRenderData good = Rows.of("碳输入",carbonProFac.getName(),carbonProFac.getLevel(), carbonProFac.getCount()).center().create();
                goods.add(good);
            }else {
                RowRenderData good = Rows.of("碳输出",carbonProFac.getName(),carbonProFac.getLevel(), carbonProFac.getCount()).center().create();
                goods1.add(good);
            }
        }

        List<MainPro> mainProList =new ArrayList<>();
        MainPro t = MainPro.builder().name("11").unit("t").pro("11").flag("/").build();
        MainPro t1 = MainPro.builder().name("22").unit("t").pro("22").flag("/").build();
        MainPro t2 = MainPro.builder().name("33").unit("t").pro("33").flag("/").build();
        mainProList = Arrays.asList(t,t1,t2);
        List<MainPro> mp = new ArrayList<>();
        for (int i = 0; i < mainProList.size(); i++) {
            MainPro mainPro = mainProList.get(i);
            mainPro.setNum(String.valueOf(i+1));
            mp.add(mainPro);
        }


        ProductProcessData productProcessData = new ProductProcessData();


        productProcessData.setCarbonIns(goods);
        productProcessData.setCarbonOuts(goods1);
        ProductProcess productProcess = new ProductProcess();
        productProcess.setProductProcessData(productProcessData);


        productProcess.setConsumption("111");
        productProcess.setPurity("22");
        productProcess.setEmissionFactor("3");
        productProcess.setYear("2022");
        productProcess.setReportCompany("山西");
        productProcess.setDownloadDateTime("2022年2月23日");

        productProcess.setMainPro(mp);


        List<EightRecFac> eightRecFacs = new ArrayList<>();

        productProcess.setEightRecFac(eightRecFacs);


        ProductPolicy productPolicy = new ProductPolicy();

        LoopRowTableRenderPolicy hackLoopTableRenderPolicy = new LoopRowTableRenderPolicy();

        ActivityLevelPolicy activityLevelPolicy = new ActivityLevelPolicy();


        //碳酸盐的总消费量
        Configure config = Configure.builder()
                .bind("detailTable", activityLevelPolicy)
                .bind("productProcessData", productPolicy)
                .bind("parameters", hackLoopTableRenderPolicy)
                .bind("responses", hackLoopTableRenderPolicy)
                .bind("properties", hackLoopTableRenderPolicy)
                .bind("mainPro", hackLoopTableRenderPolicy)
                .bind("sixProduct", hackLoopTableRenderPolicy)
                .bind("eightRecFac",hackLoopTableRenderPolicy)
                .bind("fourFactor", activityLevelPolicy)
                .useSpringEL().build();
        XWPFTemplate template = XWPFTemplate.compile(resource, config).render(productProcess);
        FileOutputStream out = null;

        out = new FileOutputStream("/Users/qibin/Downloads/out23-1.docx");
        template.write(out);

        out.flush();
        out.close();
        template.close();



    }




}
