package com.leetcode.Strategy;

/**
 * @ClassName TestController
 * @Description TODO
 * @Author QiBin
 * @Date 2021/11/1414:45
 * @Version 1.0
 **/

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Autowired
    private SimpleContext simpleContext;

    /*
     *
     * @param demoPojo
     * @return
     */
    @PostMapping("/choose")
    public String choose(@RequestBody DemoPojo demoPojo){
        return simpleContext.getResource(demoPojo);
    }
}
