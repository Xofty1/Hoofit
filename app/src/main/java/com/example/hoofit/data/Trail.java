package com.example.hoofit.data;


import java.util.List;

//public class Trail {
//    private int id;
//
//    private List<Coordinate> coordinates;
//
//    private String trailName;
//
//    private String description;
//
//    private String status;
//
//    private boolean liked;
//
//    // Конструктор, геттеры и сеттеры
//    public Trail() {}
//
//    public Trail(int id, List<Coordinate> coordinates, String trailName, String description, String status, boolean liked) {
//        this.id = id;
//        this.coordinates = coordinates;
//        this.trailName = trailName;
//        this.description = description;
//        this.status = status;
//        this.liked = liked;
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public List<Coordinate> getCoordinates() {
//        return coordinates;
//    }
//
//    public void setCoordinates(List<Coordinate> coordinates) {
//        this.coordinates = coordinates;
//    }
//
//    public String getTrailName() {
//        return trailName;
//    }
//
//    public void setTrailName(String trailName) {
//        this.trailName = trailName;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//
//    public boolean getLiked() {
//        return liked;
//    }
//
//    public void setLiked(boolean liked) {
//        this.liked = liked;
//    }
//}

public class Trail {
    private int id;
    private String name;
    private double length;
    private String difficulty;
    private String timeRequired;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private List<Coordinate> coordinatesList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    // getters and setters
}
