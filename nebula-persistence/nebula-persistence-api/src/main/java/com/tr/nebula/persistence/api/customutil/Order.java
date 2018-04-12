package com.tr.nebula.persistence.api.customutil;

/**
 * Created by  Mustafa Erbin on 24.07.2017.
 */
public class Order {
    private String field;

    private OrderType type;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public OrderType getType() {
        return type;
    }

    public void setType(OrderType type) {
        this.type = type;
    }
}
