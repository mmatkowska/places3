package com.example.malgorzata.placessample.components.utils;

import android.content.Context;
import android.widget.ImageView;

import com.example.malgorzata.placessample.R;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

public class Utils {

    public static String getLocationString(LatLng location) {
        return String.format("%f,%f",
                location.latitude,
                location.longitude);
    }

    public static void setPhoto(Context context, String photoReference, ImageView image) {
        String apiKey = context.getString(R.string.browser_key);
        String photoURL = String.format("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=%s&key=%s", photoReference, apiKey);
        Picasso.with(context).load(photoURL).into(image);
    }

    public static void setPlaceholderPhoto(ImageView image) {
        image.setImageResource(R.drawable.no_image_placeholder);
    }
}