package com.example.malgorzata.placessample.components.model;

import com.google.gson.annotations.SerializedName;

public class PlaceLocation {

    @SerializedName("lat") private float mLat;
    @SerializedName("lng") private float mLng;

    public float getLat() {
        return mLat;
    }

    public float getLng() {
        return mLng;
    }
}
