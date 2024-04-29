package com.example.hoofit.data;

import java.util.List;

public class User {
    private String name;
    private String username;
    private String email;
    private boolean isAdmin = false;
    private List<Trail> likedTrails;

    public User(String name, String username, String email, List<Trail> trails, boolean isAdmin) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.likedTrails = trails;
        this.isAdmin = isAdmin;
    }

    public User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public List<Trail> getLikedTrails() {
        return likedTrails;
    }

    public void setLikedTrails(List<Trail> likedTrails) {
        this.likedTrails = likedTrails;
    }
}
