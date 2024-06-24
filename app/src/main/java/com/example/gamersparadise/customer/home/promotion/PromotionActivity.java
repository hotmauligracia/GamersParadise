package com.example.gamersparadise.customer.home.promotion;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.gamersparadise.R;
import com.example.gamersparadise.data.Promotion;
import com.google.android.material.appbar.MaterialToolbar;

public class PromotionActivity extends AppCompatActivity {

    private static final String TAG = "PromotionActivity";

    private ImageView imgPromotion;
    private TextView tvPromotionName, tvPromotionDesc, tvPromotionTnc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion);

        MaterialToolbar topAppBar = findViewById(R.id.top_app_bar);
        ImageView backButton = findViewById(R.id.toolbar_back_icon);
        Button btnToUsePromo = findViewById(R.id.btn_to_use_promo);

        imgPromotion = findViewById(R.id.img_promotion);
        tvPromotionName = findViewById(R.id.tv_promotion_name);
        tvPromotionDesc = findViewById(R.id.tv_promotion_desc);
        tvPromotionTnc = findViewById(R.id.tv_promotion_tnc);

        setSupportActionBar(topAppBar);
        getSupportActionBar().setTitle(null);
        backButton.setOnClickListener(v -> onBackPressed());

        Intent intent = getIntent();

        if (intent.hasExtra("promotion")) {
            Promotion promotion = intent.getParcelableExtra("promotion");
            if (promotion != null) {
                displayPromotionDetails(promotion);
            } else {
                Log.e(TAG, "Promotion extra is null");
            }
        } else {
            Log.e(TAG, "Intent or promotion extra is null");
        }
    }

    private void displayPromotionDetails(Promotion promotion) {
        Glide.with(this).load(promotion.getImageUrl()).into(imgPromotion);
        tvPromotionName.setText(promotion.getName());
        tvPromotionDesc.setText(promotion.getDescription());
        String termsAndConditions = "1. Mendapatkan diskon sebesar " + promotion.getFormattedNominal() +
                "\n2. Minimum order: " + promotion.getFormattedMinimumOrder() +
                "\n3. Berlaku dari " + promotion.getStartTime() + " s.d. " + promotion.getEndTime();
        tvPromotionTnc.setText(termsAndConditions);
    }
}