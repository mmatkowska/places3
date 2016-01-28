package com.example.malgorzata.placessample.components;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.malgorzata.placessample.R;
import com.example.malgorzata.placessample.components.utils.Utils;

public class PlaceActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);

        ImageView imageView = ((ImageView) findViewById(R.id.image_view));
        ImageView backButton = (ImageView) findViewById(R.id.back_button);
        TextView nameView = ((TextView) findViewById(R.id.name));
        TextView vicinityView = ((TextView) findViewById(R.id.vicinity));
        TextView infoView = ((TextView) findViewById(R.id.info));

        Intent intent = getIntent();
        String name = intent.getStringExtra("NAME");
        String vicinity = intent.getStringExtra("VICINITY");
        String photoReference = intent.getStringExtra("PHOTO_REFERENCE");
        if (photoReference != null) {
            Utils.setPhoto(this, photoReference, imageView);
        } else {
            Utils.setPlaceholderPhoto(imageView);
        }

        nameView.setText(name);
        vicinityView.setText(vicinity);
        infoView.setText("Jakieś info o tym miejscu");

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaceActivity.this.finish();
            }
        });
    }
}
