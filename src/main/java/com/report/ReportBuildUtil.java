//package com.report;
//
//import cn.hutool.core.collection.CollUtil;
//import cn.hutool.http.HttpUtil;
//import cn.hutool.json.JSONObject;
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.carbonstop.common.core.utils.StringUtils;
//import com.carbonstop.footprint.dao.AnalysisMapper;
//import com.carbonstop.footprint.dto.DischargeGroupByModel;
//import com.carbonstop.footprint.dto.ProcessModelDischargeByModel;
//import com.carbonstop.footprint.entity.*;
//import com.carbonstop.footprint.service.*;
//import com.carbonstop.footprint.utils.report.entity.*;
//import com.deepoove.poi.XWPFTemplate;
//import com.deepoove.poi.config.Configure;
//import com.deepoove.poi.data.PictureRenderData;
//import com.deepoove.poi.data.Pictures;
//import com.deepoove.poi.plugin.table.LoopRowTableRenderPolicy;
//import com.spire.doc.Document;
//import com.spire.doc.FileFormat;
//import lombok.SneakyThrows;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.poi.openxml4j.opc.OPCPackage;
//import org.apache.poi.xwpf.usermodel.XWPFDocument;
//import org.apache.poi.xwpf.usermodel.XWPFParagraph;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.OutputStream;
//import java.lang.reflect.Field;
//import java.lang.reflect.Method;
//import java.math.BigDecimal;
//import java.math.RoundingMode;
//import java.text.SimpleDateFormat;
//import java.util.*;
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.Executor;
//import java.util.stream.Collectors;
//
//import static java.util.stream.Collectors.groupingBy;
//
///**
// * @ClassName ReportBuildUtil
// * @Description TODO
// * @Author QiBin
// * @Date 2023/2/1 17:36
// * @Version 1.0
// **/
//@Component
//@Slf4j
//public class ReportBuildUtil {
//
//    @Value("${file.path}")
//    String localPath;
//
//    @Resource
//    ReportService reportService;
//
//    @Resource
//    ProductionMaterialsService productionMaterialsService;
//
//    @Resource
//    ProductionService productionService;
//    @Resource
//    AnalysisMapper analysisMapper;
//    @Resource
//    MinioService minioService;
//    @Resource
//    ProductionBusinessService productionBusinessService;
//    @Resource
//    ProcessModelService processModelService;
//    @Resource(name = "executor")
//    Executor executor;
//    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
//    //月份
//    static SimpleDateFormat sdfY = new SimpleDateFormat("yyyy年MM月");
//
//
//    /**
//     *
//     *   The report template file path.
//     */
//    @Value("${file.reportTemplate}")
//    String reportTemplate;
//
//    @SneakyThrows
//    public FileData createDoc(Long repotId) {
//
//        try {
//            ReportData reportData = new ReportData();
//            //报告
//            Report report = reportService.getById(repotId);
//            reportData.setReportMonth(sdfY.format(report.getCreateTime()));
//            Long productionId = report.getProductionId();
//            //报告说明
//            String msg = report.getMsg();
//            reportData.setReportMsg(msg);
//            //产品核算
//            ProductionBusiness productionBusiness = productionBusinessService.getById(report.getPbId());
//            //工艺流程
//            String technologicalPic = productionBusiness.getTechnological();
//            String technological = "";
//            if (StringUtils.isNotBlank(technologicalPic)) {
//                technological = "本报告的系统边界/工艺流程是:\n";
//            }
//
////            PictureRenderData pictureRenderData = Pictures.ofUrl(technologicalPic)
////                    .size(100, 100).create();
//            if (StringUtils.isNotBlank(technologicalPic)){
//
//                List<Map<String, PictureRenderData>> list = addPicture(Arrays.asList(technologicalPic.split(",")));
//                reportData.setTechnologicalPic(list);
//            }else {
//                Map<String,Object> map=new HashMap<>();
//                map.put("picture",map);
//                reportData.setTechnologicalPic(map);
//            }
//
//            reportData.setTechnological(technological);
////            reportData.setTechnologicalPic(technologicalPic);
//            String businessType = BusinessTypeEnums.get(productionBusiness.getType()).getName();
//            reportData.setBusinessType(businessType);
//            String checkUnit = productionBusiness.getCheckUnit();
//            reportData.setCheckUnit(checkUnit);
//
//            String businessBeginAndEnd = sdf.format(productionBusiness.getBeginDate()) + "-" + sdf.format(productionBusiness.getEndTime());
//            reportData.setBusinessBeginAndEnd(businessBeginAndEnd);
//            reportData.setCompanyName(productionBusiness.getCompanyName());
//            Production byId = productionService.getById(productionId);
//            if (byId == null) {
//                return null;
//            }
//            String productionName = byId.getProductionName();
//            reportData.setProductionName(productionName);
//            String productionCode = byId.getProductionCode();
//            reportData.setProductionCode(productionCode);
//            //生命周期阶段
//            List<ProcessModel> processModels = processModelService.list(new QueryWrapper<ProcessModel>().le("type", productionBusiness.getType()).eq("parent_id", 0).orderByAsc("model_id"));
//
//            StringBuffer models = new StringBuffer();
//            if (CollUtil.isNotEmpty(processModels))
//                processModels.forEach(a->models.append(a.getModelName()).append("、"));
//            reportData.setModels(models.substring(0,models.length()-1));
//
//
//
//            //反射  获取私有方法
//            Class<? extends ReportData> aClass = reportData.getClass();
//            Field[] declaredFields = aClass.getDeclaredFields();
//            List<ProductionMaterials> productionMaterials = productionMaterialsService.list(new QueryWrapper<ProductionMaterials>().eq("pb_id", productionBusiness.getPbId()));
//
//            //排放源
//            CompletableFuture<Void> materialCom = CompletableFuture.runAsync(() -> {
//                Map<Long, List<ProductionMaterials>> collect = productionMaterials.stream().collect(groupingBy(ProductionMaterials::getModelId));
//
//                Set<Map.Entry<Long, List<ProductionMaterials>>> entries = collect.entrySet();
//
//                for (Map.Entry<Long, List<ProductionMaterials>> entry : entries) {
//                    //modelId
//                    Long key = entry.getKey();
//                    //排放源
//                    List<ProductionMaterials> value = entry.getValue();
//
//                    List<?> list;
//                    //7和10  是运输
//                    if (key != 7L && key != 10L)
//                        list = CopyUtils.copyEnumList(value, Materials.class);
//                    else list = CopyUtils.copyEnumList(value, MaterialDistances.class);
//
////                    Double allWeight = value.stream().map(ProductionMaterials::getWeight).collect(Collectors.toList()).stream()
////                            .map(BigDecimal::new).reduce((m, n) -> m.add(n).setScale(6, RoundingMode.HALF_UP)).map(BigDecimal::doubleValue)
////                            .orElse(0d);
//                    Double allDischarge = value.stream().map(ProductionMaterials::getDischarge).collect(Collectors.toList()).stream()
//                            .map(BigDecimal::new).reduce((m, n) -> m.add(n).setScale(6, RoundingMode.HALF_UP)).map(BigDecimal::doubleValue)
//                            .orElse(0d);
//
//                    //            List<String> strings = Arrays.asList("setModel", "setAllWeight", "setAllDischarge");
//                    Arrays.stream(declaredFields).forEach(p -> {
//                                String name = p.getName();
//                                String setMethodName = "set" + name.substring(0, 1).toUpperCase(Locale.ROOT) + name.substring(1);
//                                Method method = null;
//                                if (setMethodName.equals("setModel" + key)) {
//                                    try {
//                                        method = aClass.getMethod(setMethodName, p.getType());
//                                        method.setAccessible(true);
//                                        method.invoke(reportData, list);
//                                    } catch (Exception e) {
//                                        throw new RuntimeException(e);
//                                    }
//                                }
//                                //                            reportData = setPrivateValue(name, reportData, aClass, list);   需要使用原子 AtomicReference
//
//                                if (setMethodName.equals("setAllDischarge" + key)) {
//                                    try {
//                                        method = aClass.getMethod(setMethodName, p.getType());
//                                        method.invoke(reportData, String.valueOf(allDischarge));
//                                    } catch (Exception e) {
//                                        throw new RuntimeException(e);
//                                    }
//                                }
//                                //                            reportData = setPrivateValue(name, reportData, aClass, allDischarge);
//                            }
//                    );
//                }
//
//            }, executor);
//
//
//            //查询 总的碳足迹
//            CompletableFuture<Void> allDischargeCom = CompletableFuture.runAsync(() -> {
//
//                JSONObject analysisResult = analysisMapper.getAnalysisResult(productionBusiness.getPbId());
//                Double allDischarge = analysisResult.getDouble("discharge");
//                if (analysisResult != null) reportData.setAllDischarge(allDischarge);
//
//                List<ProcessModelDischargeByModel> processModelDischargeByModels = orderByModelDischarge(productionBusiness.getPbId(), allDischarge);
//
//                //查询生命周期阶段占比,分割
//                if (CollUtil.isNotEmpty(processModelDischargeByModels)) {
//                    StringBuffer percent = new StringBuffer();
//                    processModelDischargeByModels.forEach(a -> {
//                        Double discharge = a.getDischarge();
//
//                        double v = BigDecimal.valueOf(discharge == null ? 0 : discharge).divide(BigDecimal.valueOf(allDischarge), 4, RoundingMode.HALF_UP)
//                                .multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().doubleValue();
//                        percent.append(v + "%、");
//                    });
//                    reportData.setModelPercent(percent.substring(0, percent.length() - 2));
//
//                    //查询 生命周期阶段倒序
//                    processModelDischargeByModels.sort(Comparator.comparing(ProcessModelDischargeByModel::getDischarge).reversed());
//
//                    for (int i = 0; i < processModelDischargeByModels.size(); i++) {
//                        int finalI = i;
//
//                        Arrays.stream(declaredFields).forEach(p -> {
//                                    String name = p.getName();
//                                    String setMethodName = "set" + name.substring(0, 1).toUpperCase(Locale.ROOT) + name.substring(1);
//
//                                    Method method = null;
//                                    if (setMethodName.equals("setTop" + (finalI + 1) + "ModelName")) {
//                                        try {
//                                            method = aClass.getMethod(setMethodName, p.getType());
//                                            method.invoke(reportData, processModelDischargeByModels.get(finalI).getModelName());
//                                        } catch (Exception e) {
//                                            throw new RuntimeException(e);
//                                        }
//                                    }
//                                    if (setMethodName.equals("setTop" + (finalI + 1) + "Percent")) {
//                                        Double discharge = processModelDischargeByModels.get(finalI).getDischarge();
//                                        double v = BigDecimal.valueOf(discharge == null ? 0 : discharge).divide(BigDecimal.valueOf(allDischarge), 4, RoundingMode.HALF_UP)
//                                                .multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().doubleValue();
//                                        try {
//                                            method = aClass.getMethod(setMethodName, p.getType());
//                                            method.invoke(reportData, v + "%");
//                                        } catch (Exception e) {
//                                            throw new RuntimeException(e);
//                                        }
//                                    }
//                                }
//                        );
//                    }
//                }
//            }, executor);
//            CompletableFuture.runAsync(()->{
//                if (CollUtil.isNotEmpty(processModels)){
//                    List<Factor> factors = CopyUtils.copyEnumList(productionMaterials, Factor.class);
//                    for (int i = 0; i < factors.size(); i++) {
//                        Factor factor = factors.get(i);
//                        factor.setNumber(i+1);
//                    }
//                    reportData.setFactor1(factors);
//                }
//            }, executor);
//            CompletableFuture.allOf(materialCom, allDischargeCom).get();
//            LoopRowTableRenderPolicy hackLoopTableRenderPolicy = new LoopRowTableRenderPolicy();
//            //从模版到指定文件
//            Configure config = Configure.builder()
//                    .bind("model6", hackLoopTableRenderPolicy)
//                    .bind("model7", hackLoopTableRenderPolicy)
//                    .bind("model8", hackLoopTableRenderPolicy)
//                    .bind("model9", hackLoopTableRenderPolicy)
//                    .bind("model10", hackLoopTableRenderPolicy)
//                    .bind("model11", hackLoopTableRenderPolicy)
//                    .bind("model12", hackLoopTableRenderPolicy)
//                    .bind("model13", hackLoopTableRenderPolicy)
//                    .bind("factor1", hackLoopTableRenderPolicy)
//                    .useSpringEL().build();
//            XWPFTemplate template = XWPFTemplate.compile(reportTemplate, config).render(reportData);
//
//            //输出路径
//            String destAbsPath = localPath + productionName + "-" + System.currentTimeMillis() + ".docx";
//
//
//            template.write(new FileOutputStream(destAbsPath));
//            template.close();
//
//            restWord(destAbsPath);
//
//            File file = new File(destAbsPath);
//            //上传到minio
//            FileData fileData = minioService.uploadOneFile1(file);
////            file.delete();
//
//            return fileData;
//        } catch (Exception e) {
////            throw new RuntimeException(e);
//            log.info("导出异常,{}", e);
//            return null;
//        }
//    }
//
//    public static void main(String[] args) {
//        byte[] bytes = HttpUtil.downloadBytes("https://minio-test.carbonstop.net/dct-dev/1676009252953-%E7%A2%B3%E6%95%B0%E6%8D%AE%E7%AE%A1%E7%90%86.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=Uzj1Mj45K2rFMVoP%2F20230210%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20230210T060733Z&X-Amz-Expires=604800&X-Amz-SignedHeaders=host&X-Amz-Signature=c3ca9551b51152503c837a9fb59d373afaceb9f2d52a7bb4098f2d2d390a839d");
//
//        System.out.println(bytes.length);
//    }
//
//    /**
//     * 图片的添加
//     */
//    public List<Map<String, PictureRenderData>> addPicture(List<String> imageUrlList){
//        List<Map<String, PictureRenderData>> list=new ArrayList<>();
//        int width = 547;
//        int height = 452;
//        for (String imageUrl : imageUrlList) {
//            if (StringUtils.isNotBlank(imageUrl)){
//                Map<String,PictureRenderData> map=new HashMap<>();
//                log.info("========imageurl:"+imageUrl);
//                map.put("picture",Pictures.ofUrl(imageUrl).size(width, height).create());
//                list.add(map);
//            }
//        }
//        return list;
//    }
//
//    private static void restWord(String docFilePath) {
//
//        Document doc1 = new Document(docFilePath);
//
//        doc1.updateTableOfContents();
//
//        doc1.saveToFile(docFilePath, FileFormat.Docx_2010);
//
//        try (FileInputStream in = new FileInputStream(docFilePath)) {
//            XWPFDocument doc = new XWPFDocument(OPCPackage.open(in));
//            List<XWPFParagraph> paragraphs = doc.getParagraphs();
//            if (paragraphs.size() < 1) return;
//            XWPFParagraph firstParagraph = paragraphs.get(0);
//            if (firstParagraph.getText().contains("Spire.Doc")) {
//                doc.removeBodyElement(doc.getPosOfParagraph(firstParagraph));
//            }
//            OutputStream out = new FileOutputStream(docFilePath);
//            doc.write(out);
//            out.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    /**
//     * 调用传入的对象的set方法
//     *
//     * @param fieldName   属性名
//     * @param obj         对象实例(XXEntity)
//     * @param objectClass 类名(XXEntity.getClass())
//     * @param param       设置的属性的值
//     * @return
//     */
//
//    public static void setFieldValueByName(String fieldName, Object obj, Class objectClass, Object param) {
//        try {
//            Class[] parameterTypes = new Class[1];
//            Field field = objectClass.getDeclaredField(fieldName);
//            parameterTypes[0] = field.getType();
//            String firstLetter = fieldName.substring(0, 1).toUpperCase();
//            String getter = "set" + firstLetter + fieldName.substring(1);
//            Method method = objectClass.getMethod(getter, parameterTypes);
//            method.setAccessible(true);
//            method.invoke(obj, param);
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace(); //
//        } catch (Exception e) {
//            e.printStackTrace(); //
//        }
//    }
//
//
//    public ReportData setPrivateValue(String name, ReportData reportData, Class<? extends ReportData> tClass, Object value) {
//        try {
//            Method method = tClass.getMethod(name);
//            method.invoke(reportData, value);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        return reportData;
//    }
//
//
//    //查询生命周期 阶段的量和占比
//    public List<ProcessModelDischargeByModel> orderByModelDischarge(Long pbId, Double aDouble) {
//        //先查询生命周期过程模型  顶级
//        List<ProcessModelDischargeByModel> processModels = analysisMapper.selectParentModel();
//
//        //查询 全部的排放源
//        List<DischargeGroupByModel> dischargeGroupByModels = analysisMapper.selectDischargeGroupByModel(pbId);
//
//        Map<Long, List<DischargeGroupByModel>> collect = dischargeGroupByModels.stream().collect(Collectors.groupingBy(DischargeGroupByModel::getParentId));
//
//        if (aDouble != null) {
//            processModels.forEach(a -> {
//                Long modelId = a.getModelId();
//                List<DischargeGroupByModel> productionMaterials1 = collect.get(modelId);
//                if (CollUtil.isNotEmpty(productionMaterials1)) {
//                    DischargeGroupByModel dischargeGroupByModel = productionMaterials1.get(0);
//                    Double discharge = dischargeGroupByModel.getDischarge();
//                    if (discharge != null)
//                        a.setPercentDischarge(BigDecimal.valueOf(discharge).divide(BigDecimal.valueOf(aDouble), 4, RoundingMode.HALF_UP).stripTrailingZeros().doubleValue());
//                    a.setDischarge(discharge);
//                } else {
//                    a.setDischarge(0d);
//                    a.setPercentDischarge(0d);
//                }
//            });
//        }
//        return processModels;
//    }
//
//
//
//
//
//
//    //
//    //        if (collect.containsKey(6L)) {
////            JSONObject jsonObject = getMaterialsLoopTables(collect, 6L);
////            reportData.setModel6(JSONUtil.toList(jsonObject.getJSONArray("data"), Materials.class));
////            reportData.setAllWeight6(jsonObject.getStr("allWeight"));
////            reportData.setAllDischarge6(jsonObject.getStr("allDischarge"));
////        }
////        if (collect.containsKey(7L)) {
////            JSONObject jsonObject = getMaterialsLoopTables(collect, 7L);
////            reportData.setModel7(JSONUtil.toList(jsonObject.getJSONArray("data"), MaterialDistances.class));
////            reportData.setAllWeight7(jsonObject.getStr("allWeight"));
////            reportData.setAllDischarge7(jsonObject.getStr("allDischarge"));
////        }
////        if (collect.containsKey(8L)) {
////            JSONObject jsonObject = getMaterialsLoopTables(collect, 8L);
////            reportData.setModel8(JSONUtil.toList(jsonObject.getJSONArray("data"), Materials.class));
////            reportData.setAllWeight8(jsonObject.getStr("allWeight"));
////            reportData.setAllDischarge8(jsonObject.getStr("allDischarge"));
////        }
////        if (collect.containsKey(9L)) {
////            JSONObject jsonObject = getMaterialsLoopTables(collect, 9L);
////            reportData.setModel9(JSONUtil.toList(jsonObject.getJSONArray("data"), Materials.class));
////            reportData.setAllWeight9(jsonObject.getStr("allWeight"));
////            reportData.setAllDischarge9(jsonObject.getStr("allDischarge"));
////        }
////        if (collect.containsKey(10L)) {
////            JSONObject jsonObject = getMaterialsLoopTables(collect, 10L);
////            reportData.setModel10(JSONUtil.toList(jsonObject.getJSONArray("data"), MaterialDistances.class));
////            reportData.setAllWeight10(jsonObject.getStr("allWeight"));
////            reportData.setAllDischarge10(jsonObject.getStr("allDischarge"));
////        }
////        if (collect.containsKey(11L)) {
////            JSONObject jsonObject = getMaterialsLoopTables(collect, 11L);
////            reportData.setModel11(JSONUtil.toList(jsonObject.getJSONArray("data"), Materials.class));
////            reportData.setAllWeight11(jsonObject.getStr("allWeight"));
////            reportData.setAllDischarge11(jsonObject.getStr("allDischarge"));
////        }
////
////        if (collect.containsKey(4L)) {
////            JSONObject jsonObject = getMaterialsLoopTables(collect, 4L);
////            reportData.setModel4(JSONUtil.toList(jsonObject.getJSONArray("data"), Materials.class));
////            reportData.setAllWeight4(jsonObject.getStr("allWeight"));
////            reportData.setAllDischarge4(jsonObject.getStr("allDischarge"));
////        }
////
////        if (collect.containsKey(5L)) {
////            JSONObject jsonObject = getMaterialsLoopTables(collect, 5L);
////            reportData.setModel5(JSONUtil.toList(jsonObject.getJSONArray("data"), Materials.class));
////            reportData.setAllWeight5(jsonObject.getStr("allWeight"));
////            reportData.setAllDischarge5(jsonObject.getStr("allDischarge"));
////        }
//
////    //设置model table中的值
////    public JSONObject getMaterialsLoopTables(Map<Long, List<ProductionMaterials>> collect, Long modelId) {
////        JSONObject jsonObject = new JSONObject();
////        List<ProductionMaterials> c6 = collect.get(modelId);
////        List<?> list;
////        if (modelId != 7L && modelId != 10L)
////            list = CopyUtil.copyList(c6, Materials.class);
////        else list = CopyUtil.copyList(c6, MaterialDistances.class);
////        jsonObject.putOpt("data", list);
////        Double allWeight = c6.stream().map(ProductionMaterials::getWeight).collect(Collectors.toList()).stream()
////                .map(BigDecimal::new).reduce((m, n) -> m.add(n).setScale(6, RoundingMode.HALF_UP)).map(BigDecimal::doubleValue)
////                .orElse(0d);
////        Double allDischarge = c6.stream().map(ProductionMaterials::getDischarge).collect(Collectors.toList()).stream()
////                .map(BigDecimal::new).reduce((m, n) -> m.add(n).setScale(6, RoundingMode.HALF_UP)).map(BigDecimal::doubleValue)
////                .orElse(0d);
////
////        jsonObject.putOpt("allWeight", String.valueOf(allWeight));
////        jsonObject.putOpt("allDischarge", String.valueOf(allDischarge));
////        return jsonObject;
////    }
//}
