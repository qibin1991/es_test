package com.es.neo4j.entity;

/**
 * @ClassName UserRelation
 * @Description TODO
 * @Author QiBin
 * @Date 2020/11/2517:21
 * @Version 1.0
 **/

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;

import java.io.Serializable;

/**
 * 关系节点类型
 * @author langpf 2019/3/7
 */
@RelationshipProperties // type为空时默认为class名称
public class UserRelation implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    //开始节点

    private User startNode;

    //结束节点



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getStartNode() {
        return startNode;
    }

    public void setStartNode(User startNode) {
        this.startNode = startNode;
    }

}
