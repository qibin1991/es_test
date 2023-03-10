//package FileApi.alan;
//
//
//import org.apache.poi.ss.usermodel.Workbook;
//import org.junit.jupiter.api.Test;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
///**
// * @ClassName AlanPoiTest
// * @Description TODO
// * @Author QiBin
// * @Date 2022/6/21 19:13
// * @Version 1.0
// **/
//public class AlanPoiTest {
//
//
//    public static void main(String[] args) {
//        List<ExportVO> list = new ArrayList<>();
//        for (int i = 0; i < 500; i++) {
//            ExportVO exportVO = new ExportVO();
//            exportVO.setName("name" + i);
////            exportVO.setValue(new BigDecimal(123.11 + i * 0.09));
//            exportVO.setAmount(new BigDecimal(6666.666 + i * 10));
////            exportVO.setDate(new Date(132324343 + i * 100));
//            exportVO.setDateTime(new java.util.Date());
//            list.add(exportVO);
//        }
//        List<String> colList = new ArrayList<>();
////按照顺序仅导出add的列
//        colList.add("name");
//        colList.add("value");
////调用获取workbook对象；也可以直接调用exportSpecifyCol方法导出到浏览器
////        Workbook workbook = ExcelExportUtil.getWorkbookSpecifyCol(list, ExportVO.class, colList);
//
//
//    }
//
//
//
//    @Test
//    public void test (){
//
//    }
//
//}
