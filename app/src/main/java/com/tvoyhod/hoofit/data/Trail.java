package com.tvoyhod.hoofit.data;


import java.io.Serializable;
import java.util.List;
import java.util.Objects;

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

    public int getCommentsCounter() {
        return commentsCounter;
    }

    public void setCommentsCounter(int commentsCounter) {
        this.commentsCounter = commentsCounter;
    }

    public float getStars() {
        return stars;
    }

    public void setStars(float stars) {
        this.stars = stars;
    }

    public List<Comment> getComments() {
        return comments;
    }

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
    public Trail() {
    }

    public Trail(String id, String name, double length, String difficulty, String timeRequired, String description, List<Coordinate> coordinatesList) {
        this.id = id;
        this.name = name;
        this.length = length;
        this.difficulty = difficulty;
        this.timeRequired = timeRequired;
        this.description = description;
        this.coordinatesList = coordinatesList;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getTimeRequired() {
        return timeRequired;
    }

    public void setTimeRequired(String timeRequired) {
        this.timeRequired = timeRequired;
    }

    public List<Coordinate> getCoordinatesList() {
        return coordinatesList;
    }

    public void setCoordinatesList(List<Coordinate> coordinatesList) {
        this.coordinatesList = coordinatesList;
    }

}
