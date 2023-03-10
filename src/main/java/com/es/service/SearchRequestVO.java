package com.es.service;

import org.elasticsearch.common.Strings;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.sort.SortOrder;

/**
 * @ClassName: SearchRequestVO
 * @Description:
 * @Author: yfeng
 * @Date: 2018/11/16 10:56 AM
 */
public class SearchRequestVO {
    private String[] indexName;
    private String type;
    /***当前页****/
    private int pageNo = 1;
    /***每页数据****/
    private int pageSize = 10;
    private long totalCount = -1L;
    private String sortField;//排序字段
    private SortOrder sortOrder;//排序方式
    private String[] includeFields = Strings.EMPTY_ARRAY;//只要哪些字段
    private String[] excludeFields = Strings.EMPTY_ARRAY;//排除哪些字段
    private QueryBuilder queryBuilder;//构建的查询条件

    /*******添加高亮属性*********/
    private int fragmentSize;            //高亮大小     默认100
    private int numberOfFragments;       //高亮片段  默认5（0时不分片段）

    public int getFragmentSize() {
        return fragmentSize;
    }

    public void setFragmentSize(int fragmentSize) {
        this.fragmentSize = fragmentSize;
    }

    public int getNumberOfFragments() {
        return numberOfFragments;
    }

    public void setNumberOfFragments(int numberOfFragments) {
        this.numberOfFragments = numberOfFragments;
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

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
        if (this.pageNo < 1){
            this.pageNo = 1;
        }
    }

    /**
     * 当前页开始条数
     * @return
     */
    public int getStartFrom() {
        return (this.pageNo - 1) * this.pageSize;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public SortOrder getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(SortOrder sortOrder) {
        this.sortOrder = sortOrder;
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
