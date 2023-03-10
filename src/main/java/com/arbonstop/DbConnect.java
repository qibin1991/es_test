package com.arbonstop;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName DbConnect
 * @Description TODO
 * @Author QiBin
 * @Date 2021/12/2516:11
 * @Version 1.0
 **/
public class DbConnect implements Cloneable {


    private static Connection connection = Connect_MySQL.getConnection();


    /**
     * 支持批量插入数据
     *
     * @param
     */

    public static void addNewsPaper(List<Map<String, Object>> listNewsPaper) {//增
        String sql = "insert into emissionfactor_carbonemissionfactor (only_id, year, emission_factor, emission_factor_name, emission_factor_unit," +
                "extra_information,institution_short,country) " +
                "values(?, ?, ?, ?, ?,?,?,?)";
        java.sql.PreparedStatement ptmt = null;

        try {
            connection.setAutoCommit(false);// 关闭事务
            ptmt = connection.prepareStatement(sql);

        } catch (SQLException e2) {
            e2.printStackTrace();
        }

        for (Map<String, Object> paperaper : listNewsPaper) {

            try {
                ptmt.setString(1, String.valueOf(paperaper.get("only_id")));
                ptmt.setString(2, String.valueOf(paperaper.get("year")));
                ptmt.setString(3, String.valueOf(paperaper.get("emission_factor")));
                ptmt.setString(4, String.valueOf(paperaper.get("emission_factor_name")));
                ptmt.setString(5, String.valueOf(paperaper.get("emission_factor_unit")));
                ptmt.setString(6, String.valueOf(paperaper.get("extra_information")));
                ptmt.setString(7, String.valueOf(paperaper.get("institution_short")));
                ptmt.setString(8, String.valueOf(paperaper.get("country")));
                ptmt.addBatch();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        try {
            ptmt.executeBatch();//执行给定的SQL语句，该语句可能返回多个结果
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {


        int count = 0;
        int sizenum = 1000;

        File file = new File("/Users/qibin/Downloads/排放因子表.xlsx");
        ExcelReader reader = ExcelUtil.getReader(file.getAbsolutePath());
        List<Map<String, Object>> maps = reader.readAll();

        List<Map<String, Object>> listPaper = new ArrayList<>();
        for (Map<String, Object> string : maps) {


            count++;
            listPaper.add(string);  //
            if (count % sizenum == 0) {
                //System.out.println("ok");
                System.out.println("  " + count);
                addNewsPaper(listPaper); //插入数据库

                System.out.println(count);
                listPaper.clear();
            }

        }
        if (count % sizenum != 0) {
            addNewsPaper(listPaper);
            System.out.println("zui hou ");
        }
    }
}

