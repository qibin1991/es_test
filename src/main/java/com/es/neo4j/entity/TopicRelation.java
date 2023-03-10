package com.es.neo4j.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;

import java.io.Serializable;

/**
 * @ClassName TopicRelation
 * @Description TODO
 * @Author QiBin
 * @Date 2020/12/23下午12:42
 * @Version 1.0
 **/
@RelationshipProperties
@Builder
@Data
public class TopicRelation implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    //开始节点

    private TweetUserVo startNode;

    //结束节点
    private TweetTopicVo endNode;
    private String startV;
    private  String endV;
    private String datasetId;
    private String mediatname;
    private String uniqueField;
    private String edgue;
    private Long updateTimes;


}
