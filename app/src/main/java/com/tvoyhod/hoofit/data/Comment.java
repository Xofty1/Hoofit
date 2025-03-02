package com.tvoyhod.hoofit.data;

import java.util.Objects;

/**
 * The type Comment.
 */
public class Comment {
    /**
     * The Id.
     */
    String id;
    /**
     * The Message.
     */
    String message;
    /**
     * The User.
     */
    User user;
    /**
     * The Stars.
     */
    float stars;
    /**
     * The Date.
     */
    String date;

    /**
     * Instantiates a new Comment.
     */
    public Comment(){}

    /**
     * Instantiates a new Comment.
     *
     * @param id      the id
     * @param message the message
     * @param user    the user
     * @param stars   the stars
     * @param date    the date
     */
    public Comment(String id, String message, User user, float stars, String date) {
        this.id = id;
        this.message = message;
        this.user = user;
        this.stars = stars;
        this.date = date;
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
     * Gets message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets message.
     *
     * @param message the message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets user.
     *
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets user.
     *
     * @param user the user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Gets stars.
     *
     * @return the stars
     */
    public float getStars() {
        return stars;
    }

    /**
     * Sets stars.
     *
     * @param stars the stars
     */
    public void setStars(float stars) {
        this.stars = stars;
    }

    /**
     * Gets date.
     *
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets date.
     *
     * @param date the date
     */
    public void setDate(String date) {
        this.date = date;
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
