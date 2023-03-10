package com.es.neo4j.entity;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.io.Serializable;

/**
 * @ClassName UserNode
 * @Description TODO
 * @Author QiBin
 * @Date 2020/11/2517:19
 * @Version 1.0
 **/
@Node
@Data
public class UserNode implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private  String name;
    private String userId;

    private String companyId;

    private Integer age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    @Override
    public String toString() {
        return "UserNode{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", userId='" + userId + '\'' +
                ", companyId='" + companyId + '\'' +
                '}';
    }
}
