package com.zythologue.minimal_api;


public class Beer {
    public Beer(
            int id,
            String name,
            String desc,
            int cat,
            float abv,
            int brew,
            java.sql.Timestamp timestamp,
            java.sql.Timestamp timestamp2) {

        this.id = id;
        this.name = name;
        this.description = desc;
        this.category = cat;
        this.abv = abv;
        this.Brewery = brew;
        this.created = timestamp;
        this.updated = timestamp2;
    }

    public int id;
    public String name;
    public String description;
    public float abv;
    public int Brewery;
    public int category;
    public java.sql.Timestamp created;
    public java.sql.Timestamp updated;

}
