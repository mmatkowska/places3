package com.example.malgorzata.placessample.components.activity;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.malgorzata.placessample.R;
import com.example.malgorzata.placessample.components.fragment.MapFragment;
import com.example.malgorzata.placessample.components.fragment.NearbyPlacesFragment;
import com.example.malgorzata.placessample.components.model.MyPlace;
import com.example.malgorzata.placessample.components.utils.Utils;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class MainActivity
        extends AppCompatActivity
        implements GoogleApiClientCallbacks {

    private GoogleApiClient mGoogleApiClient;

    private View mListContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListContainer = findViewById(R.id.list_container);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .build();
        }

        FragmentManager supportFragmentManager = getSupportFragmentManager();

        final MapFragment mapFragment;
        final NearbyPlacesFragment nearbyPlacesFragment;

        if (savedInstanceState != null) {
            mapFragment = ((MapFragment) supportFragmentManager.findFragmentById(R.id.map_container));
            nearbyPlacesFragment = ((NearbyPlacesFragment) supportFragmentManager.findFragmentById(R.id.list_container));
        } else {
            mapFragment = new MapFragment();
            nearbyPlacesFragment = new NearbyPlacesFragment();

            supportFragmentManager.beginTransaction()
                    .add(R.id.map_container, mapFragment)
                    .commit();

            supportFragmentManager.beginTransaction()
                    .add(R.id.list_container, nearbyPlacesFragment)
                    .commit();
        }

        mapFragment.setOnFullScreenClickListener(new MapFragment.OnFullScreenClickListener() {
            @Override
            public void onFullScreenClick(boolean isFullScreen) {
                mListContainer.setVisibility(isFullScreen ? View.GONE : View.VISIBLE);
            }
        });

        mapFragment.setOnCurrentPositionClickListener(new MapFragment.OnCurrentPositionClickListener() {
            @Override
            public void onCurrentPositionClick() {
                String location = Utils.getLocationString(getCurrentLocation());
                nearbyPlacesFragment.loadPlaces(location);
            }
        });

        mapFragment.setOnSearchClickListener(new MapFragment.OnSearchClickListener() {
            @Override
            public void onSearchClick(LatLng latLng) {
                String location = Utils.getLocationString(latLng);
                nearbyPlacesFragment.loadPlaces(location);
                mListContainer.setVisibility(View.VISIBLE);
            }
        });

        mapFragment.setOnNearbyPlacesClickListener(new MapFragment.OnNearbyPlacesClickListener() {
            @Override
            public void onNearbyPlacesClick(boolean isNearbyClicked) {
                List <MyPlace> places = nearbyPlacesFragment.getPlaces();
                if (isNearbyClicked) {
                    mListContainer.setVisibility(View.VISIBLE);
                    mapFragment.showNearbyPlacesOnMapView(places);
                } else {
                    mListContainer.setVisibility(View.GONE);
                }
            }
        });

        nearbyPlacesFragment.setOnAdapterDelivered(new NearbyPlacesFragment.OnAdapterDelivered() {
            @Override
            public void onAdapterDelivered(LatLng latLng) {
                mapFragment.showChosedPlaceOnMapView(latLng);
            }
        });
    }

    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public LatLng getCurrentLocation() {
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        return new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
    }
}