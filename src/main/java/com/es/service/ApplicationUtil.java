package com.es.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Properties;
import java.util.UUID;

/**
 * Created by zxs on 2017/6/16.
 */

public class ApplicationUtil {

        private static Logger logger = LoggerFactory.getLogger(ApplicationUtil.class);
        private static HashMap<String,String> propMap = new HashMap<String,String>();
        /**
         * 获取配置文件
         * @param key
         */
        public static String getPropertiesValue(String file,String key) {
            InputStream in =null;
            try {
                String value = null ;
                if(propMap.containsKey(key)){
                    value = propMap.get(key);
                }else{
                    InputStream ips = ApplicationUtil.class.getResourceAsStream("/"+file);
                    BufferedReader ipss = new BufferedReader(new InputStreamReader(ips));
                    Properties prop = new Properties();
                    prop.load(ipss);
                    value = prop.getProperty(key);
                    if(null != value){
                        propMap.put(key, value);
                    }
                }
                return value;
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Utils getPropertiesValue exception:\r\n" + file + ",Key:"+ key + "\r\n" + e.toString());
                return "";
            }finally{
                try {
                    if(null != in){
                        in.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    public static String generateUUID() {
        String s = UUID.randomUUID().toString();
        //去掉“-”符号
        return s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18) + s.substring(19, 23) + s.substring(24);
    }
    }
