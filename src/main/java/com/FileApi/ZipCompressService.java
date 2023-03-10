//package FileApi;
//
///**
// * @ClassName ZipCompressService
// * @Description TODO
// * @Author QiBin
// * @Date 2021/5/216:27
// * @Version 1.0
// **/
//
//
//import java.io.*;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.nio.charset.Charset;
//import java.util.Map;
//import java.util.UUID;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import com.alibaba.fastjson.JSONObject;
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpHead;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.ContentType;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClientBuilder;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.util.EntityUtils;
//import org.apache.tools.zip.ZipEntry;
//import org.apache.tools.zip.ZipOutputStream;
//
//
//import org.springframework.util.StringUtils;
///**
// *
// * 文件压缩成zip
// *
// */
//public class ZipCompressService {
//
//    /**
//     *
//     * 将文件夹及文件夹包含的内容压缩成zip文件
//     * (为了解决中文乱码的问题，ZipOutputStream用org.apache.tools.zip.*)
//     *
//     * @param inputFile 源文件
//     * @param delFlag 删除源文件标记
//     * @return File 压缩后的文件
//     */
//    public static File zipCompress(File inputFile, boolean delFlag) throws Exception{
//        File zipFile = null;
//        //创建zip输出流
//        //为了解决中文乱码的问题,ZipOutputStream用org.apache.tools.zip.*
//        //不要用 java.util.zip.*
//        ZipOutputStream zos;
//        zos = null;
//        if(inputFile != null && inputFile.exists()) {
//            try {
//                String path = inputFile.getCanonicalPath();
//                String zipFileName = path + ".zip";
//                zipFile = new File(zipFileName);
//                if(zipFile.exists()) {
//                    zipFile.delete();
//                }
//                zipFile.createNewFile();//创建文件
//                zos = new ZipOutputStream(new FileOutputStream(zipFile));
//                //解决中文乱码问题,指定编码GBK
//                zos.setEncoding("GBK");
//                //压缩文件或文件夹
//                compressFile(zos, inputFile, inputFile.getName());
//            }catch(Exception e) {
//                System.out.println("文件压缩异常：" + e);
//                throw e;
//            }finally {
//                try {
//                    if(zos != null) {
//                        //先调用outputStream的flush()再关闭流；
//                        //流如果未正常关闭,则会在解压的时候出现压缩文件损坏的现象
//                        zos.flush();
//                        zos.close();
//                    }
//
//                    if(delFlag) {
//                        //递归删除源文件及源文件夹
//                        deleteFile(inputFile);
//                    }
//                }catch(Exception ex) {
//                    System.out.println("输出流关闭异常：" + ex);
//                }
//            }
//        }
//        return zipFile;
//    }
//
//    /**
//     * 压缩文件或文件夹
//     * (ZipEntry 使用org.apache.tools.zip.*，不要用 java.util.zip.*)
//     *
//     * @param zos zip输出流
//     * @param sourceFile 源文件
//     * @param baseName 父路径
//     * @throws Exception 异常
//     */
//    private static void compressFile(ZipOutputStream zos, File sourceFile, String baseName) throws Exception{
//        if(!sourceFile.exists()) {
//            return;
//        }
//
//        //若路径为目录（文件夹）
//        if(sourceFile.isDirectory()) {
//            //取出文件夹中的文件（或子文件夹）
//            File[] fileList = sourceFile.listFiles();
//            //若文件夹为空，则创建一个目录进入点
//            if(fileList.length == 0) {
//                //文件名称后跟File.separator表示这是一个文件夹
//                zos.putNextEntry(new ZipEntry(baseName + File.separator));
//                //若文件夹非空，则递归调用compressFile,对文件夹中的每个文件或每个文件夹进行压缩
//            }else {
//                for(int i = 0; i < fileList.length; i++) {
//                    compressFile(zos, fileList[i],
//                            baseName + File.separator + fileList[i].getName());
//                }
//            }
//
//            //若为文件,则先创建目录进入点,再将文件写入zip文件中
//        }else {
//            ZipEntry ze = new ZipEntry(baseName);
//            //设置ZipEntry的最后修改时间为源文件的最后修改时间
//            ze.setTime(sourceFile.lastModified());
//            zos.putNextEntry(ze);
//
//            FileInputStream fis = new FileInputStream(sourceFile);
//            copyStream(fis, zos);
//            try {
//                if(fis != null) {
//                    fis.close();
//                }
//            }catch(Exception e) {
//                System.out.println("输入流关闭异常：" + e);
//            }
//        }
//    }
//
//    /**
//     * 流拷贝
//     *
//     * @param in 输入流
//     * @param out 输出流
//     * @throws IOException
//     */
//    private static void copyStream(InputStream in, OutputStream out) throws IOException{
//        int bufferLength = 1024 * 100;
//        synchronized(in) {
//            synchronized(out) {
//                int count = 0;
//                byte[] buffer = new byte[bufferLength];
//                while((count = in.read(buffer, 0, bufferLength)) != -1) {
//                    out.write(buffer, 0, count);
//                }
//                out.flush();
//            }
//        }
//    }
//
//    /**
//     * 递归删除文件夹中的目录及文件
//     *
//     * @param sourceFile
//     * @throws Exception
//     */
//    private static void deleteFile(File sourceFile) throws Exception{
//        //如果路径为目录
//        if(sourceFile.isDirectory()) {
//            //取出文件夹中的文件或子文件夹
//            File[] fList = sourceFile.listFiles();
//            if(fList.length == 0) {
//                sourceFile.delete();
//            }else {
//                for(int i = 0; i < fList.length; i++) {
//                    deleteFile(fList[i]);
//                }
//                sourceFile.delete();
//            }
//            //如果为文件则直接删除
//        }else {
//            sourceFile.delete();
//        }
//    }
//
//    /**
//     *     当前路径下若已有同名文件又不愿意覆盖，
//     *     则依次追加后缀
//     *
//     * @param path
//     * @param docName
//     * @return
//     * @throws Exception
//     */
//    private File createFile(String path, final String docName) throws Exception{
//        //创建目标文件
//        File destFile = new File(path, docName);
//        //如果路径下存在同名文件又不愿意覆盖，
//        //那么则依次给文件加后缀（2）、（3）……
//        if(destFile.exists()) {
//            int i = 1;
//            do {
//                ++i;
//                //按“.”分割
//                String[] doc = docName.split("\\.");
//                destFile = new File(path, doc[0] + "(" + i + ")" + "." + doc[1]);
//
//                //直到文件创建成功则跳出循环
//            }while(!destFile.createNewFile());
//        }else {
//            destFile.createNewFile();
//        }
//        return destFile;
//    }
//
//    /**
//     * 过滤非法字符
//     * @param inputStr 输入字符串
//     * @return
//     */
//    private static String filterIllegalSymbol(String inputStr) {
//        if(StringUtils.isEmpty(inputStr)) {
//            return null;
//        }
//        String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
//        Pattern pattern = Pattern.compile(regEx);
//        Matcher matcher = pattern.matcher(inputStr);
//        //若包含非法字符则循环处理
//        if(matcher.find()) {
//            char[] regExCharArray = regEx.toCharArray();
//            for(int i = 0; i < regExCharArray.length; i++) {
//                char c = regExCharArray[i];
//                System.out.println(i + " 当前字符：" + c);
//                if(inputStr.indexOf(c) != -1) {
//                    System.out.println("过滤前：" + inputStr);
//                    //因为某些符号在正则表达示中有相应意义，所以要加上“\\”表示转义，
//                    //否则会报错，例如java.util.regex.PatternSyntaxException: Dangling meta character '*' near index 0
//                    inputStr = inputStr.replaceAll("\\\\" + String.valueOf(c), "");
//                    System.out.println("过滤后：" +inputStr);
//                }
//            }
//        }
//        //测试用
//        System.out.println(inputStr);
//        return inputStr;
//
//    }
//
//    public static void main(String[] args) {
//        ZipCompressService service = new ZipCompressService();
//
//        try {
//            File file = zipCompress(new File("/Users/qibin/zyyt相关/zyyt_analysis"), false);
//            System.out.println("========");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
////        service.filterIllegalSymbol("这是一个*包含非/法字:符$%<的字符串>|");
//    }
//
//    public static String postFile(File file, String url, int no, int total, String baseName) throws ClientProtocolException, IOException {
//        FileBody bin = null;
//
////        HttpClient httpclient = new DefaultHttpClient();
//        HttpClient client = HttpClientBuilder.create().build();
//        HttpPost httppost = new HttpPost(url);
//
//        if (file != null) {
//            bin = new FileBody(file);
//        }
//        /**
//         * "srcLangId":"1",
//         * "tgtLangId":"2",
//         * “fileName”:”filename”,
//         * “automatic”:false,
//         * “priority”:1,
//         * “weight”:1
//         * “area”:1000
//         */
//
////        StringBody base = new StringBody(baseName);
//
//
////请记住，这边传递汉字会出现乱码，解决方法如下,设置好编码格式就好
//
////new StringBody("汉字",Charset.forName("UTF-8")));
//
////        MultipartEntity reqEntity = new MultipartEntity();
//
//
////        reqEntity.addPart("fileName", base);
//
////        reqEntity.addPart("data", bin);
//
//
////        if (total != 0) {
////            reqEntity.addPart("fileBatchNO", new StringBody(total + ""));
////            reqEntity.addPart("fileBatchTotal", new StringBody(no + ""));
////        }
//
//
//        String boundary = "--------------------" + UUID.randomUUID().toString();
//
////        httppost.setEntity(reqEntity);
//        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//        //字符编码
//        builder.setCharset(Charset.forName("UTF-8"));
//        //模拟浏览器
//        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
//        //设置边界
//        builder.setBoundary(boundary);
//        //设置multipart/form-data 流文件
//        builder.addPart("multipartFile", new FileBody(file));
//        if (total != 0) {
//            builder.addPart("fileBatchNO", new StringBody(total + ""));
//            builder.addPart("fileBatchTotal", new StringBody(no + ""));
//        }
//
//        HttpHead header = new HttpHead();
//        header.addHeader("", "");
//        header.addHeader("", "");
//        //application/octet-stream 代表不知道是什么格式的文件
//        builder.addBinaryBody("name", file, ContentType.create("application/octet-stream"), baseName);
//        HttpEntity entity = builder.build();
//        httppost.setEntity(entity);
//        HttpResponse response = client.execute(httppost);
//
//        HttpEntity resEntity = response.getEntity();
//
//
//        if (resEntity != null) {
//
//
//        }
//
//        if (resEntity != null) {
//            resEntity.consumeContent();
//
//        }
//
//        return null;
//
//    }
//
////    public static void main(String[] args) throws ClientProtocolException, IOException {
////        File file = new File("d:/rss.xml");
////
////        String url = "http://localhost:8080/webtest/servlet/URLTest";
////
////
////    }
//
////    public String postData(File file,String url){
////        MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
////        bodyMap.add("file", new FileSystemResource(file));
////        HttpHeaders headers = new HttpHeaders();
////        headers.add("accept", "*/*");
////        headers.add("connection", "Keep-Alive");
////        headers.add("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
////        headers.add("Accept-Charset", "utf-8");
////        headers.add("Content-Type", "application/json; charset=utf-8");
////        headers.add("token", token);
////        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
////        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyMap, headers);
////        RestTemplate restTemplate = new RestTemplate();
////        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
////        String body = response.getBody();
////        return body;
////    }
//
//
//
//    public void postFile1(File file) throws IOException {
//        DataInputStream in = null;
//
//        OutputStream out = null;
//
//        HttpURLConnection conn = null;
//
//        JSONObject resposeTxt = null;
//
//        InputStream ins = null;
//
//        ByteArrayOutputStream outStream = null;
//
//        try {
//// URL url = new URL("http://192.168.3.11:8081/mes-boot-doc/test/fileupload?fileName=shafei.xls");
//            String path = "";
//
//            URL url = new URL(path);
//
//            conn = (HttpURLConnection) url.openConnection();
//
//// 发送POST请求必须设置如下两行
//
//            conn.setDoOutput(true);
//
//            conn.setUseCaches(false);
//
//            conn.setRequestMethod("POST");
//
//            conn.setRequestProperty("Content-Type", "text/html");
//
//            conn.setRequestProperty("Cache-Control", "no-cache");
//
//            conn.setRequestProperty("Charsert", "UTF-8");
//
//            conn.connect();
//
//            conn.setConnectTimeout(10000);
//
//            out = conn.getOutputStream();
//
//            in = new DataInputStream(new FileInputStream(file));
//
//            int bytes = 0;
//
//            byte[] buffer = new byte[1024];
//
//            while ((bytes = in.read(buffer)) != -1) {
//                out.write(buffer, 0, bytes);
//
//            }
//
//            out.flush();
//
//// 返回流
//
//            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                ins = conn.getInputStream();
//
//                outStream = new ByteArrayOutputStream();
//
//                byte[] data = new byte[1024];
//
//                int count = -1;
//
//                while ((count = ins.read(data, 0, 1024)) != -1) {
//                    outStream.write(data, 0, count);
//
//                }
//
//                data = null;
//
//                resposeTxt = JSONObject.parseObject(new String(outStream
//
//                        .toByteArray(), "UTF-8"));
//                String message = resposeTxt.getString("message");
//                if ("SUCCESS".equals(message)) {
//
//                }
//
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//
//        } finally {
//            if (in != null) {
//                in.close();
//
//            }
//
//            if (out != null) {
//                out.close();
//
//            }
//
//            if (ins != null) {
//                ins.close();
//
//            }
//
//            if (outStream != null) {
//                outStream.close();
//
//            }
//
//            if (conn != null) {
//                conn.disconnect();
//
//            }
//
//        }
//
//    }
//
//    /**
//     * 使用httpclint 发送文件
//     *
//     * @param file 上传的文件
//     * @return 响应结果
//     * @author: qb
//     * @date:
//     */
//    public static String uploadFile(String url, File file, String fileParamName, Map<String, String> headerParams, Map<String, String> otherParams) {
//        CloseableHttpClient httpClient = HttpClients.createDefault();
//        String result = "";
//        try {
////            String fileName = file.getOriginalFilename();
//            HttpPost httpPost = new HttpPost(url);
//            //添加header
//            for (Map.Entry<String, String> e : headerParams.entrySet()) {
//                httpPost.addHeader(e.getKey(), e.getValue());
//            }
//
//            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//            builder.setCharset(Charset.forName("utf-8"));
//            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);//加上此行代码解决返回中文乱码问题
//            builder.addBinaryBody(fileParamName, file, ContentType.MULTIPART_FORM_DATA, fileParamName);// 文件流
//            for (Map.Entry<String, String> e : otherParams.entrySet()) {
//                builder.addTextBody(e.getKey(), e.getValue());// 类似浏览器表单提交，对应input的name和value
//            }
//            HttpEntity entity = builder.build();
//            httpPost.setEntity(entity);
//            HttpResponse response = httpClient.execute(httpPost);// 执行提交
//            HttpEntity responseEntity = response.getEntity();
//            if (responseEntity != null) {
//                // 将响应内容转换为字符串
//                result = EntityUtils.toString(responseEntity, Charset.forName("UTF-8"));
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                httpClient.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return result;
//    }
//
//
//
//}
//
//
//
//
//
//
