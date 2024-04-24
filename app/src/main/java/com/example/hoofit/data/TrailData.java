package com.example.hoofit.data;
import com.google.gson.annotations.SerializedName;

import java.util.List;
public class TrailData {
    @SerializedName("time")
    private String time;

    @SerializedName("trails")
    private List<Trail> trails;

    public TrailData() {
    }

    public TrailData(String time, List<Trail> trails) {
        this.time = time;
        this.trails = trails;
    }

    public String getTime() {
        return time;
    }

    public List<Trail> getTrails() {
        return trails;
    }

    @Override
    public String toString() {
        return "TrailData{" +
                "time='" + time + '\'' +
                ", trails=" + trails +
                '}';
    }
}