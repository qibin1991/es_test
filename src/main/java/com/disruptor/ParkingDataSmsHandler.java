package com.disruptor;

import com.lmax.disruptor.EventHandler;

/**
 * @ClassName ParkingDataSmsHandler
 * @Description TODO
 * @Author QiBin
 * @Date 2022/6/24 19:44
 * @Version 1.0
 **/
public class ParkingDataSmsHandler implements EventHandler<InParkingDataEvent> {


    @Override
    public void onEvent(InParkingDataEvent event, long sequence, boolean endOfBatch) throws Exception {
        long threadId = Thread.currentThread().getId();
        String carLicense = event.getCarLicense();
        System.out.println(String.format("Thread Id %s send %s in plaza sms to user",threadId,carLicense));
    }
}
