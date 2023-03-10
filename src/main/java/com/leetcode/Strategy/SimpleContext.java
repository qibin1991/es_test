package com.leetcode.Strategy;

/**
 * @ClassName SimpleContext
 * @Description TODO
 * @Author QiBin
 * @Date 2021/11/1414:45
 * @Version 1.0
 **/

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/***、
 *通过Spring将实现Strategy的实现类都自动注入到strategyMap类中，
 * 当用户传入选择的资源池时，自动根据资源池id去对应的资源池实现中查找资源。
 *
 */
@Service
public class SimpleContext {
    @Autowired
    private final Map<String, Strategy> strategyMap = new ConcurrentHashMap<>();

    public SimpleContext(Map<String, Strategy> strategyMap) {
        this.strategyMap.clear();
        strategyMap.forEach((k, v)-> this.strategyMap.put(k, v));
    }

    public String getResource(DemoPojo demoPojo){
        return strategyMap.get(demoPojo.getPoolid()).getVpcList(demoPojo);
    }
}
