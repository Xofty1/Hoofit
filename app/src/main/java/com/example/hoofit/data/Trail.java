package com.example.hoofit.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Trail {
    @SerializedName("ID")
    private int id;

    @SerializedName("coordinates")
    private List<Coordinate> coordinates;

    @SerializedName("trail_name")
    private String trailName;

    @SerializedName("description")
    private String description;

    @SerializedName("status")
    private String status;

    @SerializedName("liked")
    private boolean liked;

    // Конструктор, геттеры и сеттеры
    public Trail() {}

    public Trail(int id, List<Coordinate> coordinates, String trailName, String description, String status, boolean liked) {
        this.id = id;
        this.coordinates = coordinates;
        this.trailName = trailName;
        this.description = description;
        this.status = status;
        this.liked = liked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Coordinate> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Coordinate> coordinates) {
        this.coordinates = coordinates;
    }

    public String getTrailName() {
        return trailName;
    }

    public void setTrailName(String trailName) {
        this.trailName = trailName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean getLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }
}

