package com.tr.nebula.security.db.domain.model;

import com.tr.nebula.security.db.domain.Menu;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mustafa Erbin on 29.03.2017.
 */
public class MenuModel implements Serializable {

    public MenuModel(Menu menu) {
        this.id = menu.getId();
        this.text = menu.getText();
        this.path = menu.getPath();
        this.icon = menu.getIcon();
        this.module = menu.getModule();
        this.items = menu.getItems();
        this.index = menu.getIndex();
    }

    private Long id;

    private String text;

    private String path;

    private String module;

    private String icon;

    private List items;

    private int index;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List getItems() {
        return items;
    }

    public void setItems(List items) {
        this.items = items;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
