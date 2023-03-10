package com.es.neo4j.entity;

import lombok.Data;

/**
 * @ClassName LinkedinEdge
 * @Description TODO
 * @Author QiBin
 * @Date 2020/4/2416:20
 * @Version 1.0
 **/
@Data
public class LinkedinEdge {

    private  String startV;
    private String endV;
    private String datasetId;
    private String mediatname;
    private String conTime;
    private String startField;
    private String endField;
    private int delFlag;
    private String startlable;
    private String endlable;
    private long uptime;
    private int startTime;
    private int endTime;
    private String edgue;
    private String _id;
}
