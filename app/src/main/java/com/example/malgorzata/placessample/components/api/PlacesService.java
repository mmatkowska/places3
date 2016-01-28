package com.example.malgorzata.placessample.components.api;

import com.example.malgorzata.placessample.components.model.PlaceWrapper;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PlacesService {

    @GET("/maps/api/place/nearbysearch/json?")
    Call<PlaceWrapper> getPlaceWrapper(@Query("location") String location,
                                       @Query("radius") String radius,
                                       @Query("key") String api_key);
}