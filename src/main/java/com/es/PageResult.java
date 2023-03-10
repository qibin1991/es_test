package com.es;

import java.io.Serializable;
import java.util.List;

/**
 * 分页的结果集
 * @author QiBin
 */
public class PageResult<T> implements Serializable{

    private long total;
    private List<T> rows;
    private long totalPage;


    public PageResult(){

    }

    public PageResult(long total, List<T> rows) {
        this.total = total;
        this.rows = rows;
    }

    public long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(long totalPage) {
        this.totalPage = totalPage;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
