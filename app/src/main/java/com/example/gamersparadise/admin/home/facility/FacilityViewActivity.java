package com.example.gamersparadise.admin.home.facility;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gamersparadise.R;

public class FacilityViewActivity extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility_view);

        Spinner spinnerFasilitas = findViewById(R.id.spinnerLokasi);
        String[] lokasi = getResources().getStringArray(R.array.Lokasi);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, lokasi);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFasilitas.setAdapter(adapter);

    }
}