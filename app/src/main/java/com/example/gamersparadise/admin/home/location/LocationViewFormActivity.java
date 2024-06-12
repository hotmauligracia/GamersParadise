package com.example.gamersparadise.admin.home.location;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gamersparadise.Authentication;
import com.example.gamersparadise.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class LocationViewFormActivity extends AppCompatActivity {

    private EditText edtLocationName, edtLocationProvince, edtLocationCity, edtLocationSubdistrict, edtLocationZipCode, edtLocationStreet;
    private Button btnSave;
    private Authentication auth;

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
        btnSave = findViewById(R.id.btn_save);

        loadLocation();

        MaterialToolbar topAppBar = findViewById(R.id.top_app_bar);
        setSupportActionBar(topAppBar);

        getSupportActionBar().setTitle(null);

        ImageView backButton = findViewById(R.id.toolbar_back_icon);
        backButton.setOnClickListener(v -> onBackPressed());

        btnSave.setOnClickListener(v -> saveLocation());
    }

    private void loadLocation() {
        if (auth.getCurrentUser() != null) {
            auth.loadDocumentData("locations",
                    auth.getCurrentUser().getUid(),
                    new Authentication.FirebaseDocumentCallback()
                    {
                        @Override
                        public void onSuccess(DocumentSnapshot document) {
                            if (document != null) {
                                edtLocationName.setText(document.getString("name"));
                                edtLocationProvince.setText(document.getString("province"));
                                edtLocationCity.setText(document.getString("city"));
                                edtLocationSubdistrict.setText(document.getString("subdistrict"));
                                edtLocationZipCode.setText(document.getString("zipCode"));
                                edtLocationStreet.setText(document.getString("street"));
                            }
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            Toast.makeText(LocationViewFormActivity.this,
                                    errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void saveLocation() {
        String name = edtLocationName.getText().toString();
        String province = edtLocationProvince.getText().toString();
        String city = edtLocationCity.getText().toString();
        String subdistrict = edtLocationSubdistrict.getText().toString();
        String zipCode = edtLocationZipCode.getText().toString();
        String street = edtLocationStreet.getText().toString();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(province) ||
                TextUtils.isEmpty(city) || TextUtils.isEmpty(subdistrict) ||
                TextUtils.isEmpty(zipCode) || TextUtils.isEmpty(street)
        ) {
            Toast.makeText(this, R.string.error_field_kosong, Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> locationData = new HashMap<>();
        locationData.put("name", name);
        locationData.put("province", province);
        locationData.put("city", city);
        locationData.put("subdistrict", subdistrict);
        locationData.put("zipCode", zipCode);
        locationData.put("street", street);

        if (auth.getCurrentUser() != null) {
            auth.saveDocumentData("locations",
                    auth.getCurrentUser().getUid(),
                    locationData,
                    new Authentication.FirebaseDocumentCallback()
                    {
                        @Override
                        public void onSuccess(DocumentSnapshot document) {
                            Toast.makeText(LocationViewFormActivity.this, "Data lokasi berhasil disimpan.", Toast.LENGTH_SHORT).show();
                            setResult(Activity.RESULT_OK);
                            finish();
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            Toast.makeText(LocationViewFormActivity.this,
                                    errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}