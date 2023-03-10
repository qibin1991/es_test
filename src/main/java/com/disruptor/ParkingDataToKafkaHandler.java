package com.disruptor;

import com.lmax.disruptor.EventHandler;

/**
 * @ClassName ParkingDataToKafkaHandler
 * @Description TODO
 * @Author QiBin
 * @Date 2022/6/24 19:44
 * @Version 1.0
 **/
public class ParkingDataToKafkaHandler implements EventHandler<InParkingDataEvent> {

    @Override
    public void onEvent(InParkingDataEvent event, long sequence,
                        boolean endOfBatch) throws Exception {
        long threadId = Thread.currentThread().getId();
        String carLicense = event.getCarLicense();
        System.out.println(String.format("Thread Id %s send %s in plaza messsage to kafka...",threadId,carLicense));
    }
}
