package com.example.gamersparadise.admin.home.location;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gamersparadise.R;

public class LocationViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_view);

        Button btnLokasiBaru = findViewById(R.id.btn_lokasi_baru);
        btnLokasiBaru.setOnClickListener(v -> {
            Intent createLocation = new Intent(LocationViewActivity.this, LocationViewFormActivity.class);
            startActivity(createLocation);
        });
    }
}