package com.zythologue.minimal_api;

public class BeerDTO {
    public String name;
    public String description;
    public float abv;
    public int brewery;
    public int category;

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
        return brewery;
    }

    public void setBrewery(int brewery) {
        this.brewery = brewery;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

}
