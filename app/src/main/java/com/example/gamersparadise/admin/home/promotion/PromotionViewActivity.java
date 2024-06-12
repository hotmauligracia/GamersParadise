package com.example.gamersparadise.admin.home.promotion;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gamersparadise.R;

public class PromotionViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion_view);

        Button btnPromoBaru = findViewById(R.id.btn_save);
        btnPromoBaru.setOnClickListener(v -> {
            Intent createPromotion = new Intent(PromotionViewActivity.this, PromotionViewFormActivity.class);
            startActivity(createPromotion);
        });
    }
}