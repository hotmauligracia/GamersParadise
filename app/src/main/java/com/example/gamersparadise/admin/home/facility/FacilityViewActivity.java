package com.example.gamersparadise.admin.home.facility;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gamersparadise.R;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.Arrays;

public class FacilityViewActivity extends AppCompatActivity {

    private View illustLokasi, deletionConfirmationPopup;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility_view);

        illustLokasi = findViewById(R.id.illust_lokasi);
        deletionConfirmationPopup = findViewById(R.id.deletion_confirmation_popup);
        TextView tvConfirmTitle = findViewById(R.id.tv_confirm_title);
        TextView tvConfirmMessage = findViewById(R.id.tv_confirm_message);

        tvConfirmTitle.setText("Yakin Hapus Fasilitas?");
        tvConfirmMessage.setText("Fasilitas yang sudah dihapus tidak dapat dikembalikan lagi");

        MaterialToolbar topAppBar = findViewById(R.id.top_app_bar);
        setSupportActionBar(topAppBar);
        getSupportActionBar().setTitle(null);

        ImageView backButton = findViewById(R.id.toolbar_back_icon);
        backButton.setOnClickListener(v -> onBackPressed());

        String[] arrayLokasi = getResources().getStringArray(R.array.lokasi);

        ArrayList<String> lokasi = new ArrayList<>();
        lokasi.add("Pilih Lokasi");
        lokasi.addAll(Arrays.asList(arrayLokasi));

        Spinner spinnerLokasi = findViewById(R.id.spinner_lokasi);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, lokasi) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLokasi.setAdapter(adapter);
        spinnerLokasi.setSelection(0);

        spinnerLokasi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    String selectedLocation = (String) parent.getItemAtPosition(position);
                    // Handle the selected location
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case where nothing is selected if needed
            }
        });
    }
}