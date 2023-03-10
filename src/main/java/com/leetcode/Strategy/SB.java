package com.leetcode.Strategy;

/**
 * @ClassName SB
 * @Description TODO
 * @Author QiBin
 * @Date 2021/11/1414:43
 * @Version 1.0
 **/
import org.springframework.stereotype.Component;

@Component("B")
public class SB implements Strategy {
    @Override
    public String getVpcList(DemoPojo demoPojo) {
        System.out.println("B,getVpcList ==========="+demoPojo);
        return demoPojo.getName()+"b";
    }
}
