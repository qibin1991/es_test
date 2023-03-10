package com.disruptor;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;

/**
 * @ClassName ParkingDataInDbHandler
 * @Description TODO
 * @Author QiBin
 * @Date 2022/6/24 19:43
 * @Version 1.0
 **/
public class ParkingDataInDbHandler implements EventHandler<InParkingDataEvent>, WorkHandler<InParkingDataEvent> {

    @Override
    public void onEvent(InParkingDataEvent event) throws Exception {
        long threadId = Thread.currentThread().getId();
        String carLicense = event.getCarLicense();
        System.out.println(String.format("Thread Id %s save %s into db ....",threadId,carLicense));
    }

    @Override
    public void onEvent(InParkingDataEvent event, long sequence, boolean endOfBatch) throws Exception {
        // TODO Auto-generated method stub
        this.onEvent(event);
    }

}
