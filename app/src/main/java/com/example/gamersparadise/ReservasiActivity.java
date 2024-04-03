package com.example.gamersparadise;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

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
}