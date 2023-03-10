package com.es.neo4j.entity;

/**
 * @ClassName User
 * @Description TODO
 * @Author QiBin
 * @Date 2020/11/2517:14
 * @Version 1.0
 **/

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.io.Serializable;

/**
 * 创建一个Person实体类
 * @author langpf 2019/3/7
 */
@Node
@Data
public class User implements Serializable {
    @Id
    @GeneratedValue
    private Long nodeId;
    private String name;

    private int born;

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBorn() {
        return born;
    }

    public void setBorn(int born) {
        this.born = born;
    }
}
