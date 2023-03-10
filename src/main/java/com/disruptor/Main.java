package com.disruptor;

/**
 * @ClassName Main
 * @Description TODO
 * @Author QiBin
 * @Date 2022/6/24 19:40
 * @Version 1.0
 **/

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.dsl.ProducerType;
/**
 * 多生产者多消费者模型
 *
 * @author lzhcode
 */

/**
 * Disruptor它适合一切异步环境，但是对于并发量小的场景不一定需要。在log4j2中，已经使用了disruptor进行
 * 日志记录,同样是用异步，选择disruptor会更快。下面以简单场景举例：
 *
 *
 * 停车批量入场数据上报，数据上报后需要对每条入场数据存入DB，还需要发送kafka消息给其他业务系统。
 * 如果执行完所有的操作，再返回，那么接口耗时比较长，我们可以批量上报后验证数据正确性，
 * 通过后按单条入场数据写入环形队列，然后直接返回成功。
 *
 * 实现方式一：启 动2个消费者线程，一个消费者去执行db入库，一个消费者去发送kafka消息。
 *
 * 实现方式二：启动4个消费者，2个消费者并发执行db入库，两个消费者并发发送kafka消息，
 * 充分利用cpu多核特性，提高执行效率。
 *
 * 实现方式三：如果要求写入DB和kafka后，需要给用户发送短信。那么可以启动三个消费者线程，一个执行db插入，
 * 一个执行kafka消息发布，最后一个依赖前两个线程执行成功，前两个线程都执行成功后，该线程执行短信发送。
 *
 *
 *
 * 使用步骤:
 * 1定义事件
 *     InParkingDataEvent.java
 * 2定义事件处理的具体实现
 * 	ParkingDataInDbHandler.java
 * 	ParkingDataSmsHandler.java
 * 	ParkingDataToKafkaHandler.java
 * 3.发布事件类实现(Disruptor 要求 RingBuffer.publish 必须得到调用，如果发生异常也一样要调用 publish，
 *     那么，很显然这个时候需要调用者在事件处理的实现上来判断事件携带的数据是否是正确的或者完整的)
 *     InParkingDataEventPublisher.java
 * 4定义用于事件处理的线程池, 指定等待策略, 启动 Disruptor,执行完毕后关闭Disruptor
 *     Main.java
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {
        long beginTime = System.currentTimeMillis();

        //最好是2的n次方
        int bufferSize = 1024;
        // Disruptor交给线程池来处理，共计 p1,c1,c2,c3四个线程
        ExecutorService executor = Executors.newFixedThreadPool(4);
        // 构造缓冲区与事件生成
        Disruptor<InParkingDataEvent> disruptor = new Disruptor<InParkingDataEvent>(
                new EventFactory<InParkingDataEvent>() {
                    @Override
                    public InParkingDataEvent newInstance() {
                        return new InParkingDataEvent();
                    }
                }, bufferSize, executor, ProducerType.SINGLE, new YieldingWaitStrategy());

        // 使用disruptor创建消费者组C1,C2
        EventHandlerGroup<InParkingDataEvent> handlerGroup = disruptor.handleEventsWith(new ParkingDataToKafkaHandler(),
                new ParkingDataInDbHandler());

        ParkingDataSmsHandler smsHandler = new ParkingDataSmsHandler();
        // 声明在C1,C2完事之后执行JMS消息发送操作 也就是流程走到C3
        handlerGroup.then(smsHandler);

        disruptor.start();// 启动
        //线程 调度
        CountDownLatch latch = new CountDownLatch(1);

        // 生产者准备
        executor.submit(new InParkingDataEventPublisher(latch, disruptor));
        latch.await();// 等待生产者结束
        disruptor.shutdown();
        executor.shutdown();

        System.out.println("总耗时:" + (System.currentTimeMillis() - beginTime));
    }

}

