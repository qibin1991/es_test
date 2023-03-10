package com.es.service;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;

/**
 * @ClassName: AggRequestVO
 * @Description:
 * @Author: yfeng
 * @Date: 2018/11/16 10:56 AM
 */
public class AggRequestVO {
    private String[] indexName;
    private String type;
    private Integer size;
    private QueryBuilder queryBuilder;//查询条件构建
    private AggregationBuilder aggregationBuilder; //聚合构建

    public String[] getIndexName() {
        return indexName;
    }

    public void setIndexName(String[] indexName) {
        this.indexName = indexName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public QueryBuilder getQueryBuilder() {
        return queryBuilder;
    }

    public void setQueryBuilder(QueryBuilder queryBuilder) {
        this.queryBuilder = queryBuilder;
    }

    public AggregationBuilder getAggregationBuilder() {
        return aggregationBuilder;
    }

    public void setAggregationBuilder(AggregationBuilder aggregationBuilder) {
        this.aggregationBuilder = aggregationBuilder;
    }
}
