//package com.mybatisPlus;
//
///**
// * @ClassName CodeGeneration
// * @Description TODO
// * @Author QiBin
// * @Date 2022/7/20 15:16
// * @Version 1.0
// **/
//import com.baomidou.mybatisplus.annotation.DbType;
//import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
//import com.baomidou.mybatisplus.core.toolkit.StringUtils;
//import com.baomidou.mybatisplus.generator.AutoGenerator;
//import com.baomidou.mybatisplus.generator.config.*;
//import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
//import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
//
//import java.io.File;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Scanner;
//
//import static com.baomidou.mybatisplus.generator.config.rules.DateType.ONLY_DATE;
//
///**
// * @author zhanglu
// * @ClassName: CodeGeneration
// * @Description: 代码生成器
// * @date 2019年8月14日 下午2:55:14
// */
//public class CodeGeneration {
//    public static String scanner(String tip) {
//        /*
//         * 查询出当前库所有表信息
//         *	SELECT A.tablename, obj_description(relfilenode, 'pg_class') AS comments FROM pg_tables A, pg_class B 				  WHERE A.schemaname='platform' AND A.tablename = B.relname
//         */
//        Scanner scanner = new Scanner(System.in);
//        StringBuilder help = new StringBuilder();
//        help.append("请输入" + tip + "：");
//        System.out.println(help.toString());
//        if (scanner.hasNext()) {
//            String ipt = scanner.next();
//            if (StringUtils.isNotEmpty(ipt)) {
//                return ipt;
//            }
//        }
//        throw new MybatisPlusException("请输入正确的" + tip + "！");
//    }
//
//    /**
//     * @param args
//     * @Title: main
//     * @Description: 生成
//     */
//    public static void main(String[] args) {
//        AutoGenerator mpg = new AutoGenerator();
//
//        // 全局配置
//        GlobalConfig gc = new GlobalConfig();
//        final String projectPath = System.getProperty("user.dir") + "/supervison-api";
//        String filePath = projectPath + "/src/main/java";
//        System.out.println("生成文件 的路径为：" + filePath);
//        gc.setOutputDir(filePath);
////        gc.setOutputDir("E://code");
////        gc.setFileOverride(true);
////        gc.setActiveRecord(false);// 不需要ActiveRecord特性的请改为false
////        gc.setEnableCache(false);// XML 二级缓存
////        gc.setBaseResultMap(true);// XML ResultMap
////        gc.setBaseColumnList(false);// XML columList
//        gc.setOpen(false);
//        gc.setDateType(ONLY_DATE);
//        gc.setAuthor("zhanglu_autoGeneration");// 作者
//
//        // 自定义文件命名，注意 %s 会自动填充表实体属性！
////        gc.setControllerName("%sAction");
////        gc.setServiceName("%sService");
////        gc.setServiceImplName("%sServiceImpl");
////        gc.setMapperName("%sMapper");
////        gc.setXmlName("%sMapper");
//        mpg.setGlobalConfig(gc);
//
//        // 数据源配置
//        DataSourceConfig dsc = new DataSourceConfig();
//        dsc.setDriverName("org.postgresql.Driver");
//        dsc.setUsername("supervison");
//        dsc.setPassword("123");
//        dsc.setDbType(DbType.POSTGRE_SQL);
//        dsc.setUrl("jdbc:postgresql://10.212.170.84:5432/supervison_platform");
//        dsc.setSchemaName("platform");
//        mpg.setDataSource(dsc);
//        // 包配置
//        final PackageConfig pc = new PackageConfig();
////        pc.setModuleName("");
//        pc.setParent("com.chnenergy.monitoring.supervison.api.dao");
//        //以下生成的类类型的map的KEY值，可以去常量类中ConstVal获得，为了省事，直接写了字符串
//        Map m = new HashMap();
//        m.put("entity_path", gc.getOutputDir() + File.separator + (pc.getParent().replaceAll("\\.", "\\" + File.separator)) + "/entity");
//        m.put("mapper_path", gc.getOutputDir() + File.separator + (pc.getParent().replaceAll("\\.", "\\" + File.separator)) + "/mapper");
////        m.put("service_path",gc.getOutputDir() + File.separator + (pc.getParent().replaceAll("\\.", "\\" + File.separator)) +"/service");
////        m.put("service_impl_path",gc.getOutputDir() + File.separator + (pc.getParent().replaceAll("\\.", "\\" + File.separator)) + "/service/impl");
////        m.put(ConstVal.CONTROLLER_PATH,gc.getOutputDir() + File.separator + (pc.getParent().replaceAll("\\.", "\\" + File.separator)) + "controller");
////                m.put(ConstVal.XML_PATH,gc.getOutputDir() + File.separator + (pc.getParent().replaceAll("\\.", "\\" + File.separator)) + "/xml");
//
//        pc.setPathInfo(m);
//        // 自定义配置
//        InjectionConfig cfg = new InjectionConfig() {
//            @Override
//            public void initMap() {
//                // to do nothing
//            }
//        };
//
//
//        cfg.setFileOutConfigList(null);
//        mpg.setCfg(cfg);
//        mpg.setPackageInfo(pc);
//        // 配置模板
//        TemplateConfig templateConfig = new TemplateConfig();
//
//        // 配置自定义输出模板
//        //指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
//        // templateConfig.setEntity("templates/entity2.java");
//        // templateConfig.setService();
//        // templateConfig.setController();
////        templateConfig.setXml(TEMPLATE_XML);
//        mpg.setTemplate(templateConfig);
//        // 策略配置
//        StrategyConfig strategy = new StrategyConfig();
//        strategy.setNaming(NamingStrategy.underline_to_camel);
//        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
////        strategy.setTablePrefix("platform");
//        //strategy.setSuperEntityClass("com.baomidou.ant.common.BaseEntity");
//        strategy.setEntityLombokModel(true);
//        strategy.setRestControllerStyle(true);
//        // 写于父类中的公共字段
//        //strategy.setSuperEntityColumns("id");
//        strategy.setInclude(scanner("表名，多个英文逗号分割").split(","));
////        strategy.setControllerMappingHyphenStyle(true);
////        strategy.setTablePrefix(pc.getModuleName() + "_");
//        mpg.setStrategy(strategy);
//        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
//        // 执行生成
//        mpg.execute();
//
//    }
//
//}
