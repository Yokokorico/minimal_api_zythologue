package com.zythologue.minimal_api.Model;

import java.sql.Timestamp;

public class Ingredient {
    public int id;
    public String name;
    public String type;
    public Timestamp created;
    public Timestamp updated;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public Timestamp getCreated() {
        return created;
    }
    public void setCreated(Timestamp created) {
        this.created = created;
    }
    public Timestamp getUpdated() {
        return updated;
    }
    public void setUpdated(Timestamp updated) {
        this.updated = updated;
    }
    }
