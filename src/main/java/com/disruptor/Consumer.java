package com.disruptor;

/**
 * @ClassName Consumer
 * @Description TODO
 * @Author QiBin
 * @Date 2022/6/24 20:08
 * @Version 1.0
 **/
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import com.lmax.disruptor.WorkHandler;

public class Consumer implements WorkHandler<Order> {

    private String comsumerId;

    private static AtomicInteger count = new AtomicInteger(0);

    private Random random = new Random();

    public Consumer(String comsumerId) {
        this.comsumerId = comsumerId;
    }

    public void onEvent(Order event) throws Exception {
        Thread.sleep(1 * random.nextInt(5));

//		if("C1".equals(comsumerId)){
//			Thread.sleep(100*1000);
//		}
        System.err.println("当前消费者: " + this.comsumerId + ", 消费信息ID: " + event.getId());
        count.incrementAndGet();
    }

    public int getCount(){
        return count.get();
    }


}

