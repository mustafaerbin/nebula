package com.tr.nebula.persistence.api.customutil;

import java.util.List;
import java.util.Map;

/**
 * Created by Mustafa Erbin on 26.07.2017.
 */
public class CustomQuery {
    private int offset;
    private int maxResults = 100;
    private List<QueryCriteria> criteria;

    private Map<String, String[]> selectionStrings;

    private List<Order> orderList;

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }

    public List<QueryCriteria> getCriteria() {
        return criteria;
    }

    public void setCriteria(List<QueryCriteria> criteria) {
        this.criteria = criteria;
    }

    public Map<String, String[]> getSelectionStrings() {
        return selectionStrings;
    }

    public void setSelectionStrings(Map<String, String[]> selectionStrings) {
        this.selectionStrings = selectionStrings;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }
}
