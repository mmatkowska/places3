package com.example.malgorzata.placessample.components.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;

import com.example.malgorzata.placessample.R;
import com.example.malgorzata.placessample.components.api.PlacesService;
import com.example.malgorzata.placessample.components.model.MyPlace;
import com.example.malgorzata.placessample.components.model.PlaceWrapper;
import com.example.malgorzata.placessample.components.adapter.*;
import com.google.android.gms.maps.model.LatLng;

public class NearbyPlacesFragment extends Fragment {

    private PlacesService mPlacesService;

    private RecyclerView mPlacesRecycler;
    private PlacesAdapter mAdapter;

    private OnAdapterDelivered mOnAdapterDelivered;

    private List<MyPlace> mPlaces;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createPlacesService();
    }

    private void createPlacesService() {
        String BASE_URL = "https://maps.googleapis.com";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mPlacesService = retrofit.create(PlacesService.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nearby_places_fragment, container, false);
        mPlacesRecycler = ((RecyclerView) view.findViewById(R.id.places_recycler));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initPlacesRecycler();
    }

    private void initPlacesRecycler() {
        mPlacesRecycler.setHasFixedSize(true);
        mPlacesRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    public void loadPlaces(String location) {
        PlacesLoader placesLoader = new PlacesLoader();
        placesLoader.execute(location, getString(R.string.browser_key));
    }

    public List<MyPlace> getPlaces() {
        return mPlaces;
    }

    private class PlacesLoader extends AsyncTask<String, Void, List<MyPlace>> {

        @Override
        protected List<MyPlace> doInBackground(String... params) {
            String location = params[0];
            String apiKey = params[1];
            Call<PlaceWrapper> listCall = mPlacesService.getPlaceWrapper(location, "500", apiKey);
            try {
                PlaceWrapper placeWrapper = listCall.execute().body();
                mPlaces = placeWrapper.getPlaces();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return mPlaces;
        }

        @Override
        protected void onPostExecute(final List<MyPlace> places) {
            super.onPostExecute(places);
            mAdapter = new PlacesAdapter(getActivity(), places);
            mPlacesRecycler.setAdapter(mAdapter);
            mAdapter.setOnGoToPlaceButtonClickListener(new PlacesAdapter.OnGoToPlaceButtonClickListener() {
                @Override
                public void onGoToPlaceButtonClick(LatLng latLng) {
                    if (latLng != null) {
                        mOnAdapterDelivered.onAdapterDelivered(latLng);
                    }
                }
            });
        }
    }

    public void setOnAdapterDelivered(OnAdapterDelivered onAdapterDelivered) {
        mOnAdapterDelivered = onAdapterDelivered;
    }

    public interface OnAdapterDelivered {
        public void onAdapterDelivered(LatLng latLng);
    }
}