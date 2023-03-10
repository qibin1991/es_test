package com.webservice;

import java.util.Arrays;

public class I6000Result {

    private String status;
    private String message;
    private String reason;
    private Object[] kpis;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Object[] getKpis() {
        return kpis;
    }

    public void setKpis(Object[] kpis) {
        this.kpis = kpis;
    }

    @Override
    public String toString() {
        return "I6000Result{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", reason='" + reason + '\'' +
                ", kpis=" + Arrays.toString(kpis) +
                '}';
    }
}
