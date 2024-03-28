package com.zythologue.minimal_api;

import java.security.Timestamp;

public class Review {
    public User user;
    public Beer beer;
    public int rating;
    public String review;
    public Timestamp created;
    public Timestamp updated;
}
