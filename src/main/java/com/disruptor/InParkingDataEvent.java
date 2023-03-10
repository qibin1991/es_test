package com.disruptor;

/**
 * @ClassName InParkingDataEvent
 * @Description TODO
 * @Author QiBin
 * @Date 2022/6/24 19:43
 * @Version 1.0
 **/
public class InParkingDataEvent {

    private String carLicense = "";

    public void setCarLicense(String carLicense) {
        this.carLicense = carLicense;
    }

    public String getCarLicense() {
        return carLicense;
    }
}
