package com.disruptor;

/**
 * @ClassName DisruptorTest
 * @Description TODO
 * @Author QiBin
 * @Date 2022/6/24 16:16
 * @Version 1.0
 **/


import sun.misc.Contended;

import java.util.concurrent.ThreadFactory;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

/**
 * @Description:
 * @Created on 2019-10-04
 */
public class DisruptorTest {


    /**
     * 在Disruptor中有几个比较关键的:
     *
     * ThreadFactory：这是一个线程工厂，用于我们Disruptor中生产、消费的时候需要的线程。
     * EventFactory：事件工厂，用于产生我们队列元素的工厂。在Disruptor中，他会在初始化的时候直接填充满RingBuffer，一次到位。
     * EventHandler：用于处理Event的handler，这里一个EventHandler可以看做是一个消费者，但是多个EventHandler他们都是独立消费的队列。
     * WorkHandler:也是用于处理Event的handler，和上面区别在于，多个消费者都是共享同一个队列。
     * WaitStrategy：等待策略，在Disruptor中有多种策略，来决定消费者在消费时，如果没有数据采取的策略是什么？下面简单列举一下Disruptor中的部分策略
     * BlockingWaitStrategy：通过线程阻塞的方式，等待生产者唤醒，被唤醒后，再循环检查依赖的sequence是否已经消费。
     * BusySpinWaitStrategy：线程一直自旋等待，可能比较耗cpu
     * YieldingWaitStrategy：尝试100次，然后Thread.yield()让出cpu
     *
     */


    public static void main(String[] args) throws Exception {
        // 队列中的元素
        class Element {
            @Contended
            private String value;


            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }

        // 生产者的线程工厂
        ThreadFactory threadFactory = new ThreadFactory() {
            int i = 0;
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "simpleThread" + String.valueOf(i++));
            }
        };

        // RingBuffer生产工厂,初始化RingBuffer的时候使用
        EventFactory<Element> factory = new EventFactory<Element>() {
            @Override
            public Element newInstance() {
                return new Element();
            }
        };

        // 处理Event的handler
        EventHandler<Element> handler = new EventHandler<Element>() {
            @Override
            public void onEvent(Element element, long sequence, boolean endOfBatch) throws InterruptedException {
                System.out.println("Element: " + Thread.currentThread().getName() + ": " + element.getValue() + ": " + sequence);
//                Thread.sleep(10000000);
            }
        };


        // 阻塞策略
        BlockingWaitStrategy strategy = new BlockingWaitStrategy();

        // 指定RingBuffer的大小
        int bufferSize = 8;

        // 创建disruptor，采用单生产者模式
        Disruptor<Element> disruptor = new Disruptor(factory, bufferSize, threadFactory, ProducerType.SINGLE, strategy);

        // 设置EventHandler
        disruptor.handleEventsWith(handler);

        // 启动disruptor的线程
        disruptor.start();
        for (int i = 0; i < 10; i++) {
            disruptor.publishEvent((element, sequence) -> {
                System.out.println("之前的数据" + element.getValue() + "当前的sequence" + sequence);
                element.setValue("我是第" + sequence + "个");
            });

        }
    }
}

