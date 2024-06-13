package com.example.gamersparadise.admin.home.location;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamersparadise.Authentication;
import com.example.gamersparadise.R;
import com.example.gamersparadise.admin.home.location.adapter.LocationViewAdapter;
import com.example.gamersparadise.data.Location;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class LocationViewActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_NEW_LOCATION = 1;
    public static final int REQUEST_CODE_EDIT_LOCATION = 2;

    private RecyclerView rvLocation;
    private View illustLokasi;
    private LocationViewAdapter adapter;
    private List<Location> locationList;
    private Authentication auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_view);

        auth = new Authentication();

        rvLocation = findViewById(R.id.rv_location_view);
        illustLokasi = findViewById(R.id.illust_lokasi);
        rvLocation.setLayoutManager(new LinearLayoutManager(this));
        locationList = new ArrayList<>();
        adapter = new LocationViewAdapter(this, locationList, auth);
        rvLocation.setAdapter(adapter);
        Button btnNewLocation = findViewById(R.id.btn_new_location);

        MaterialToolbar topAppBar = findViewById(R.id.top_app_bar);
        setSupportActionBar(topAppBar);
        getSupportActionBar().setTitle(null);

        ImageView backButton = findViewById(R.id.toolbar_back_icon);
        backButton.setOnClickListener(v -> onBackPressed());

        btnNewLocation.setOnClickListener(v -> {
            Intent createLocation = new Intent(LocationViewActivity.this, LocationViewFormActivity.class);
            startActivityForResult(createLocation, REQUEST_CODE_NEW_LOCATION);
        });

        fetchLocationData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQUEST_CODE_NEW_LOCATION || requestCode == REQUEST_CODE_EDIT_LOCATION)
                && resultCode == RESULT_OK) {
            fetchLocationData();
        }
    }

    private void fetchLocationData() {
        locationList.clear();
        auth.fetchCollectionData("locations", new Authentication.FirebaseCollectionCallback() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                for (QueryDocumentSnapshot document : querySnapshot) {
                    Location location = document.toObject(Location.class);
                    location.setId(document.getId());
                    locationList.add(location);
                }
                adapter.notifyDataSetChanged();
                updateVisibility();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(LocationViewActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateVisibility() {
        if (locationList.isEmpty()) {
            illustLokasi.setVisibility(View.VISIBLE);
            rvLocation.setVisibility(View.GONE);
        } else {
            illustLokasi.setVisibility(View.GONE);
            rvLocation.setVisibility(View.VISIBLE);
        }
    }

    private void updateVisibility() {
        if (locationList.isEmpty()) {
            illustLokasi.setVisibility(View.VISIBLE);
            rvLocation.setVisibility(View.GONE);
        } else {
            illustLokasi.setVisibility(View.GONE);
            rvLocation.setVisibility(View.VISIBLE);
        }
    }
}