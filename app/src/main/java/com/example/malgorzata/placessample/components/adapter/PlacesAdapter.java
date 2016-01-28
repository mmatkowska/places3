package com.example.malgorzata.placessample.components.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.malgorzata.placessample.R;
import com.example.malgorzata.placessample.components.PlaceActivity;
import com.example.malgorzata.placessample.components.model.MyPlace;
import com.google.android.gms.maps.model.LatLng;
import com.example.malgorzata.placessample.components.utils.Utils;

import java.util.List;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.ViewHolder> {

    private Context mContext;
    private List<MyPlace> mPlaces;
    private OnGoToPlaceButtonClickListener mOnGoToPlaceButtonClickListener;
    private final String NAME = "NAME";
    private final String VICINITY = "VICINITY";
    private final String PHOTO_REFERENCE = "PHOTO_REFERENCE";

    public PlacesAdapter(Context context, List<MyPlace> places) {
        mContext = context;
        mPlaces = places;
    }

    @Override
    public PlacesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.nearby_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PlacesAdapter.ViewHolder holder, final int position) {
        MyPlace myPlace = mPlaces.get(position);
        Float lat = mPlaces.get(position).getGeometry().getLocation().getLat();
        Float lng = mPlaces.get(position).getGeometry().getLocation().getLng();
        final LatLng latLng = new LatLng(lat, lng);

        holder.mName.setText(myPlace.getName());
        holder.mVicinity.setText(myPlace.getVicinity());

        if (myPlace.getPhotos() != null && !myPlace.getPhotos().isEmpty()) {
            Utils.setPhoto(mContext, myPlace.getPhotos().get(0).getPhotoReference(), holder.mImage);
        } else {
            Utils.setPlaceholderPhoto(holder.mImage);
        }

        holder.mGoToPlaceFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    mOnGoToPlaceButtonClickListener.onGoToPlaceButtonClick(latLng);
            }
        });

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PlaceActivity.class);
                intent.putExtra(NAME, mPlaces.get(position).getName())
                        .putExtra(VICINITY, mPlaces.get(position).getVicinity());
                if (mPlaces.get(position).getPhotos() != null && !mPlaces.get(position).getPhotos().isEmpty()) {
                    intent.putExtra(PHOTO_REFERENCE, mPlaces.get(position).getPhotos().get(0).getPhotoReference());
                }
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPlaces.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView mName;
        private final TextView mVicinity;
        private final ImageView mImage;
        private final FloatingActionButton mGoToPlaceFAB;
        private final CardView mCardView;

        public ViewHolder(View v) {
            super(v);
            mName = ((TextView) v.findViewById(R.id.name));
            mVicinity = ((TextView) v.findViewById(R.id.vicinity));
            mImage = ((ImageView) v.findViewById(R.id.image));
            mGoToPlaceFAB = ((FloatingActionButton) v.findViewById(R.id.goto_button));
            mCardView = (CardView) v.findViewById(R.id.card_view);
        }
    }

    public void setOnGoToPlaceButtonClickListener(OnGoToPlaceButtonClickListener onGoToPlaceButtonClickListener) {
        mOnGoToPlaceButtonClickListener = onGoToPlaceButtonClickListener;
    }

    public interface OnGoToPlaceButtonClickListener {
        public void onGoToPlaceButtonClick(LatLng latLng);
    }
}