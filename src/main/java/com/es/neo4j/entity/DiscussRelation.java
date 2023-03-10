package com.es.neo4j.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;

/**
 * @ClassName DiscussRelation
 * @Description TODO
 * @Author QiBin
 * @Date 2020/12/23下午12:43
 * @Version 1.0
 **/
@RelationshipProperties
@Builder
@Data
public class DiscussRelation {

    @Id
    @GeneratedValue
    private Long id;

    //开始节点

    private TweetsVo startNode;

    //结束节点
    private TweetsVo endNode;
    private String startV;
    private  String endV;
    private String datasetId;
    private String mediatname;
    private String uniqueField;
    private String edgue;
    private Long updateTimes;
}
