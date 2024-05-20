package com.example.hoofit.data;

import java.io.Serializable;
import java.util.Date;

public class Interesting implements Serializable {
    private String id;
    private String type;
    private String name;
    private String description;
    private String uri;
    private Date date;
    private Trail trail;
    private Reserve reserve;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Trail getTrail() {
        return trail;
    }

    public void setTrail(Trail trail) {
        this.trail = trail;
    }

    public Reserve getReserve() {
        return reserve;
    }

    public void setReserve(Reserve reserve) {
        this.reserve = reserve;
    }

    public Interesting(String type, String name, String description, String uri) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.uri = uri;
    }
    public Interesting(String type, String name, String description, Trail trail) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.trail = trail;
    }
    public Interesting(String type, String name, String description, Reserve reserve) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.reserve = reserve;
    }
    public Interesting() {
    }

    public Interesting(String type, String name, String description) {
        this.type = type;
        this.name = name;
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public enum ItemType {
        TRAIL("Trail"),
        RESERVE("Reserve"),
        RESOURCE("Resource");

        private final String displayName;

        ItemType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }


        @Override
        public String toString() {
            return displayName;
        }
    }

}
