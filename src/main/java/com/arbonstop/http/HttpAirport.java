package com.arbonstop.http;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
//import com.spire.doc.collections.SortedItemList;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;


/**
 * @ClassName HttpAirport
 * @Description TODO
 * @Author QiBin
 * @Date 2022/3/114:38
 * @Version 1.0
 **/
public class HttpAirport {
    //请求超时时间,这个时间定义了socket读数据的超时时间，也就是连接到服务器之后到从服务器获取响应数据需要等待的时间,发生超时，会抛出SocketTimeoutException异常。
    private static final int SOCKET_TIME_OUT = 60000;
    //连接超时时间,这个时间定义了通过网络与服务器建立连接的超时时间，也就是取得了连接池中的某个连接之后到接通目标url的连接等待时间。发生超时，会抛出ConnectionTimeoutException异常
    private static final int CONNECT_TIME_OUT = 60000;

    public static void main(String[] args) throws IOException {

//        String s = HttpUtil.get("http://airport.anseo.cn/c-china");
//        System.out.println(s);


//        Document doc = null;
//        try {
//            doc = DocumentHelper.parseText(s);
//        } catch (DocumentException e) {
//            e.printStackTrace();
//        }
//        Element root = doc.getRootElement();// 指向根节点
//
//        Iterator it = root.elementIterator();
//        while (it.hasNext()) {
//            Element element = (Element) it.next();// 一个Item节点
//            System.out.println(element.getName() + " : " + element.getTextTrim());
//        }

//        Document doc = Jsoup.parse(s);
//        String digest = doc.text();//提取文本格式内容
//        System.out.println(digest);


        /**
         * userID: Lu
         * unitofMeasureTag: 1
         * triptype: One Way
         * cabinclass: Premium
         * noofpassenger: 1
         * noofArrAirport: 1
         * depCode: PEK
         * arrCode1: HKG
         * arrCode2: #
         * arrCode3: #
         */

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userID", "Lu");
        jsonObject.put("unitofMeasureTag", "1");
        jsonObject.put("triptype", "One Way");
        jsonObject.put("cabinclass", "Premium");
        jsonObject.put("noofpassenger", "1");
        jsonObject.put("noofArrAirport", "1");
        jsonObject.put("depCode", "PEK");
        jsonObject.put("arrCode1", "HKG");
        jsonObject.put("arrCode2", "#");
        jsonObject.put("arrCode3", "#");


        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("userID", "Lu"));
        params.add(new BasicNameValuePair("unitofMeasureTag", "1"));
        params.add(new BasicNameValuePair("triptype", "One Way"));
        params.add(new BasicNameValuePair("cabinclass", "Premium"));
        params.add(new BasicNameValuePair("noofpassenger", "1"));
        params.add(new BasicNameValuePair("depCode", "PEK"));
        params.add(new BasicNameValuePair("arrCode1", "HKG"));
        params.add(new BasicNameValuePair("noofArrAirport", "1"));
        params.add(new BasicNameValuePair("arrCode2", "#"));
        params.add(new BasicNameValuePair("arrCode3", "#"));


        String post = HttpUtil.post("https://applications.icao.int/icec/Home/getCompute", jsonObject);
        System.out.println(post);






//        System.out.println(cookie);


//        CloseableHttpClient httpClient = HttpClients.createDefault();
//
//        HttpPost uploadFile = new HttpPost("https://applications.icao.int/icec/Home/getCompute");
//
//        uploadFile.setHeader("Cookie", "__utmz=1.1646115814.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); __utma=1.1801448367.1646115814.1646115814.1646185847.2; __utmc=1; ASP.NET_SessionId=3wneyq5xz1xtir14v0gemnlk; TS01e182e3=0106b7013648a2cc06f16bc4d09e852b5216ed7a665b972929b0ffee0f08e4359c054e1df1d6973a52e20f612fd09918f7d38ead81030965449da356dfd0a9623176942117; TS6716441c027=08142408a0ab2000f9cf3180724b9143e16debe2ff214f2256c690d0802c25a07b5dcd4e45367b4b083e192dd511300042cccf9b85c99666da4dea3bf0086c5e4b953d249cbc04772031d102c12d7c280f82b0f915a7c20652c812f745f73db9");
//
////        HttpEntity entity = new UrlEncodedFormEntity(createParam(jsonObject), Consts.UTF_8);
//        HttpEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
//
////        RequestConfig reqConfig = RequestConfig.custom().setSocketTimeout(SOCKET_TIME_OUT).setConnectTimeout(CONNECT_TIME_OUT).build();
////        uploadFile.setConfig(reqConfig);
//        uploadFile.setEntity(entity);
//        CloseableHttpResponse response = httpClient.execute(uploadFile);
//        HttpEntity responseEntity = response.getEntity();
//
//
//        String result = EntityUtils.toString(responseEntity, "UTF-8");
//        System.out.println(result);
//
//        Document doc = Jsoup.parse(result);
//        String digest = doc.text();//提取文本格式内容
//        System.out.println(digest);




//        InputStream content = responseEntity.getContent();
//
//        String result = "";
//        BufferedReader in = new BufferedReader(new InputStreamReader(content));
//        String line;
//        while ((line = in.readLine()) != null) {
//            result += line;
//        }
//        if (!StringUtils.isBlank(result)) {
//            System.out.println("上传文件" + "返回参数==>" + result);
//        }


//        String body = HttpRequest.post("https://applications.icao.int/icec/Home/getCompute")
//                .header(Header.COOKIE, "__utmz=1.1646115814.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); __utma=1.1801448367.1646115814.1646115814.1646185847.2; __utmc=1; ASP.NET_SessionId=3wneyq5xz1xtir14v0gemnlk; TS01e182e3=0106b7013648a2cc06f16bc4d09e852b5216ed7a665b972929b0ffee0f08e4359c054e1df1d6973a52e20f612fd09918f7d38ead81030965449da356dfd0a9623176942117; TS6716441c027=08142408a0ab20000e6341f0c88b49af792471865ea2c63574eecb1f50ce773402198043fb05a57f0855974a96113000befe5afaea2c59ef61adb62e42db02d4f0270a6c2fe88a3533e9cceaa6fa7e87e651e8ae43fb5b3cbf97509fc9b9c82a")
//                .body(jsonObject)
//                .timeout(20000)//超时，毫秒
//                .execute().body();


    }

    private static List<NameValuePair> createParam(Map<String, Object> param) {
        //建立一个NameValuePair数组，用于存储欲传送的参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        if (param != null) {
            for (String k : param.keySet()) {
                nvps.add(new BasicNameValuePair(k, param.get(k).toString()));
            }
        }
        return nvps;
    }

}
