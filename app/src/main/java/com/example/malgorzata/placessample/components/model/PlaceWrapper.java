package com.example.malgorzata.placessample.components.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlaceWrapper {

    @SerializedName("results") private List<MyPlace> mMyPlaces;

    public List<MyPlace> getPlaces() {
        return mMyPlaces;
    }
}
