//package carbonstop;
//
///**
// * @ClassName TestJacob
// * @Description TODO
// * @Author QiBin
// * @Date 2022/3/110:52
// * @Version 1.0
// **/
///*
// * Copyright © 2019 bjfansr@cn.ibm.com Inc. All rights reserved
// * @package: com.ibm.jacob
// * @version V1.0
// */
//
//
//import com.jacob.com.Dispatch;
//import com.jacob.com.Variant;
//
//
//
//public class TestJacob {
//
//    public static void main(String[] args)  {
//        JacobExcelTool tool = new JacobExcelTool();
//        //打开
//        tool.OpenExcel("/Users/qibin/Downloads/蒸汽转换计算工具.xlsm", false, false);
//        Dispatch sheet = tool.getSheetByName("蒸汽转换");
////        for (int i = 2; i <= 7; i++) {
////            tool.setValue(sheet, "B" + i, "value", i * 1.2);
////        }
//
//        tool.setValue(sheet,"B4","value",100.00);
//        tool.setValue(sheet,"B5","value",1.50);
//        tool.setValue(sheet,"B6","value",100.00);
//
//        //调用Excel宏
////        tool.callMacro("文档特定设置");
//        //调用Excel宏
//        tool.callMacro("Vapor_Exergy");
//        Variant num = tool.getValue("B8", sheet);
//        System.out.println(num);
//        //关闭并保存，释放对象
//        tool.CloseExcel(true, true);
//    }
//}
