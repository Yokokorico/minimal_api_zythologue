package com.zythologue.minimal_api.Model;


public class Beer {
    

    public int id;
    public String name;
    public String description;
    public float abv;
    public int Brewery;
    public int category;
    public java.sql.Timestamp created;
    public java.sql.Timestamp updated;
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
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public float getAbv() {
        return abv;
    }
    public void setAbv(float abv) {
        this.abv = abv;
    }
    public int getBrewery() {
        return Brewery;
    }
    public void setBrewery(int brewery) {
        Brewery = brewery;
    }
    public int getCategory() {
        return category;
    }
    public void setCategory(int category) {
        this.category = category;
    }
    public java.sql.Timestamp getCreated() {
        return created;
    }
    public void setCreated(java.sql.Timestamp created) {
        this.created = created;
    }
    public java.sql.Timestamp getUpdated() {
        return updated;
    }
    public void setUpdated(java.sql.Timestamp updated) {
        this.updated = updated;
    }

}
