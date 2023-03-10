package com.es.neo4j.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;

import java.io.Serializable;

/**
 * @ClassName CommentRelation
 * @Description TODO
 * @Author QiBin
 * @Date 2020/12/23下午12:43
 * @Version 1.0
 **/
@RelationshipProperties
@Builder
@Data
public class CommentRelation implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    //开始节点

    private TweetUserVo startNode;

    //结束节点
    private TweetUserVo endNode;
    private String startV;
    private  String endV;
    private String datasetId;
    private String mediatname;
    private String uniqueField;
    private String edgue;
    private Long updateTimes;

}
