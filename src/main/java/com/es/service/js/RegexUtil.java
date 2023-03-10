package com.es.service.js;

/**
 * @ClassName RegexUtil
 * @Description TODO
 * @Author QiBin
 * @Date 2021/1/23下午12:54
 * @Version 1.0
 **/

import cn.hutool.core.text.UnicodeUtil;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName RegexUtil
 * @Description TODO
 * @Author QiBin
 * @Date 2020/9/1611:39
 * @Version 1.0
 **/
@Component
public class RegexUtil {

    /**
     * 正则表达式匹配两个指定字符串中间的内容
     *
     * @param soap
     * @return
     */
    public List<String> getSubUtil(String soap, String rgex) {
        List<String> list = new ArrayList<>();
        // 匹配的模式
        Pattern pattern = Pattern.compile(rgex);
        Matcher m = pattern.matcher(soap);
        while (m.find()) {
            int i = 1;
            list.add(m.group(i));
            i++;
        }
        return list;
    }

    public static void main(String[] args) {
//        String s= "{\"timestamp\": 1611213960, \"crawl_time\": 1611250744, \"post_user\": \"Gene Bunin\", \"share_num\": \"1\", \"post_data\": \"\", \"hashtag_list\": [], \"profilelink_list\": [{\"@text\": \"\", \"@link\": \"https://l.facebook.com/l.php?u=http%3A%2F%2Fshahit.biz%2F%3Ff%3Daliyem-urayim%26fbclid%3DIwAR09o2_0voyfL9n1NItGRF8SzlajB_-bOsMjWzBASvQ3bhoiDNb5j4U-pF4&h=AT0QxRixBgYq4AVhKZH7-Bpj30OKfpbqTAwgO6T0Ezei_-U5SX5kndZzHRU2SreDMYJxMWJH9NH0Ui5Pccjq1OBGpBH-NfwGVAqqNPtY-10MMElvcvz_qGAPwAjGGfWsQ50&__tn__=-UK-y-R&c[0]=AT1rFuyNojon2WBAQu9yOe3gkaFylSzzS7uSyvTeugZ3WqFmLGuzHc6NvZGgdbLRO5u0lSKg0hjj-AwFbaHJCiORX7JAKHat9bEISVkJ9cyTcam2ky1sBGF3FTEBWRhRYnuQ2XTLfpgOU7cuPefbWkT7YcsTb7PAKI0VWGM0SU7yGE4-zzPNhZZMtyle1_MFAfJENnk\"}, {\"@text\": \"\", \"@link\": \"https://www.facebook.com/hashtag/uyghurpulse?__eep__=6&__cft__[0]=AZVrMhDK3M0pbigew8pXC4QBFuMang2WA58IoEDHp48Y9FXUydErZm1Y8bjFPrG3J2oBUBX44Qu1061GwZ9j5UJtzRPWc26hi_-Z4WaDZ1_77hppVrJ5JcqEte3p9JI-499YQT9V5qFeqjhgF4NA8hoES8MesvT0JfeZ7F-tmYMqeQ&__tn__=*NK-y-R\"}], \"post_video\": \"https://www.facebook.com/101141098079050/videos/1041606086316895/?__cft__[0]=AZVrMhDK3M0pbigew8pXC4QBFuMang2WA58IoEDHp48Y9FXUydErZm1Y8bjFPrG3J2oBUBX44Qu1061GwZ9j5UJtzRPWc26hi_-Z4WaDZ1_77hppVrJ5JcqEte3p9JI-499YQT9V5qFeqjhgF4NA8hoES8MesvT0JfeZ7F-tmYMqeQ&__tn__=%2B%3FFH-y-R\", \"post_pic\": \"\", \"post_link\": \"https://www.facebook.com/gene.bunin/posts/2961080107457162?__cft__[0]=AZVrMhDK3M0pbigew8pXC4QBFuMang2WA58IoEDHp48Y9FXUydErZm1Y8bjFPrG3J2oBUBX44Qu1061GwZ9j5UJtzRPWc26hi_-Z4WaDZ1_77hppVrJ5JcqEte3p9JI-499YQT9V5qFeqjhgF4NA8hoES8MesvT0JfeZ7F-tmYMqeQ&__tn__=%2CO%2CP-R\", \"post_id\": \"2961080107457162\", \"is_quote\": true, \"quote_fullname\": \"Uyghur Pulse\", \"quote_href\": \"https://www.facebook.com/Uyghur-Pulse-101141098079050/?__cft__[0]=AZVrMhDK3M0pbigew8pXC4QBFuMang2WA58IoEDHp48Y9FXUydErZm1Y8bjFPrG3J2oBUBX44Qu1061GwZ9j5UJtzRPWc26hi_-Z4WaDZ1_77hppVrJ5JcqEte3p9JI-499YQT9V5qFeqjhgF4NA8hoES8MesvT0JfeZ7F-tmYMqeQ&__tn__=-UC%2CP-y-R\", \"quote_timestamp\": 1611142380, \"quote_text\": \"-- Beat 1083: Eli, Norway (\\\\u0626\\\\u06c7\\\\u064a\\\\u063a\\\\u06c7\\\\u0631\\\\u0686\\\\u06d5) --Eli testifies for his mother, Aliyem Urayim (shahit.biz/?f=aliyem-urayim), a businesswoman from Ghulja who was allegedly sentenced to 17 years and 10 months in prison after initially being detained in early 2017, following a trip to Turkey. #uyghurpulse\", \"quote_hashtag_list\": [{\"#text\": \"\", \"#link\": \"https://www.facebook.com/hashtag/uyghurpulse?__eep__=6&__cft__[0]=AZVrMhDK3M0pbigew8pXC4QBFuMang2WA58IoEDHp48Y9FXUydErZm1Y8bjFPrG3J2oBUBX44Qu1061GwZ9j5UJtzRPWc26hi_-Z4WaDZ1_77hppVrJ5JcqEte3p9JI-499YQT9V5qFeqjhgF4NA8hoES8MesvT0JfeZ7F-tmYMqeQ&__tn__=*NK-y-R\"}], \"quote_profilelink_list\": [{\"@text\": \"\", \"@link\": \"https://l.facebook.com/l.php?u=http%3A%2F%2Fshahit.biz%2F%3Ff%3Daliyem-urayim%26fbclid%3DIwAR09o2_0voyfL9n1NItGRF8SzlajB_-bOsMjWzBASvQ3bhoiDNb5j4U-pF4&h=AT0QxRixBgYq4AVhKZH7-Bpj30OKfpbqTAwgO6T0Ezei_-U5SX5kndZzHRU2SreDMYJxMWJH9NH0Ui5Pccjq1OBGpBH-NfwGVAqqNPtY-10MMElvcvz_qGAPwAjGGfWsQ50&__tn__=-UK-y-R&c[0]=AT1rFuyNojon2WBAQu9yOe3gkaFylSzzS7uSyvTeugZ3WqFmLGuzHc6NvZGgdbLRO5u0lSKg0hjj-AwFbaHJCiORX7JAKHat9bEISVkJ9cyTcam2ky1sBGF3FTEBWRhRYnuQ2XTLfpgOU7cuPefbWkT7YcsTb7PAKI0VWGM0SU7yGE4-zzPNhZZMtyle1_MFAfJENnk\"}], \"greater_list\": [{\"greater_name\": \"Martyna Kokotkiewicz\", \"greater_href\": \"https://www.facebook.com/martyna.kokotkiewicz?__cft__[0]=AZVrMhDK3M0pbigew8pXC4QBFuMang2WA58IoEDHp48Y9FXUydErZm1Y8bjFPrG3J2oBUBX44Qu1061GwZ9j5UJtzRPWc26hi_-Z4WaDZ1_77hppVrJ5JcqEte3p9JI-499YQT9V5qFeqjhgF4NA8hoES8MesvT0JfeZ7F-tmYMqeQ&__tn__=-a]-R\", \"greater_pic\": \"https://scontent-lax3-1.xx.fbcdn.net/v/t1.0-1/cp0/p40x40/139058689_10224875444931147_9150201259006616532_n.jpg?_nc_cat=103&ccb=2&_nc_sid=7206a8&_nc_ohc=zfYF5YE0tiUAX-LGQ9W&_nc_ht=scontent-lax3-1.xx&tp=27&oh=ff2993b350e167755be663ac36844872&oe=60304B96\"}, {\"greater_name\": \"\\\\u0421\\\\u0430\\\\u043d\\\\u0430\\\\u0442 \\\\u0421\\\\u04d9\\\\u0431\\\\u0438\\\\u0442\\\\u043e\\\\u0432\", \"greater_href\": \"https://www.facebook.com/profile.php?id=100054688698545&__cft__[0]=AZVrMhDK3M0pbigew8pXC4QBFuMang2WA58IoEDHp48Y9FXUydErZm1Y8bjFPrG3J2oBUBX44Qu1061GwZ9j5UJtzRPWc26hi_-Z4WaDZ1_77hppVrJ5JcqEte3p9JI-499YQT9V5qFeqjhgF4NA8hoES8MesvT0JfeZ7F-tmYMqeQ&__tn__=-a]-R\", \"greater_pic\": \"https://scontent-lax3-2.xx.fbcdn.net/v/t1.0-1/cp0/p40x40/118635725_111941960638760_7724956268125592761_n.jpg?_nc_cat=106&ccb=2&_nc_sid=7206a8&_nc_ohc=M3yDOFA4MJoAX-84WMg&_nc_ht=scontent-lax3-2.xx&tp=27&oh=a5e29e2b5c706fd98421c0827fd01b45&oe=602E14BE\"}, {\"greater_name\": \"Mohammed Uyghur Scientist\", \"greater_href\": \"https://www.facebook.com/uyghur.scientist?__cft__[0]=AZVrMhDK3M0pbigew8pXC4QBFuMang2WA58IoEDHp48Y9FXUydErZm1Y8bjFPrG3J2oBUBX44Qu1061GwZ9j5UJtzRPWc26hi_-Z4WaDZ1_77hppVrJ5JcqEte3p9JI-499YQT9V5qFeqjhgF4NA8hoES8MesvT0JfeZ7F-tmYMqeQ&__tn__=-a]-R\", \"greater_pic\": \"https://scontent-lax3-1.xx.fbcdn.net/v/t1.0-1/cp0/p40x40/134057177_867359604101109_983646612347734516_n.jpg?_nc_cat=103&ccb=2&_nc_sid=7206a8&_nc_ohc=0EBvEBaERe0AX_wBQCN&_nc_ht=scontent-lax3-1.xx&tp=27&oh=e7aac85df9b642d4cf06d0077fc05ef0&oe=602DBB2B\"}, {\"greater_name\": \"\\\\u0410\\\\u043b\\\\u0442\\\\u044b\\\\u043d\\\\u0430\\\\u0439 \\\\u0410\\\\u0440\\\\u0430\\\\u0441\\\\u0430\\\\u043d\", \"greater_href\": \"https://www.facebook.com/profile.php?id=100042025004507&__cft__[0]=AZVrMhDK3M0pbigew8pXC4QBFuMang2WA58IoEDHp48Y9FXUydErZm1Y8bjFPrG3J2oBUBX44Qu1061GwZ9j5UJtzRPWc26hi_-Z4WaDZ1_77hppVrJ5JcqEte3p9JI-499YQT9V5qFeqjhgF4NA8hoES8MesvT0JfeZ7F-tmYMqeQ&__tn__=-a]-R\", \"greater_pic\": \"https://scontent-lax3-2.xx.fbcdn.net/v/t1.0-1/cp0/p40x40/138289692_432498934827603_3244401742465075867_o.jpg?_nc_cat=111&ccb=2&_nc_sid=7206a8&_nc_ohc=O7d26n3eEL4AX9ZJd6l&_nc_ht=scontent-lax3-2.xx&tp=27&oh=08ff8c954bdaf9b44d64d58481295082&oe=602D8871\"}], \"great_num\": 4, \"comment_num\": 0, \"comment_list\": []}";
String s= "Seyit T\\u00fcmt\\u00fcrk";
        String s1 = UnicodeUtil.toString(s);
        String s2 = StringEscapeUtils.unescapeJava(s1);
        System.out.println(s2);
    }

    public static String double2Date(Double d) {
        String t = "";

        try {
            Calendar base = Calendar.getInstance();
            SimpleDateFormat outFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //Delphi的日期类型从1899-12-30开始

            base.set(1899, 11, 30, 0, 0, 0);
            base.add(Calendar.DATE, d.intValue());
            base.add(Calendar.MILLISECOND, (int) ((d % 1) * 24 * 60 * 60 * 1000));


            t = outFormat.format(base.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return t;

    }
}

