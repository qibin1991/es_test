package com.es.service.thinkTank;

import java.util.Date;

/**
 * @ClassName QdWenYu
 * @Description TODO
 * @Author QiBin
 * @Date 2021/8/2717:47
 * @Version 1.0
 **/
public class QdWenYu {

    public static void main(String[] args) {
        String s = new Date().toString();
        System.out.println(s);


        String s1 = "Опубликовано в выпуске № 16 (879) за 4 мая 2021 года";
        String s2 = s1.substring(s1.indexOf("за") + 3, s1.indexOf("года"));
        System.out.println(s2);
        String[] s3 = s2.split(" ");
        System.out.println(s3.length);

        String s4 = "6 февраля 2021, 10:56";
        System.out.println(s4.contains("."));

        String s5 = "06:05 28.09.2021";
        String[] split = s5.split("\\.");
        System.out.println(split[0]);

        System.out.println("");
    }


}
