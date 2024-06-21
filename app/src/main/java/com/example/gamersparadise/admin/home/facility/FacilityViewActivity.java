package com.example.gamersparadise.admin.home.facility;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamersparadise.Authentication;
import com.example.gamersparadise.R;
import com.example.gamersparadise.admin.home.facility.adapter.FacilityViewAdapter;
import com.example.gamersparadise.data.Facility;
import com.example.gamersparadise.data.Location;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class FacilityViewActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_NEW_FACILITY = 1;
    public static final int REQUEST_CODE_EDIT_FACILITY = 2;

    private RecyclerView rvFacilityView;
    private FacilityViewAdapter adapter;
    private List<Location> locationList;
    private List<Facility> facilityList;
    private Authentication auth;
    private ArrayAdapter<String> spinnerAdapter;
    private Spinner spinnerLokasi;
    private View illustFasilitas, deletionConfirmationPopup;
    private Button btnNewFacility;
    private String locationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility_view);

        auth = new Authentication();

        MaterialToolbar topAppBar = findViewById(R.id.top_app_bar);
        ImageView backButton = findViewById(R.id.toolbar_back_icon);
        TextView tvConfirmTitle = findViewById(R.id.tv_confirm_title);
        TextView tvConfirmMessage = findViewById(R.id.tv_confirm_message);

        rvFacilityView = findViewById(R.id.rv_facility_view);
        illustFasilitas = findViewById(R.id.illust_fasilitas);
        deletionConfirmationPopup = findViewById(R.id.deletion_confirmation_popup);
        spinnerLokasi = findViewById(R.id.spinner_lokasi);
        btnNewFacility = findViewById(R.id.btn_new_facility);

        rvFacilityView.setLayoutManager(new LinearLayoutManager(this));
        locationList = new ArrayList<>();
        facilityList = new ArrayList<>();
        adapter = new FacilityViewAdapter(this, facilityList);
        rvFacilityView.setAdapter(adapter);

        tvConfirmTitle.setText("Yakin Hapus Fasilitas?");
        tvConfirmMessage.setText("Fasilitas yang sudah dihapus tidak dapat dikembalikan lagi");

        setSupportActionBar(topAppBar);
        getSupportActionBar().setTitle(null);
        backButton.setOnClickListener(v -> onBackPressed());

        ArrayList<String> lokasi = new ArrayList<>();
        lokasi.add("Pilih Lokasi");

        spinnerAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, lokasi) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }
        };
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLokasi.setAdapter(spinnerAdapter);
        spinnerLokasi.setSelection(0);

        spinnerLokasi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    btnNewFacility.setVisibility(View.GONE);
                    illustFasilitas.setVisibility(View.VISIBLE);
                    rvFacilityView.setVisibility(View.GONE);
                } else {
                    String selectedLocation = (String) parent.getItemAtPosition(position);
                    locationId = locationList.get(position - 1).getId();
                    fetchFacilityData(selectedLocation);

                    btnNewFacility.setOnClickListener(v -> {
                        Intent createFacility = new Intent(FacilityViewActivity.this, FacilityViewFormActivity.class);
                        createFacility.putExtra("locationId", locationId);
                        startActivityForResult(createFacility, REQUEST_CODE_NEW_FACILITY);
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        fetchLocationData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQUEST_CODE_NEW_FACILITY || requestCode == REQUEST_CODE_EDIT_FACILITY)
                && resultCode == RESULT_OK) {
            int selectedPosition = spinnerLokasi.getSelectedItemPosition();

            if (selectedPosition > 0) {
                locationId = locationList.get(selectedPosition - 1).getId();
                String selectedLocation = (String) spinnerLokasi.getSelectedItem();

                fetchFacilityData(selectedLocation);
            }
        }
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
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(FacilityViewActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateLocationSpinner() {
        List<String> lokasiNames = new ArrayList<>();
        lokasiNames.add("Pilih Lokasi");
        for (Location location : locationList) {
            lokasiNames.add(location.getName());
        }
        spinnerAdapter.clear();
        spinnerAdapter.addAll(lokasiNames);
        spinnerAdapter.notifyDataSetChanged();
    }

    private void fetchFacilityData(String locationName) {
        locationId = null;

        for (Location location : locationList) {
            if (location.getName().equals(locationName)) {
                locationId = location.getId();
                break;
            }
        }

        if (locationId != null) {
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
                    Toast.makeText(FacilityViewActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void updateVisibility() {
        if (facilityList.isEmpty()) {
            illustFasilitas.setVisibility(View.VISIBLE);
            rvFacilityView.setVisibility(View.GONE);
        } else {
            illustFasilitas.setVisibility(View.GONE);
            rvFacilityView.setVisibility(View.VISIBLE);
        }
        btnNewFacility.setVisibility(View.VISIBLE);
    }

    public void showDeletionConfirmationPopup(Facility facility) {
        deletionConfirmationPopup.setVisibility(View.VISIBLE);

        Button btnConfirmYa = findViewById(R.id.btn_confirm_ya);
        Button btnConfirmTidak = findViewById(R.id.btn_confirm_tidak);

        btnConfirmYa.setOnClickListener(v -> {
            deleteFacility(facility);
            deletionConfirmationPopup.setVisibility(View.GONE);
        });

        btnConfirmTidak.setOnClickListener(v -> deletionConfirmationPopup.setVisibility(View.GONE));
    }

    private void deleteFacility(Facility facility) {
        if (facility.getImageUrl() != null && !facility.getImageUrl().isEmpty()) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(facility.getImageUrl());
            storageReference.delete().addOnSuccessListener(aVoid -> deleteFacilityDocument(facility)).addOnFailureListener(e -> deleteFacilityDocument(facility));
        } else {
            deleteFacilityDocument(facility);
        }
    }

    private void deleteFacilityDocument(Facility facility) {
        auth.deleteDocumentData("locations/" + facility.getLocationId() + "/facilities",
                facility.getId(),
                new Authentication.FirebaseDocumentDeleteCallback() {
            @Override
            public void onSuccess() {
                facilityList.remove(facility);
                adapter.notifyDataSetChanged();
                updateVisibility();
                Toast.makeText(FacilityViewActivity.this, "Fasilitas berhasil dihapus", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(FacilityViewActivity.this, "Gagal menghapus fasilitas: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}