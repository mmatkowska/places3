package com.example.malgorzata.placessample.components.fragment;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.malgorzata.placessample.R;
import com.example.malgorzata.placessample.components.model.MyPlace;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.example.malgorzata.placessample.components.activity.*;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapFragment extends com.google.android.gms.maps.SupportMapFragment {

    private FloatingActionButton mFullScreenFAB;
    private FloatingActionButton mCurrentPositionFAB;

    private EditText mSearchField;
    private FloatingActionButton mSearchFAB;
    private FloatingActionButton mNearbyFAB;

    private OnFullScreenClickListener mOnFullScreenClickListener;
    private OnCurrentPositionClickListener mOnCurrentPositionClickListener;
    private OnSearchClickListener mOnSearchClickListener;
    private OnNearbyPlacesClickListener mOnNearbyPlacesClickListener;

    private boolean mIsFullScreen = true;
    private boolean mIsNearbyClicked = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View map = super.onCreateView(inflater, container, savedInstanceState);
        FrameLayout view = ((FrameLayout) inflater.inflate(R.layout.map_fragment, container, false));
        view.addView(map, 0);
        mFullScreenFAB = ((FloatingActionButton) view.findViewById(R.id.fullscreen_button));
        mCurrentPositionFAB = ((FloatingActionButton) view.findViewById(R.id.current_position_button));
        mSearchFAB = ((FloatingActionButton) view.findViewById(R.id.search_button));
        mNearbyFAB = (FloatingActionButton) view.findViewById(R.id.nearable_places_button);
        mSearchField = ((EditText) view.findViewById(R.id.search_field));
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final GoogleApiClientCallbacks googleApiClientCallbacks = (GoogleApiClientCallbacks) getActivity();

        mFullScreenFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsFullScreen = !mIsFullScreen;
                mOnFullScreenClickListener.onFullScreenClick(mIsFullScreen);
            }
        });

        mCurrentPositionFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        mNearbyFAB.setVisibility(View.VISIBLE);
                        googleMap.setMyLocationEnabled(true);

                        LatLng latLng = googleApiClientCallbacks.getCurrentLocation();

                        CameraUpdate center = CameraUpdateFactory.newLatLng(latLng);
                        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);

                        googleMap.moveCamera(center);
                        googleMap.animateCamera(zoom);

                        mOnCurrentPositionClickListener.onCurrentPositionClick();
                    }
                });
            }
        });

        mSearchFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsFullScreen = false;
                try {
                    Geocoder geoCoder = new Geocoder(getActivity(), Locale.getDefault());
                    List<Address> addresses = geoCoder.getFromLocationName(mSearchField.getText().toString(), 1);

                    if (!addresses.isEmpty()) {
                        Address address = addresses.get(0);
                        final LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                        getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap googleMap) {
                                CameraUpdate center = CameraUpdateFactory.newLatLng(latLng);
                                CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);

                                googleMap.moveCamera(center);
                                googleMap.animateCamera(zoom);
                                googleMap.addMarker(new MarkerOptions().position(latLng)
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                    @Override
                                    public boolean onMarkerClick(Marker marker) {
                                        marker.remove();
                                        return false;
                                    }
                                });
                            }
                        });

                        mOnSearchClickListener.onSearchClick(latLng);
                    } else {
                        Toast.makeText(getActivity(), "Address not found!", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        mNearbyFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsNearbyClicked = !mIsNearbyClicked;
                mOnNearbyPlacesClickListener.onNearbyPlacesClick(mIsNearbyClicked);
            }
        });
    }

    public void setOnFullScreenClickListener(OnFullScreenClickListener onFullScreenClickListener) {
        mOnFullScreenClickListener = onFullScreenClickListener;
    }

    public void setOnCurrentPositionClickListener(OnCurrentPositionClickListener onCurrentPositionClickListener) {
        mOnCurrentPositionClickListener = onCurrentPositionClickListener;
    }

    public void setOnSearchClickListener(OnSearchClickListener onSearchClickListener) {
        mOnSearchClickListener = onSearchClickListener;
    }

    public void setOnNearbyPlacesClickListener(OnNearbyPlacesClickListener onNearbyPlacesClickListener) {
        mOnNearbyPlacesClickListener = onNearbyPlacesClickListener;
    }

    public void showChosedPlaceOnMapView(final LatLng latLng) {
        if (latLng != null) {
            getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    CameraUpdate center = CameraUpdateFactory.newLatLng(latLng);
                    CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);

                    googleMap.moveCamera(center);
                    googleMap.animateCamera(zoom);
                    googleMap.addMarker(new MarkerOptions().position(latLng)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                    googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            marker.remove();
                            return false;
                        }
                    });
                }
            });
        }
    }

    public void showNearbyPlacesOnMapView(final List<MyPlace> places) {
        getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                if (places != null) {
                    for (MyPlace place : places) {
                        LatLng latLng = new LatLng(place.getGeometry().getLocation().getLat(),
                                place.getGeometry().getLocation().getLng());
                        googleMap.addMarker(new MarkerOptions().position(latLng)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)))
                                .setTitle(place.getName());
                    }
                }
            }
        });
    }

    public interface OnFullScreenClickListener {
        void onFullScreenClick(boolean isFullScreen);
    }

    public interface OnCurrentPositionClickListener {
        void onCurrentPositionClick();
    }

    public interface OnSearchClickListener {
        void onSearchClick(LatLng latLng);
    }

    public interface OnNearbyPlacesClickListener {
        void onNearbyPlacesClick(boolean isNearbyClicked);
    }
}