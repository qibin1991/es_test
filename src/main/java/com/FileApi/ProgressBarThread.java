package com.FileApi;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @ClassName ProgressBarThread
 * @Description TODO
 * @Author QiBin
 * @Date 2021/7/717:44
 * @Version 1.0
 **/
public class ProgressBarThread implements Runnable {


    private ArrayList<Integer> proList = new ArrayList<Integer>();
    private int progress;//当前进度
    private int totalSize;//下载文法总大小
    private boolean run = true;
    private int showProgress;
    public static HashMap<String, Integer> progressMap = new HashMap<>();//各用户的下载进度
    public static HashMap<String, Boolean> executeStatusMap = new HashMap<>();//各用户是否下载中
    private Long fileId;
    private String token;

    public ProgressBarThread(int totalSize, Long fileId, String token) {
        this.totalSize = totalSize;
        this.fileId = fileId;
        this.token = token;
        //创建进度条时，将指定用户的执行状态改为true
        executeStatusMap.put(token, true);
    }

    /**
     * @param progress 进度
     */
    public void updateProgress(int progress) {
        synchronized (this.proList) {
            if (this.run) {
                this.proList.add(progress);
                this.proList.notify();
            }
        }
    }

    public void finish() {
        this.run = false;
        //关闭进度条
    }

    @Override
    public void run() {
        synchronized (this.proList) {
            try {
                while (this.run) {
                    if (this.proList.size() == 0) {
                        this.proList.wait();
                    }
                    synchronized (proList) {
                        this.progress += this.proList.remove(0);
                        //更新进度条
                        showProgress = (int) ((new BigDecimal((float) this.progress / this.totalSize).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()) * 100);
                        progressMap.put(token + fileId, showProgress);
                        if (showProgress == 100) {
                            //进度100%时将用户的执行状态改为false
                            executeStatusMap.put(token, false);
                        }
                        System.err.println("当前进度：" + showProgress + "%");
                    }
                }
                System.err.println("下载完成");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        try {
            File file = new File("C:/Users/mfy/Desktop/蜜雪冰城.pdf");
            FileInputStream fis = new FileInputStream(file);
            FileOutputStream fos = new FileOutputStream("D:/nms/img/蜜雪冰城.pdf");
            ProgressBarThread pbt = new ProgressBarThread((int) file.length(), 1L, "000001");//创建进度条
            new Thread(pbt).start();//开启线程，刷新进度条
            byte[] buf = new byte[1024];
            int size = 0;
            while ((size = fis.read(buf)) > -1) {//循环读取
                fos.write(buf, 0, size);
                pbt.updateProgress(size);//写完一次，更新进度条
            }
            pbt.finish();//文件读取完成，关闭进度条
            fos.flush();
            fos.close();
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //web socket 获取 下载或上传的进度

//    public HzRestResult getProgress(String fileId, HttpServletRequest request) {
//        String token = request.getHeader("token");
//        Integer progress = ProgressBarThread.progressMap.get(token + fileId);
//        if (progress == null) {
//            return HzRestResult.getFailed("该任务不存在!");
//        }
//        if(progress.equals(100)){
//            //进度100%时清除指定map
//            ProgressBarThread.progressMap.remove(token + fileId);
//        }
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("fileId", fileId);
//        jsonObject.put("token", token);
//        jsonObject.put("progress", progress);
//        return HzRestResult.getSuccess(jsonObject);
//    }


}

