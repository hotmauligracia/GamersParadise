package com.example.gamersparadise.customer.home.facility;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gamersparadise.R;
import com.google.android.material.appbar.MaterialToolbar;

public class ReservasiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservasi);

        MaterialToolbar topAppBar = findViewById(R.id.top_app_bar);
        setSupportActionBar(topAppBar);

        getSupportActionBar().setTitle(null);

        ImageView backButton = findViewById(R.id.toolbar_back_icon);
        backButton.setOnClickListener(v -> onBackPressed());
    }

    public void onFacilityItemClick(View view) {
        Intent detailFacility = new Intent(ReservasiActivity.this, ViewFacilityActivity.class);
        startActivity(detailFacility);
    }
}