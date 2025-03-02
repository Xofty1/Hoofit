package com.tvoyhod.hoofit.data;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * The type Interesting.
 */
public class Interesting implements Serializable {
    private String id;
    private String type;
    private String name;
    private String description;
    private String uri;
    private Date date;
    private Trail trail;
    private Reserve reserve;

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(String id) {
        this.id = id;
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
     * Gets trail.
     *
     * @return the trail
     */
    public Trail getTrail() {
        return trail;
    }

    /**
     * Sets trail.
     *
     * @param trail the trail
     */
    public void setTrail(Trail trail) {
        this.trail = trail;
    }

    /**
     * Gets reserve.
     *
     * @return the reserve
     */
    public Reserve getReserve() {
        return reserve;
    }

    /**
     * Sets reserve.
     *
     * @param reserve the reserve
     */
    public void setReserve(Reserve reserve) {
        this.reserve = reserve;
    }

    /**
     * Instantiates a new Interesting.
     */
    public Interesting() {
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets type.
     *
     * @param type the type
     */
    public void setType(String type) {
        this.type = type;
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
     * Gets uri.
     *
     * @return the uri
     */
    public String getUri() {
        return uri;
    }

    /**
     * Sets uri.
     *
     * @param uri the uri
     */
    public void setUri(String uri) {
        this.uri = uri;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Interesting that = (Interesting) o;
        return Objects.equals(id, that.id);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
