package com.es.service;

import java.util.List;

/**
 * @ClassName: SearchResponseVO
 * @Description:
 * @Author: yfeng
 * @Date: 2018/11/16 10:42 AM
 */
public class ScrollSearchResponseVO<T> {
    private String scrollId;

    private Long totalCount;

    private List<T> data;

    public String getScrollId() {
        return scrollId;
    }

    public void setScrollId(String scrollId) {
        this.scrollId = scrollId;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

}
