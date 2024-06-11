package com.example.gamersparadise.admin.home.menu;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gamersparadise.R;

public class MenuViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_view);

        Button btnMenuBaru = findViewById(R.id.btn_menu_baru);
        btnMenuBaru.setOnClickListener(v -> {
            Intent createMenu = new Intent(MenuViewActivity.this, MenuViewFormActivity.class);
            startActivity(createMenu);
        });
    }
}