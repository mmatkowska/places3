package com.example.malgorzata.placessample.components.model;

import com.google.gson.annotations.SerializedName;

public class Photo {

    @SerializedName("photo_reference") private String mPhotoReference;

    public String getPhotoReference() {
        return mPhotoReference;
    }
}