package com.tr.nebula.security.web.controller.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by  Mustafa Erbin on 6.04.2017.
 */
public class EndPointTreeModel implements Serializable {

    private String text;

    private Object code;

    private List<EndPointTreeModel> children;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Object getCode() {
        return code;
    }

    public void setCode(Object code) {
        this.code = code;
    }

    public List<EndPointTreeModel> getChildren() {
        return children;
    }

    public void setChildren(List<EndPointTreeModel> children) {
        this.children = children;
    }
}
