package com.example.hoofit.data;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Reserve implements Serializable {
    private String id;
    private String name;
    private String description;
    private List<Trail> trails;

    public Reserve(String id, String name, String description, List<Trail> trails) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.trails = trails;
    }

    public Reserve() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Trail> getTrails() {
        return trails;
    }

    public void setTrails(List<Trail> trails) {
        this.trails = trails;
    }

    // getters and setters
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reserve that = (Reserve) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

