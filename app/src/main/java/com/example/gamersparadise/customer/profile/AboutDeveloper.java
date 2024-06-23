package com.example.gamersparadise.customer.profile;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gamersparadise.R;

public class AboutDeveloper extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_developer);

        ImageView backButton = findViewById(R.id.toolbar_back_icon);
        backButton.setOnClickListener(v -> onBackPressed());
    }
}
