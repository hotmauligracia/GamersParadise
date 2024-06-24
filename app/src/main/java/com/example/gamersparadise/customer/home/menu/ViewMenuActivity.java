package com.example.gamersparadise.customer.home.menu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.gamersparadise.R;
import com.example.gamersparadise.data.Menu;
import com.google.android.material.appbar.MaterialToolbar;

public class ViewMenuActivity extends AppCompatActivity {

    private static final String TAG = "ViewMenuActivity";

    private ImageView imgViewMenu;
    private TextView tvViewMenuName, tvViewMenuDesc, tvViewMenuPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_menu);

        MaterialToolbar topAppBar = findViewById(R.id.top_app_bar);
        ImageView backButton = findViewById(R.id.toolbar_back_icon);
        Button btnToCart = findViewById(R.id.btn_to_cart);

        imgViewMenu = findViewById(R.id.img_view_menu);
        tvViewMenuName = findViewById(R.id.tv_view_menu_name);
        tvViewMenuDesc = findViewById(R.id.tv_view_menu_desc);
        tvViewMenuPrice = findViewById(R.id.tv_view_menu_price);

        setSupportActionBar(topAppBar);
        getSupportActionBar().setTitle(null);
        backButton.setOnClickListener(v -> onBackPressed());

        Intent intent = getIntent();

        if (intent.hasExtra("menu")) {
            Menu menu = intent.getParcelableExtra("menu");
            if (menu != null) {
                displayMenuDetails(menu);
            } else {
                Log.e(TAG, "Menu extra is null");
            }
        } else {
            Log.e(TAG, "Intent or menu extra is null");
        }
    }

    private void displayMenuDetails(Menu menu) {
        Glide.with(this).load(menu.getImageUrl()).into(imgViewMenu);
        tvViewMenuName.setText(menu.getName());
        tvViewMenuDesc.setText(menu.getDescription());
        tvViewMenuPrice.setText(menu.getFormattedPrice());
    }
}