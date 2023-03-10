package com.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @ClassName: DateUntil
 * @Description:
 * @auther GH
 * @date 2019/12/4 14:26
 */


public class DateUntil {


    public static Boolean checkDate(String date) {

        Boolean flag = false;

        if(date.length()==10){
            flag = true;
        }
        return flag;
    }

    public static Date StringToDate(String args) {

        //创建SimpleDateFormat对象实例并定义好转换格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // System.out.println("把当前时间转换成字符串：" + sdf.format(new Date()));

        Date date = null;
        try {
            // 注意格式需要与上面一致，不然会出现异常
            date = sdf.parse(args);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println("字符串转换成时间:" + date);
        return  date;
    }


      public static String getNowDate() {

        //创建SimpleDateFormat对象实例并定义好转换格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // System.out.println("把当前时间转换成字符串：" + sdf.format(new Date()));

        String date = null;
        try {
            // 注意格式需要与上面一致，不然会出现异常
            date = sdf.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("字符串转换成时间:" + date);
        return  date;
    }
    /**
     * @Description: 年月日时分秒转换为时间的毫秒值格式
     * @param : 年月日时分秒
     * @throws ParseException
     */
    @SuppressWarnings("null")
    public static long dateMillisTime(String time) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (null != time) {
            return sdf.parse(time).getTime();
        } else {
            return 0;
        }
    }

    public static String getFormatDate(Date date, String format) {
        DateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }

    public static String getFormatDate(String  time, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        if (null != time) {
            return sdf.parse(time).toString();
        } else {
            return "";
        }
    }
    public static void main(String[] args) {
        System.out.println(
               checkDate("2019-01-11")
                );


    }

    /**
     * 昨天0点的时间戳
     */
    public static void getYestoday0(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)-1,0,0,0);
        long tt = calendar.getTime().getTime();
    }

    /**
     * 昨天 11.59.59时间戳
     */
    public static void getYsetoday11(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)-1,23,59,59);
        long tt = calendar.getTime().getTime();
    }

    /**
     * 计算今天0点的时间戳
     *
     */
    public static void getToday0(){
        Long  time = System.currentTimeMillis();  //当前时间的时间戳
        long zero = time/(1000*3600*24)*(1000*3600*24) - TimeZone.getDefault().getRawOffset();
        System.out.println(new Timestamp(zero));//今天零点零分零秒
        System.out.println(zero/1000);

    }
    /**
     * 计算今天11点59时间戳
     */
    public static void getToday11(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),23,59,59);
        long tt = calendar.getTime().getTime()/1000;
        System.out.println(tt);

    }

    /**
     * 1529576852650		//当前毫秒数
     * 66452				//当前时间  距离当天凌晨  秒数
     * 1529510400000		//当天凌晨毫秒数
     * 2018-06-21 00:00:00	//当天凌晨日期
     * @throws ParseException
     */
    public void sss() throws ParseException{
        long now = System.currentTimeMillis();
        SimpleDateFormat sdfOne = new SimpleDateFormat("yyyy-MM-dd");
        long overTime = (now - (sdfOne.parse(sdfOne.format(now)).getTime()))/1000;
        //当前毫秒数
        System.out.println(now);
        //当前时间  距离当天凌晨  秒数
        System.out.println(overTime);
        //当天凌晨毫秒数
        System.out.println(sdfOne.parse(sdfOne.format(now)).getTime());
        //当天凌晨日期
        SimpleDateFormat sdfTwo = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.print(sdfTwo.format(sdfOne.parse(sdfOne.format(now)).getTime()));
    }

}