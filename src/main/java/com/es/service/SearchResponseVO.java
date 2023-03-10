package com.es.service;

import java.util.List;

/**
 * @ClassName: SearchResponseVO
 * @Description:
 * @Author: yfeng
 * @Date: 2018/11/16 10:42 AM
 */
public class SearchResponseVO<T> {
    private Long totalCount;

    private List<T> data;

    private Long warnTime;

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

    public Long getWarnTime() {
        return warnTime;
    }

    public void setWarnTime(Long warnTime) {
        this.warnTime = warnTime;
    }
}
