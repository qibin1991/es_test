package com.es.service;

import org.elasticsearch.common.Strings;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.sort.SortOrder;

/**
 * @ClassName: ScrollSearchRequestVO
 * @Description:
 * @Author: yfeng
 * @Date: 2018/11/16 10:57 AM
 */
public class ScrollSearchRequestVO {

    private String[] indexName;
    private String type;
    private Integer limit = 100;
    private String scrollId;
    private Integer keepAlive;//游标ID保持存活时间，单位:秒
    private String[] includeFields = Strings.EMPTY_ARRAY;//只要哪些字段
    private String[] excludeFields = Strings.EMPTY_ARRAY;//排除哪些字段
    private QueryBuilder queryBuilder;//构建的查询条件

    private SortOrder sortValue;//排序规则
    private String sortField;//排序字段

    public SortOrder getSortValue() {
        return sortValue;
    }

    public void setSortValue(SortOrder sortValue) {
        this.sortValue = sortValue;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

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

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getScrollId() {
        return scrollId;
    }

    public void setScrollId(String scrollId) {
        this.scrollId = scrollId;
    }

    public Integer getKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(Integer keepAlive) {
        this.keepAlive = keepAlive;
    }

    public String[] getIncludeFields() {
        return includeFields;
    }

    public void setIncludeFields(String[] includeFields) {
        this.includeFields = includeFields;
    }

    public String[] getExcludeFields() {
        return excludeFields;
    }

    public void setExcludeFields(String[] excludeFields) {
        this.excludeFields = excludeFields;
    }

    public QueryBuilder getQueryBuilder() {
        return queryBuilder;
    }

    public void setQueryBuilder(QueryBuilder queryBuilder) {
        this.queryBuilder = queryBuilder;
    }


}
