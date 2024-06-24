package com.example.gamersparadise.customer.home.facility;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamersparadise.Authentication;
import com.example.gamersparadise.R;
import com.example.gamersparadise.customer.home.facility.adapter.FacilityAdapter;
import com.example.gamersparadise.data.Facility;
import com.example.gamersparadise.data.Location;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FacilityActivity extends AppCompatActivity {

    private static final String TAG = "FacilityActivity";

    private Authentication auth;
    private List<Location> locationList;
    private List<Facility> facilityList;
    private List<String> locationNames;
    private ArrayAdapter<String> spinnerAdapter;
    private String locationId;
    private FacilityAdapter adapter;
    private RecyclerView rvFacility;
    private View illustFasilitas;
    private Spinner spinnerLocationName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility);

        auth = new Authentication();

        MaterialToolbar topAppBar = findViewById(R.id.top_app_bar);
        ImageView backButton = findViewById(R.id.toolbar_back_icon);

        rvFacility = findViewById(R.id.rv_facility);
        illustFasilitas = findViewById(R.id.illust_fasilitas);
        spinnerLocationName = findViewById(R.id.spinner_location_name);

        locationList = new ArrayList<>();
        facilityList = new ArrayList<>();

        setSupportActionBar(topAppBar);
        getSupportActionBar().setTitle(null);
        backButton.setOnClickListener(v -> onBackPressed());

        Intent intent = getIntent();

        if (intent.hasExtra("locationId")) {
            locationId = intent.getStringExtra("locationId");
            if (locationId == null) {
                Log.e(TAG, "LocationId extra is null");
            }
        } else {
            Log.e(TAG, "Intent or locationId extra is null");
        }

        locationNames = new ArrayList<>();
        locationNames.add("Pilih Lokasi");

        spinnerAdapter = new ArrayAdapter<String>(
                this, R.layout.custom_location_spinner_item, locationNames) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }
        };
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLocationName.setAdapter(spinnerAdapter);
        spinnerLocationName.setSelection(0);

        spinnerLocationName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    locationId = locationList.get(position - 1).getId();
                    fetchFacilityData(locationId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        adapter = new FacilityAdapter(this, facilityList);
        rvFacility.setLayoutManager(new LinearLayoutManager(this));
        rvFacility.setAdapter(adapter);

        fetchLocationData();
    }

    private void fetchLocationData() {
        auth.fetchCollectionData("locations", new Authentication.FirebaseCollectionCallback() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                locationList.clear();
                for (QueryDocumentSnapshot document : querySnapshot) {
                    Location location = document.toObject(Location.class);
                    location.setId(document.getId());
                    locationList.add(location);
                }
                updateLocationSpinner();
                if (locationId != null) {
                    setSpinnerToLocationId(locationId);
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(FacilityActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateLocationSpinner() {
        for (Location location : locationList) {
            locationNames.add(location.getName());
        }
        spinnerAdapter.notifyDataSetChanged();
    }

    private void setSpinnerToLocationId(String locationId) {
        for (int i = 0; i < locationList.size(); i++) {
            if (locationList.get(i).getId().equals(locationId)) {
                spinnerLocationName.setSelection(i + 1);
                break;
            }
        }
    }

    private void fetchFacilityData(String locationId) {
        auth.fetchCollectionData("locations/" + locationId + "/facilities", new Authentication.FirebaseCollectionCallback() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                facilityList.clear();
                for (QueryDocumentSnapshot document : querySnapshot) {
                    Facility facility = document.toObject(Facility.class);
                    facility.setId(document.getId());
                    facilityList.add(facility);
                }
                adapter.notifyDataSetChanged();
                updateVisibility();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(FacilityActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateVisibility() {
        if (facilityList.isEmpty()) {
            illustFasilitas.setVisibility(View.VISIBLE);
            rvFacility.setVisibility(View.GONE);
        } else {
            illustFasilitas.setVisibility(View.GONE);
            rvFacility.setVisibility(View.VISIBLE);
        }
    }
}