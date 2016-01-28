package com.example.malgorzata.placessample.components.model;

import com.google.gson.annotations.SerializedName;

public class Geometry {

    @SerializedName("location") private PlaceLocation mLocation;

    public PlaceLocation getLocation() {
        return mLocation;
    }
}
