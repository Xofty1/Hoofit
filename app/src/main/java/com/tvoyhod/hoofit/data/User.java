package com.tvoyhod.hoofit.data;

import java.util.ArrayList;
import java.util.List;

/**
 * The type User.
 */
public class User {
    private String id;
    private String name;
    private String username;
    private String email;
    private boolean isAdmin;
    private List<Trail> likedTrails = new ArrayList<>();

    /**
     * Instantiates a new User.
     *
     * @param id       the id
     * @param name     the name
     * @param username the username
     * @param email    the email
     * @param trails   the trails
     * @param isAdmin  the is admin
     */
    public User(String id, String name, String username, String email, List<Trail> trails, boolean isAdmin) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.likedTrails = trails;
        this.isAdmin = isAdmin;
    }

    /**
     * Instantiates a new User.
     */
    public User() {
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets username.
     *
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets email.
     *
     * @param email the email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Is admin boolean.
     *
     * @return the boolean
     */
    public boolean isAdmin() {
        return isAdmin;
    }

    /**
     * Sets admin.
     *
     * @param admin the admin
     */
    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    /**
     * Gets liked trails.
     *
     * @return the liked trails
     */
    public List<Trail> getLikedTrails() {
        return likedTrails;
    }

    /**
     * Sets liked trails.
     *
     * @param likedTrails the liked trails
     */
    public void setLikedTrails(List<Trail> likedTrails) {
        this.likedTrails = likedTrails;
    }
}
