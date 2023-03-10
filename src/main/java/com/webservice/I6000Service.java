package com.webservice;


import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class I6000Service {

    /**
     * 累计终端台账数
     */
    public Object terminalTotal() throws Exception {
        HashMap<String, Object> terminalTotalHashMap = new HashMap<>();
        return "========";
    }

    /**
     * 累计访问人次
     */
    public Object requestPersonTotal() throws Exception {
        HashMap<String, Object> terminalTotalHashMap = new HashMap<>();
        return "========";
    }

    /**
     * 补全终端数
     */
    public Object addTerminalTotal() throws Exception {
        HashMap<String, Object> terminalTotalHashMap = new HashMap<>();
        return "========";
    }


    /**
     * 未知终端数
     */
    public Object noTerminalTotal() throws Exception {
        HashMap<String, Object> terminalTotalHashMap = new HashMap<>();
        return "========";
    }

    /**
     * 日终端告警数
     */
    public Object toDayAlarmTotal() throws Exception {
        HashMap<String, Object> terminalTotalHashMap = new HashMap<>();
        return "========";

    }

    /**
     * 月终端告警数
     */
    public Object toMonthAlarmTotal() throws Exception {
        HashMap<String, Object> terminalTotalHashMap = new HashMap<>();
        return "========";
    }


    /**
     * 日告警终端数
     */
    public Object toDayAlarmTerminalTotal() throws Exception {
        HashMap<String, Object> terminalTotalHashMap = new HashMap<>();
        return "========";

    }

    /**
     * 月告警终端数
     */
    public Object toMonthAlarmTerminalTotal() throws Exception {
        HashMap<String, Object> terminalTotalHashMap = new HashMap<>();
        return "========";
    }

    /**
     * 累计终端告警数
     */
    public Object toAlarmTotal() throws Exception {
        HashMap<String, Object> terminalTotalHashMap = new HashMap<>();
        return "\"========\"";
    }

    /**
     * 注册用户数
     */
    public Object toUserTotal() throws Exception {
        HashMap<String, Object> terminalTotalHashMap = new HashMap<>();
        return "========";
    }

    /**
     * 日登录人数
     */
    public Object toDayUserTotal() throws Exception {
        HashMap<String, Object> terminalTotalHashMap = new HashMap<>();

        return "========";
    }

    /**
     * 在线用户数
     */
    public Object toOnlineUserTotal() throws Exception {
        HashMap<String, Object> terminalTotalHashMap = new HashMap<>();
        return  "========";
    }

    /**
     * 业务应用系统占用表空间大小
     */
    public Object toProgramOfChartTotal() throws Exception {
        HashMap<String, Object> terminalTotalHashMap = new HashMap<>();
        return "========";
    }
}
