package com.arbonstop;

/*
 * @ClassName Connect_MySQL
 * @Description TODO
 * @Author QiBin
 * @Date 2021/12/2516:19
 * @Version 1.0
 **/


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Connect_MySQL {
    //url: jdbc:mysql://localhost:3306/carbon_java?
    // useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8

    private static final String URL="jdbc:mysql://192.168.1.70:3306/carbonstop_reprocessed_plastic?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8";
    private static final String USER="root";
    private static final String PASSWORD="123456";
    private static Connection connection=null;

    static{


        //1、加载驱动程序（反射的方法）
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //2、连接数据库
        try {
            connection=(Connection) DriverManager.
                    getConnection(URL, USER,PASSWORD);//地址，用户名，密码
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static Connection getConnection(){
        return connection;
    }

}