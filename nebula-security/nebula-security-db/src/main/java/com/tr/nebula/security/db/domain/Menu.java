package com.tr.nebula.security.db.domain;

import com.tr.nebula.persistence.jpa.domain.BaseEntity;
import com.tr.nebula.security.api.domain.NebulaMenu;

import javax.persistence.*;
import java.util.List;

/**
 * Created by  Mustafa Erbin on 29.03.2017.
 */
@Entity(name = "n_menu")
public class Menu extends BaseEntity implements NebulaMenu {
    @Column(length = 50, nullable = false)
    private String text;

    @Column(length = 100, nullable = false)
    private String path;

    @Column(name = "itemIndex")
    private int index;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Menu parent;

    @Column(length = 50)
    private String module;

    @Column(length = 30)
    private String icon;

    @Transient
    private List<Menu> items;

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

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Menu getParent() {
        return parent;
    }

    public void setParent(Menu parent) {
        this.parent = parent;
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

    public List<Menu> getItems() {
        return items;
    }

    public void setItems(List<Menu> items) {
        this.items = items;
    }
}
