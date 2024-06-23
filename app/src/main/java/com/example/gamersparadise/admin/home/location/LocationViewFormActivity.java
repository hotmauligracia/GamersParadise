package com.example.gamersparadise.admin.home.location;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gamersparadise.Authentication;
import com.example.gamersparadise.R;
import com.example.gamersparadise.data.Location;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class LocationViewFormActivity extends AppCompatActivity {

    private static final String TAG = "LocationViewFormActivity";

    private EditText edtLocationName, edtLocationProvince, edtLocationCity, edtLocationSubdistrict, edtLocationZipCode, edtLocationStreet;
    private Authentication auth;
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_view_form);

        auth = new Authentication();

        edtLocationName = findViewById(R.id.edt_location_name);
        edtLocationProvince = findViewById(R.id.edt_location_province);
        edtLocationCity = findViewById(R.id.edt_location_city);
        edtLocationSubdistrict = findViewById(R.id.edt_location_subdistrict);
        edtLocationZipCode = findViewById(R.id.edt_location_zip_code);
        edtLocationStreet = findViewById(R.id.edt_location_street);
        Button btnSave = findViewById(R.id.btn_save);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("location")) {
            location = intent.getParcelableExtra("location");
            if (location != null) {
                populateForm(location);
            } else {
                Log.e(TAG, "Location extra is null");
            }
        } else {
            Log.e(TAG, "Intent or location extra is null");
        }

        MaterialToolbar topAppBar = findViewById(R.id.top_app_bar);
        setSupportActionBar(topAppBar);
        getSupportActionBar().setTitle(null);

        ImageView backButton = findViewById(R.id.toolbar_back_icon);
        backButton.setOnClickListener(v -> onBackPressed());

        btnSave.setOnClickListener(v -> {
            if (validateInputs()) {
                if (location == null) {
                    createNewLocation();
                } else {
                    updateLocation();
                }
            }
        });
    }

    private void populateForm(Location location) {
        edtLocationName.setText(location.getName());
        edtLocationProvince.setText(location.getProvince());
        edtLocationCity.setText(location.getCity());
        edtLocationSubdistrict.setText(location.getSubdistrict());
        edtLocationZipCode.setText(location.getZipCode());
        edtLocationStreet.setText(location.getStreet());
    }

    private boolean validateInputs() {
        if (TextUtils.isEmpty(edtLocationName.getText().toString()) ||
                TextUtils.isEmpty(edtLocationProvince.getText().toString()) ||
                TextUtils.isEmpty(edtLocationCity.getText().toString()) ||
                TextUtils.isEmpty(edtLocationSubdistrict.getText().toString()) ||
                TextUtils.isEmpty(edtLocationZipCode.getText().toString()) ||
                TextUtils.isEmpty(edtLocationStreet.getText().toString())) {
            Toast.makeText(this, R.string.error_field_kosong, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void createNewLocation() {
        Map<String, Object> data = new HashMap<>();
        data.put("name", edtLocationName.getText().toString());
        data.put("province", edtLocationProvince.getText().toString());
        data.put("city", edtLocationCity.getText().toString());
        data.put("subdistrict", edtLocationSubdistrict.getText().toString());
        data.put("zipCode", edtLocationZipCode.getText().toString());
        data.put("street", edtLocationStreet.getText().toString());

        auth.addDocumentData("locations", data, new Authentication.FirebaseDocumentAddCallback() {
            @Override
            public void onSuccess(String documentId) {
                Toast.makeText(LocationViewFormActivity.this, "Data lokasi ditambahkan", Toast.LENGTH_SHORT).show();
                setResult(Activity.RESULT_OK);
                finish();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(LocationViewFormActivity.this, "Gagal menambahkan data lokasi: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateLocation() {
        Map<String, Object> data = new HashMap<>();
        data.put("name", edtLocationName.getText().toString());
        data.put("province", edtLocationProvince.getText().toString());
        data.put("city", edtLocationCity.getText().toString());
        data.put("subdistrict", edtLocationSubdistrict.getText().toString());
        data.put("zipCode", edtLocationZipCode.getText().toString());
        data.put("street", edtLocationStreet.getText().toString());

        auth.updateDocumentData("locations", location.getId(), data, new Authentication.FirebaseDocumentCallback() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Toast.makeText(LocationViewFormActivity.this, "Data lokasi diperbarui", Toast.LENGTH_SHORT).show();
                setResult(Activity.RESULT_OK);
                finish();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(LocationViewFormActivity.this, "Gagal memperbarui data lokasi: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}