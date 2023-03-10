package com.es.neo4j.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

import java.io.Serializable;

/**
 * @ClassName TweetTopicVo
 * @Description TODO
 * @Author QiBin
 * @Date 2020/12/23下午12:47
 * @Version 1.0
 **/
@Node("TwitterTopic")
@Builder
@Data
public class TweetTopicVo implements Serializable {
    @org.springframework.data.neo4j.core.schema.Id
    @GeneratedValue
    private Long Id;

    @Property
    private String datasetId;
    @Property
    private String datasetName;
    @Property
    private String tagJson;
    @Property
    private String mediatname = "TwitterTopic";
    @Property
    private Integer delFlage = 0;
    @Property
    private Long updateTimes;

}
