package com.arbonstop;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @ClassName FileToDb
 * @Description TODO
 * @Author QiBin
 * @Date 2021/12/2416:42
 * @Version 1.0
 **/
public class FileToDb {


    public static void main(String[] args) {

        File file = new File("");
        ExcelReader reader = ExcelUtil.getReader(file.getAbsolutePath());
        List<Map<String, Object>> maps = reader.readAll();



    }

}
