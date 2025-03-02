package com.tvoyhod.hoofit.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * The type Reserve.
 */
public class Reserve implements Serializable, Parcelable {
    private String id;
    private String name;
    private String description;
    private List<Trail> trails;

    /**
     * Instantiates a new Reserve.
     *
     * @param id          the id
     * @param name        the name
     * @param description the description
     * @param trails      the trails
     */
    public Reserve(String id, String name, String description, List<Trail> trails) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.trails = trails;
    }

    /**
     * Instantiates a new Reserve.
     */
    public Reserve() {
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
     * Gets trails.
     *
     * @return the trails
     */
    public List<Trail> getTrails() {
        return trails;
    }

    /**
     * Sets trails.
     *
     * @param trails the trails
     */
    public void setTrails(List<Trail> trails) {
        this.trails = trails;
    }
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {

    }
}

