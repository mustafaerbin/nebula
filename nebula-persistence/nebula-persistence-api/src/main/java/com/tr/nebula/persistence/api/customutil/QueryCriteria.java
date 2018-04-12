package com.tr.nebula.persistence.api.customutil;

/**
 * Created by Mustafa Erbin on 20.07.2017.
 */
public class QueryCriteria {

    private String field;
    private CriteriaOperator operator;
    private Object value;
    private String type;


    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public CriteriaOperator getOperator() {
        return operator;
    }

    public void setOperator(CriteriaOperator operator) {
        this.operator = operator;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
