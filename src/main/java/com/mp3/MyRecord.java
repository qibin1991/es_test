package com.mp3;

/**
 * @ClassName MyRecord
 * @Description TODO
 * @Author QiBin
 * @Date 2021/5/714:45
 * @Version 1.0
 **/
/*

 * 实现录音机的功能

 */


import javax.activation.MimetypesFileTypeMap;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class MyRecord extends JFrame implements ActionListener {

//定义录音格式

    AudioFormat af = null;

//定义目标数据行,可以从中读取音频数据,该 TargetDataLine 接口提供从目标数据行的缓冲区读取所捕获数据的方法。

    TargetDataLine td = null;

//定义源数据行,源数据行是可以写入数据的数据行。它充当其混频器的源。应用程序将音频字节写入源数据行，这样可处理字节缓冲并将它们传递给混频器。

    SourceDataLine sd = null;

//定义字节数组输入输出流

    ByteArrayInputStream bais = null;

    ByteArrayOutputStream baos = null;

//定义音频输入流

    AudioInputStream ais = null;

//定义停止录音的标志，来控制录音线程的运行

    Boolean stopflag = false;

//记录开始录音的时间

    long startPlay;

//定义所需要的组件

    JPanel jp1, jp2, jp3;

    JLabel jl1 = null;

    JButton captureBtn, stopBtn, playBtn, saveBtn, fileBtn;

    public static void main(String[] args) {

//创造一个实例

        MyRecord mr = new MyRecord();

    }

//构造函数

    public MyRecord() {

//组件初始化

        jp1 = new JPanel();

        jp2 = new JPanel();

        jp3 = new JPanel();

//定义字体

        Font myFont = new Font("华文新魏", Font.BOLD, 30);

        jl1 = new JLabel("实时识别功能的实现");

        jl1.setFont(myFont);

        jp1.add(jl1);

        captureBtn = new JButton("开始录音");

//对开始录音按钮进行注册监听

        captureBtn.addActionListener(this);

        captureBtn.setActionCommand("captureBtn");

//对停止录音进行注册监听

        stopBtn = new JButton("停止录音");

        stopBtn.addActionListener(this);

        stopBtn.setActionCommand("stopBtn");

//对播放录音进行注册监听

        playBtn = new JButton("播放录音");

        playBtn.addActionListener(this);

        playBtn.setActionCommand("playBtn");

//对保存录音进行注册监听

        saveBtn = new JButton("保存录音");

        saveBtn.addActionListener(this);

        saveBtn.setActionCommand("saveBtn");

        fileBtn = new JButton("文件上传");
        fileBtn.addActionListener(this);

        fileBtn.setActionCommand("fileBtn");


        this.add(jp1, BorderLayout.NORTH);

        this.add(jp2, BorderLayout.CENTER);

        this.add(jp3, BorderLayout.SOUTH);

        jp3.setLayout(null);

        jp3.setLayout(new GridLayout(1, 5, 20, 20));

        jp3.add(captureBtn);

        jp3.add(stopBtn);

        jp3.add(playBtn);

        jp3.add(saveBtn);

        jp3.add(fileBtn);


//设置按钮的属性
        fileBtn.setEnabled(true);

        captureBtn.setEnabled(true);

        stopBtn.setEnabled(false);

        playBtn.setEnabled(false);

        saveBtn.setEnabled(false);

//设置窗口的属性

        this.setSize(400, 300);

        this.setTitle("识别");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setLocationRelativeTo(null);

        this.setVisible(true);

    }

    public void actionPerformed(ActionEvent e) {

        if (e.getActionCommand().equals("captureBtn")) {

//点击开始录音按钮后的动作

//停止按钮可以启动

            captureBtn.setEnabled(false);

            stopBtn.setEnabled(true);

            playBtn.setEnabled(false);

            saveBtn.setEnabled(false);

//调用录音的方法
// ================改为线程程
//            capture();
//====================

            AudioStart audioStart = new AudioStart();
            Thread t1 = new Thread(audioStart);
            t1.start();


//记录开始录音的时间

            startPlay = System.currentTimeMillis();

        } else if (e.getActionCommand().equals("stopBtn")) {

//点击停止录音按钮的动作

            captureBtn.setEnabled(true);

            stopBtn.setEnabled(false);

            playBtn.setEnabled(true);

            saveBtn.setEnabled(true);

//调用停止录音的方法

            stop();

//记录停止录音的时间

            long stopPlay = System.currentTimeMillis();

//输出录音的时间

            System.out.println("Play continues " + (stopPlay - startPlay));

        } else if (e.getActionCommand().equals("playBtn")) {

//调用播放录音的方法

            play();

        } else if (e.getActionCommand().equals("saveBtn")) {

//调用保存录音的方法

            save();

        }else if (e.getActionCommand().equals("fileBtn")){


        }

    }

//开始录音

    public void capture() {

        try {

//af为AudioFormat也就是音频格式

            af = getAudioFormat();

            DataLine.Info info = new DataLine.Info(TargetDataLine.class, af);

            td = (TargetDataLine) (AudioSystem.getLine(info));

//打开具有指定格式的行，这样可使行获得所有所需的系统资源并变得可操作。

            td.open(af);

//允许某一数据行执行数据 I/O

            td.start();

//创建播放录音的线程

            Record record = new Record();

            Thread t1 = new Thread(record);

            t1.start();

        } catch (LineUnavailableException ex) {

            ex.printStackTrace();

            return;

        }

    }

//停止录音

    public void stop() {

        stopflag = true;

    }

//播放录音

    public void play() {

//将baos中的数据转换为字节数据

        byte audioData[] = baos.toByteArray();

//转换为输入流

        bais = new ByteArrayInputStream(audioData);

        af = getAudioFormat();

        ais = new AudioInputStream(bais, af, audioData.length / af.getFrameSize());

        try {

            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, af);

            sd = (SourceDataLine) AudioSystem.getLine(dataLineInfo);

            sd.open(af);

            sd.start();

//创建播放进程

            Play py = new Play();

            Thread t2 = new Thread(py);

            t2.start();

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            try {

//关闭流

                if (ais != null) {

                    ais.close();

                }

                if (bais != null) {

                    bais.close();

                }

                if (baos != null) {

                    baos.close();

                }

            } catch (Exception e) {

                e.printStackTrace();

            }

        }

    }


//将开始录音改造为线程录音

    class AudioStart implements Runnable {

//定义存放录音的字节数组,作为缓冲区

        byte bts[] = new byte[10000];

//将字节数组包装到流里，最终存入到baos中

//重写run函数

        public void run() {
            try {
                af = getAudioFormat();

                DataLine.Info info = new DataLine.Info(TargetDataLine.class, af);

                td = (TargetDataLine) (AudioSystem.getLine(info));

//打开具有指定格式的行，这样可使行获得所有所需的系统资源并变得可操作。

                td.open(af);

//允许某一数据行执行数据 I/O

                td.start();


                Record record = new Record();

                Thread t1 = new Thread(record);

                t1.start();


                AccordingAudio audio = new AccordingAudio();
                Thread t2 = new Thread(audio);
                t2.start();


            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }

        }

    }


    int i = 0;
    int count = 0;

    //开始录音的同时  打开  识别线程  由播放录音和保存录音改造

    class AccordingAudio implements Runnable {

        @Override
        public void run() {
            ByteArrayOutputStream baosAudio = new ByteArrayOutputStream();
            ByteArrayInputStream baisAudio = null;
            AudioInputStream aisAudio = null;
            try {

                byte bts[] = new byte[8];
                try {

                    stopflag = false;

                    while (stopflag != true && td != null) {

//当停止录音没按下时，该线程一直执行

//从数据行的输入缓冲区读取音频数据。

//要读取bts.length长度的字节,cnt 是实际读取的字节数

                        int cnt = td.read(bts, 0, bts.length);

                        if (cnt > 0) {

                            baosAudio.write(bts, 0, cnt);


                            if (i == 0) {
                                af = getAudioFormat();

                                byte audioData[] = baosAudio.toByteArray();

                                baisAudio = new ByteArrayInputStream(audioData);

                                aisAudio = new AudioInputStream(baisAudio, af, audioData.length / af.getFrameSize());

                                count = audioData.length;

                                //去识别  并展示

                                System.out.println("====i====" + i);
                                i += 1;
                                System.out.println("======count=====" + count);
                            } else {
                                af = getAudioFormat();

                                byte audioData[] = baosAudio.toByteArray();
                                byte budioData[] = new byte[100];
                                //截取数组   将一开始的去掉
                                System.arraycopy(audioData, count, budioData, 0, audioData.length - count);

                                baisAudio = new ByteArrayInputStream(budioData);

                                aisAudio = new AudioInputStream(baisAudio, af, audioData.length / af.getFrameSize());
                                //去识别  并展示

                                System.out.println("===i====" + i);
                                i++;
                                count = audioData.length;
                                System.out.println("======count=====" + count);

                            }
                        }
                    }
                    if (stopflag == true) {
                        byte audioData[] = baosAudio.toByteArray();
                        if (audioData.length > count) {

                            byte budioData[] = new byte[10000];
                            //截取数组   将一开始的去掉
                            System.arraycopy(audioData, count, budioData, 0, audioData.length - count);
                            //直接将byte[]识别  不再转换成buffer

//                            baisAudio = new ByteArrayInputStream(budioData);
//
//                            aisAudio = new AudioInputStream(baisAudio, af, audioData.length / af.getFrameSize());


                            //去识别  并展示
                            i++;
                            System.out.println("===i====" + i);
                            count = audioData.length;
                            System.out.println("======count=====" + count);
                        }


                    }

                } catch (Exception e) {

                    e.printStackTrace();

                }


            } catch (Exception e) {

                e.printStackTrace();

            }

        }
    }


//保存录音

    public void save() {

//取得录音输入流

        af = getAudioFormat();

        byte audioData[] = baos.toByteArray();

        bais = new ByteArrayInputStream(audioData);

        ais = new AudioInputStream(bais, af, audioData.length / af.getFrameSize());

//定义最终保存的文件名

        File file = null;

//写入文件

        try {

//以当前的时间命名录音的名字

//将录音的文件存放到F盘下语音文件夹下

            long l = System.currentTimeMillis();

            File filePath = new File("/users/qibin/zyyt_相关/" + l + ".pcm");

            if (!filePath.exists()) {//如果文件不存在，则创建该目录

                filePath.mkdir();

            }

            long time = System.currentTimeMillis();

            file = new File(filePath + "/" + time + ".wav");

            AudioSystem.write(ais, AudioFileFormat.Type.WAVE, file);

//将录音产生的wav文件转换为容量较小的mp3格式

//定义产生后文件名

            String tarFileName = time + ".mp3";

            Runtime run = null;

            try {

                run = Runtime.getRuntime();

                long start = System.currentTimeMillis();

//调用解码器来将wav文件转换为mp3文件

                Process p = run.exec(filePath + "/" + "lame -b 16 " + filePath + "/" + file.getName() + " " + filePath + "/" + tarFileName); //16为码率，可自行修改

//释放进程

                p.getOutputStream().close();

                p.getInputStream().close();

                p.getErrorStream().close();

                p.waitFor();

                long end = System.currentTimeMillis();

                System.out.println("convert need costs:" + (end - start) + "ms");

//删除无用的wav文件

                if (file.exists()) {

                    file.delete();

                }

            } catch (Exception e) {

                e.printStackTrace();

            } finally {

//最后都要执行的语句

//run调用lame解码器最后释放内存

                run.freeMemory();

            }

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

//关闭流

            try {

                if (bais != null) {

                    bais.close();

                }

                if (ais != null) {

                    ais.close();

                }

            } catch (Exception e) {

                e.printStackTrace();

            }

        }

    }

//设置AudioFormat的参数

    public AudioFormat getAudioFormat() {

//下面注释部分是另外一种音频格式，两者都可以

        AudioFormat.Encoding encoding = AudioFormat.Encoding.

                PCM_SIGNED;

        float rate = 8000f;

        int sampleSize = 16;

        String signedString = "signed";

        boolean bigEndian = true;

        int channels = 1;

        return new AudioFormat(encoding, rate, sampleSize, channels,

                (sampleSize / 8) * channels, rate, bigEndian);

//      //采样率是每秒播放和录制的样本数

//      float sampleRate = 16000.0F;

//      // 采样率8000,11025,16000,22050,44100

//      //sampleSizeInBits表示每个具有此格式的声音样本中的位数

//      int sampleSizeInBits = 16;

//      // 8,16

//      int channels = 1;

//      // 单声道为1，立体声为2

//      boolean signed = true;

//      // true,false

//      boolean bigEndian = true;

//      // true,false

//      return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed,bigEndian);

    }

//录音类，因为要用到MyRecord类中的变量，所以将其做成内部类

    class Record implements Runnable {

//定义存放录音的字节数组,作为缓冲区

        byte bts[] = new byte[10000];

//将字节数组包装到流里，最终存入到baos中

//重写run函数

        public void run() {

            baos = new ByteArrayOutputStream();

            try {

                stopflag = false;

                while (stopflag != true) {

//当停止录音没按下时，该线程一直执行

//从数据行的输入缓冲区读取音频数据。

//要读取bts.length长度的字节,cnt 是实际读取的字节数

                    int cnt = td.read(bts, 0, bts.length);

                    if (cnt > 0) {

                        baos.write(bts, 0, cnt);

                    }

                }

            } catch (Exception e) {

                e.printStackTrace();

            } finally {

                try {

//关闭打开的字节数组流

                    if (baos != null) {

                        baos.close();

                    }

                } catch (IOException e) {

                    e.printStackTrace();

                } finally {

                    td.drain();

                    td.close();

                }

            }

        }

    }

//播放类,同样也做成内部类

    class Play implements Runnable {

//播放baos中的数据即可

        public void run() {

            byte bts[] = new byte[10000];

            try {

                int cnt;

//读取数据到缓存数据

                while ((cnt = ais.read(bts, 0, bts.length)) != -1) {

                    if (cnt > 0) {

//写入缓存数据

//将音频数据写入到混频器

                        sd.write(bts, 0, cnt);

                    }

                }

            } catch (Exception e) {

                e.printStackTrace();

            } finally {

                sd.drain();

                sd.close();

            }

        }

    }


    //调用文件上传
    public void fileUpload(){

    }

    //格式转换
    public void covertFile(File pathname) {
        String type = new MimetypesFileTypeMap().getContentType(pathname);

    }


}
