package com.tr.nebula.web.dev.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Random;
import java.util.UUID;

/**
 * Created by kamilbukum on 21/03/2017.
 */
@Entity
public class Example {
    @Id
    private String oid;

    @Column
    private String name;

    public Example(){
        this.oid = UUID.randomUUID().toString();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getOid() {
        return oid;
    }
}
