package com.example.hoofit.data;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Comment {
    String id;
    String message;
    User user;
    float stars;
    String date;
public Comment(){}
    public Comment(String id, String message, User user, float stars, String date) {
        this.id = id;
        this.message = message;
        this.user = user;
        this.stars = stars;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public float getStars() {
        return stars;
    }

    public void setStars(float stars) {
        this.stars = stars;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment that = (Comment) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}