package com.mp3;

import ws.schild.jave.*;

import javax.sound.sampled.*;
import java.io.File;
import java.util.Scanner;

/**
 * @ClassName Util
 * @Description TODO
 * @Author QiBin
 * @Date 2021/5/713:51
 * @Version 1.0
 **/
public class Util {
    public void codec() {
        Encoder encoder = new Encoder();
        try {
            for (int i = 0; i < encoder.getAudioEncoders().length; i++) {
                System.out.println(encoder.getAudioEncoders()[i].toString());
            }
        } catch (EncoderException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public boolean m4aToWav() {
        boolean succeeded = true;
        try {
            File source = new File("source.m4a");
            File target = new File("target.wav");

            //Audio Attributes
            AudioAttributes audio = new AudioAttributes();
            audio.setCodec("pcm_s16le");
            audio.setBitRate(16000);
            audio.setChannels(1);
            audio.setBitRate(16000);
            audio.setSamplingRate(16000);

            //Encoding attributes
            EncodingAttributes attrs = new EncodingAttributes();
            attrs.setFormat("wav");
            attrs.setAudioAttributes(audio);

            //Encode
            Encoder encoder = new Encoder();
            encoder.encode(new MultimediaObject(source), target, attrs);

        } catch (Exception ex) {
            ex.printStackTrace();
            succeeded = false;
        }
        return succeeded;
    }


    static TargetDataLine targetDataLine;

    static AudioFormat audioFormat;


    public static void captureAudio() {
        try {
// 构造具有线性 PCM 编码和给定参数的 AudioFormat。

            audioFormat = getAudioFormat();

// 根据指定信息构造数据行的信息对象，这些信息包括单个音频格式。此构造方法通常由应用程序用于描述所需的行。

// lineClass - 该信息对象所描述的数据行的类

// format - 所需的格式

            DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);

// 如果请求 DataLine，且 info 是 DataLine.Info 的实例(至少指定一种完全限定的音频格式)，

// 上一个数据行将用作返回的 DataLine 的默认格式。

            targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);

// 开启线程

            new CaptureThread().start();

        } catch (Exception e) {
            e.printStackTrace();

            System.exit(0);

        }

    }

    static class CaptureThread extends Thread {
        public void run() {
// 指定的文件类型

            AudioFileFormat.Type fileType = null;

// 设置文件类型和文件扩展名

            fileType = AudioFileFormat.Type.WAVE;

            try {
// format - 所需音频格式

                targetDataLine.open(audioFormat);

// 当开始音频捕获或回放时，生成 START 事件。

                targetDataLine.start();

// new AudioInputStream(TargetDataLine line):构造从指示的目标数据行读取数据的音频输入流。该流的格式与目标数据行的格式相同,line - 此流从中获得数据的目标数据行。

// stream - 包含要写入文件的音频数据的音频输入流

// fileType - 要写入的音频文件的种类

// out - 应将文件数据写入其中的外部文件

                AudioSystem.write(new AudioInputStream(targetDataLine), fileType, new File("/users/qibin/mp3/1.wav"));
                //调用sdk  识别

//AudioSystem.write(new AudioInputStream(new ByteArrayInputStream(bt),audioFormat,bt.length / audioFormat.getFrameSize()),fileType,"new File("D://ss.wav")");

            } catch (Exception e) {
                e.printStackTrace();

            }

        }

    }

    public static AudioFormat getAudioFormat() {
// 8000,11025,16000,22050,44100 采样率

        float sampleRate = 8000F;

// 8,16 每个样本中的位数

        int sampleSizeInBits = 16;

// 1,2 信道数(单声道为 1，立体声为 2，等等)

        int channels = 2;

// true,false

        boolean signed = true;

// true,false 指示是以 big-endian 顺序还是以 little-endian 顺序存储音频数据。

        boolean bigEndian = false;

// 构造具有线性 PCM 编码和给定参数的 AudioFormat。

        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed,

                bigEndian);

    }

// 关闭targetDataLine

    public static void closeCaptureAudio() {
        targetDataLine.stop();

        targetDataLine.close();

    }


    public static void main(String[] agrs) {


        Scanner sc = new Scanner(System.in);

        String x = sc.nextLine();
        System.out.println("=====");
        if (x.equals("yes")) {
            captureAudio(); //开始录音的方法

        }

        x = sc.nextLine();

        if (x.equals("no")) {
            closeCaptureAudio(); //关闭录音的方法

        }
    }
}
