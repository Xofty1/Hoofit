package com.example.hoofit.data;

public class Interesting {
    private ItemType type;
    private String name;
    private String description;
    private String uri;

    public Interesting(ItemType type, String name, String description, String uri) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.uri = uri;
    }

    public Interesting() {
    }

    public Interesting(ItemType type, String name, String description) {
        this.type = type;
        this.name = name;
        this.description = description;
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
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
