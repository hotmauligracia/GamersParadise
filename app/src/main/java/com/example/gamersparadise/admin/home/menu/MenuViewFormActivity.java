package com.example.gamersparadise.admin.home.menu;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gamersparadise.R;

public class MenuViewFormActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_view_form);

        Spinner spinnerTipeMenu = findViewById(R.id.spinnerTipeMenu);
        String[] tipe_menu = getResources().getStringArray(R.array.tipeMenu);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, tipe_menu);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipeMenu.setAdapter(adapter);
    }
}