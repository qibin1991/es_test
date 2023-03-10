package com.es.service;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @ClassName FbTopicVo
 * @Description TODO
 * @Author QiBin
 * @Date 2020/9/2318:35
 * @Version 1.0
 **/
@Data
public class FbTopicVo {
    @JSONField(name = "#text")
    private String topicText;
    @JSONField(name = "#link")
    private String topicLink;
}
