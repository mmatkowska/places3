package com.example.malgorzata.placessample.components.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyPlace {

    @SerializedName("name") private String mName;
    @SerializedName("geometry") private Geometry mGeometry;
    @SerializedName("icon") private String mIconURL;
    @SerializedName("vicinity") private String mVicinity;
    @SerializedName("photos") private List<Photo> mPhotos;

    public Geometry getGeometry() {
        return mGeometry;
    }

    public String getName() {
        return mName;
    }

    public List<Photo> getPhotos() {
        return mPhotos;
    }

    public String getIconURL() {
        return mIconURL;
    }

    public String getVicinity() {
        return mVicinity;
    }
}
