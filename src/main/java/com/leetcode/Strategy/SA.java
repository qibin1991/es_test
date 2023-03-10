package com.leetcode.Strategy;

/**
 * @ClassName SA
 * @Description TODO
 * @Author QiBin
 * @Date 2021/11/1414:43
 * @Version 1.0
 **/

import org.springframework.stereotype.Component;

@Component("A")
public class SA implements Strategy {
    @Override
    public String getVpcList(DemoPojo demoPojo) {
        System.out.println("A,getVpcList ==========="+demoPojo);
        return demoPojo.getName()+"a";
    }
}
