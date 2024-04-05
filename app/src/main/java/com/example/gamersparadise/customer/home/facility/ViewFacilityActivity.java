package com.example.gamersparadise.customer.home.facility;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gamersparadise.R;

public class ViewFacilityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_facility);

        Button btnPesanFasilitas = findViewById(R.id.btn_pesan_fasilitas);
        btnPesanFasilitas.setOnClickListener(v -> {
            Intent reservasi2Activity = new Intent(ViewFacilityActivity.this, ReserveActivity.class);
            startActivity(reservasi2Activity);
        });
    }
}