package com.example.gamersparadise.admin.home.location;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamersparadise.R;
import com.google.android.material.appbar.MaterialToolbar;

public class LocationViewActivity extends AppCompatActivity {

    private RecyclerView rvLocation;
    private ConstraintLayout illustLokasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_view);

        MaterialToolbar topAppBar = findViewById(R.id.top_app_bar);
        setSupportActionBar(topAppBar);

        getSupportActionBar().setTitle(null);

        ImageView backButton = findViewById(R.id.toolbar_back_icon);
        backButton.setOnClickListener(v -> onBackPressed());

        Button btnLokasiBaru = findViewById(R.id.btn_lokasi_baru);
        btnLokasiBaru.setOnClickListener(v -> {
            Intent createLocation = new Intent(LocationViewActivity.this, LocationViewFormActivity.class);
            startActivity(createLocation);
        });
    }
}