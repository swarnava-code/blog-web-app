package com.mukut.demo.model;


public class PageModel {
    private Integer pageNo;
    private Integer start;
    private Integer limit;

    public Integer getPageNo() {
        return pageNo;
    }

    @Override
    public String toString() {
        return "PageModel{" +
                "pageNo=" + pageNo +
                ", start=" + start +
                ", limit=" + limit +
                '}';
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getStart() {
        return start;
    }

    public String getUrl() {
        return "/?start=" + start + "&limit=" + limit;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
