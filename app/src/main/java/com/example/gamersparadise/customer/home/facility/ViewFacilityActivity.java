package com.example.gamersparadise.customer.home.facility;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.gamersparadise.R;
import com.example.gamersparadise.data.Facility;
import com.google.android.material.appbar.MaterialToolbar;

public class ViewFacilityActivity extends AppCompatActivity {

    private static final String TAG = "ViewFacilityActivity";

    private ImageView imgViewFacility;
    private TextView tvViewFacilityName, tvViewFacilityCapacity, tvViewFacilityPrice, tvViewFacilityDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_facility);
      
        MaterialToolbar topAppBar = findViewById(R.id.top_app_bar);
        ImageView backButton = findViewById(R.id.toolbar_back_icon);
        Button btnToReserve = findViewById(R.id.btn_to_reserve);

        imgViewFacility = findViewById(R.id.img_view_facility);
        tvViewFacilityName = findViewById(R.id.tv_view_facility_name);
        tvViewFacilityCapacity = findViewById(R.id.tv_view_facility_capacity);
        tvViewFacilityPrice = findViewById(R.id.tv_view_facility_price);
        tvViewFacilityDetails = findViewById(R.id.tv_view_facility_details);

        setSupportActionBar(topAppBar);
        getSupportActionBar().setTitle(null);
        backButton.setOnClickListener(v -> onBackPressed());

        Intent intent = getIntent();

        if (intent.hasExtra("facility")) {
            Facility facility = intent.getParcelableExtra("facility");
            if (facility != null) {
                displayFacilityDetails(facility);
            } else {
                Log.e(TAG, "Facility extra is null");
            }
        } else {
            Log.e(TAG, "Intent or facility extra is null");
        }

        btnToReserve.setOnClickListener(v -> {
            Intent reserveActivity = new Intent(ViewFacilityActivity.this, ReserveActivity.class);
            startActivity(reserveActivity);
        });
    }

    private void displayFacilityDetails(Facility facility) {
        Glide.with(this).load(facility.getImageUrl()).into(imgViewFacility);
        tvViewFacilityName.setText(facility.getName());
        tvViewFacilityCapacity.setText(String.format("%s orang", facility.getCapacity()));
        tvViewFacilityPrice.setText(String.format("%s/jam", facility.getFormattedPrice()));
        tvViewFacilityDetails.setText(facility.getDetails());
    }
}