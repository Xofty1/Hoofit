package com.tvoyhod.hoofit.data;


import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * The type Trail.
 */
public class Trail implements Serializable {
    private String id;
    private String name;
    private double length;
    private String difficulty;
    private String timeRequired;
    private String description;
    private List<Coordinate> coordinatesList;
    private float stars;
    private int commentsCounter;
    private List<Comment> comments;

    /**
     * Gets comments counter.
     *
     * @return the comments counter
     */
    public int getCommentsCounter() {
        return commentsCounter;
    }

    /**
     * Sets comments counter.
     *
     * @param commentsCounter the comments counter
     */
    public void setCommentsCounter(int commentsCounter) {
        this.commentsCounter = commentsCounter;
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
     * Gets comments.
     *
     * @return the comments
     */
    public List<Comment> getComments() {
        return comments;
    }

    /**
     * Sets comments.
     *
     * @param comments the comments
     */
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trail that = (Trail) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Instantiates a new Trail.
     */
    public Trail() {
    }

    /**
     * Instantiates a new Trail.
     *
     * @param id              the id
     * @param name            the name
     * @param length          the length
     * @param difficulty      the difficulty
     * @param timeRequired    the time required
     * @param description     the description
     * @param coordinatesList the coordinates list
     */
    public Trail(String id, String name, double length, String difficulty, String timeRequired, String description, List<Coordinate> coordinatesList) {
        this.id = id;
        this.name = name;
        this.length = length;
        this.difficulty = difficulty;
        this.timeRequired = timeRequired;
        this.description = description;
        this.coordinatesList = coordinatesList;
    }

    /**
     * Instantiates a new Trail.
     *
     * @param id              the id
     * @param name            the name
     * @param length          the length
     * @param difficulty      the difficulty
     * @param timeRequired    the time required
     * @param description     the description
     * @param coordinatesList the coordinates list
     * @param comments        the comments
     */
    public Trail(String id, String name, double length, String difficulty, String timeRequired, String description, List<Coordinate> coordinatesList, List<Comment> comments) {
        this.id = id;
        this.name = name;
        this.length = length;
        this.difficulty = difficulty;
        this.timeRequired = timeRequired;
        this.description = description;
        this.coordinatesList = coordinatesList;
        this.stars = 0;
        this.commentsCounter = 0;
        this.comments = comments;
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets description.
     *
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = description;
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
     * Gets length.
     *
     * @return the length
     */
    public double getLength() {
        return length;
    }

    /**
     * Sets length.
     *
     * @param length the length
     */
    public void setLength(double length) {
        this.length = length;
    }

    /**
     * Gets difficulty.
     *
     * @return the difficulty
     */
    public String getDifficulty() {
        return difficulty;
    }

    /**
     * Sets difficulty.
     *
     * @param difficulty the difficulty
     */
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * Gets time required.
     *
     * @return the time required
     */
    public String getTimeRequired() {
        return timeRequired;
    }

    /**
     * Sets time required.
     *
     * @param timeRequired the time required
     */
    public void setTimeRequired(String timeRequired) {
        this.timeRequired = timeRequired;
    }

    /**
     * Gets coordinates list.
     *
     * @return the coordinates list
     */
    public List<Coordinate> getCoordinatesList() {
        return coordinatesList;
    }

    /**
     * Sets coordinates list.
     *
     * @param coordinatesList the coordinates list
     */
    public void setCoordinatesList(List<Coordinate> coordinatesList) {
        this.coordinatesList = coordinatesList;
    }

}
