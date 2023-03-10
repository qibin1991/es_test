package com.leetcode.Strategy;

import org.springframework.stereotype.Component;

/**
 * @ClassName SC
 * @Description TODO
 * @Author QiBin
 * @Date 2021/11/1414:43
 * @Version 1.0
 **/

@Component("C")
public class SC implements Strategy {
    @Override
    public String getVpcList(DemoPojo demoPojo) {
        System.out.println("c"+demoPojo);
        return demoPojo.getName()+"c";
    }
}