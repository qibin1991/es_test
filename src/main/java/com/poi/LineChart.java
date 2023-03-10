package com.poi;

/**
 * @ClassName LineChart
 * @Description TODO
 * @Author QiBin
 * @Date 2022/8/10 16:45
 * @Version 1.0
 **/

import org.apache.poi.util.Units;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xwpf.usermodel.XWPFChart;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import java.io.FileOutputStream;


/**
 * @version 1.0.0
 * @Description: poi生成折线图
 * @Date: 2021/12/25 18:14
 * @Copyright (C) ZhuYouBin
 */
public class LineChart {

    public static void main(String[] args) throws Exception {
        // 1、创建word文档对象
        XWPFDocument document = new XWPFDocument();
        // 2、创建chart图表对象,抛出异常
        XWPFChart chart = document.createChart(15 * Units.EMU_PER_CENTIMETER, 10 * Units.EMU_PER_CENTIMETER);
        // 3、图表相关设置
        chart.setTitleText("使用POI创建的折线图"); // 图表标题
        chart.setTitleOverlay(false); // 图例是否覆盖标题

        // 4、图例设置
        XDDFChartLegend legend = chart.getOrAddLegend();
        legend.setPosition(LegendPosition.TOP); // 图例位置:上下左右

        // 5、X轴(分类轴)相关设置
        XDDFCategoryAxis xAxis = chart.createCategoryAxis(AxisPosition.BOTTOM); // 创建X轴,并且指定位置
        xAxis.setTitle("日期（年月）"); // x轴标题
        String[] xAxisData = new String[] {
                "2021-01","2021-02","2021-03","2021-04","2021-05","2021-06",
                "2021-07","2021-08","2021-09","2021-10","2021-11","2021-12",
        };
        XDDFCategoryDataSource xAxisSource = XDDFDataSourcesFactory.fromArray(xAxisData); // 设置X轴数据

        // 6、Y轴(值轴)相关设置
        XDDFValueAxis yAxis = chart.createValueAxis(AxisPosition.LEFT); // 创建Y轴,指定位置
        yAxis.setTitle("粉丝数（个）"); // Y轴标题
        Integer[] yAxisData = new Integer[]{
                10, 35, 21, 46, 79, 88,
                39, 102, 71, 28, 99, 57
        };
        XDDFNumericalDataSource<Integer> yAxisSource = XDDFDataSourcesFactory.fromArray(yAxisData); // 设置Y轴数据

        // 7、创建折线图对象
        XDDFLineChartData lineChart = (XDDFLineChartData) chart.createData(ChartTypes.LINE, xAxis, yAxis);

        // 8、加载折线图数据集
        XDDFLineChartData.Series lineSeries = (XDDFLineChartData.Series) lineChart.addSeries(xAxisSource, yAxisSource);
        lineSeries.setTitle("粉丝数", null); // 图例标题
        lineSeries.setSmooth(true); // 线条样式:true平滑曲线,false折线
        lineSeries.setMarkerSize((short) 6); // 标记点大小
        lineSeries.setMarkerStyle(MarkerStyle.CIRCLE); // 标记点样式

        // 9、绘制折线图
        chart.plot(lineChart);

        // 10、输出到word文档
        FileOutputStream fos = new FileOutputStream("/Users/qibin/Downloads/lineChart.docx");
        document.write(fos); // 导出word

        // 11、关闭流
        fos.close();
        document.close();
    }

}
