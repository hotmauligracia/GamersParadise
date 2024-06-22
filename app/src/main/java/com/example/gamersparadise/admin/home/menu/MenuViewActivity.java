package com.example.gamersparadise.admin.home.menu;

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
import com.example.gamersparadise.admin.home.menu.adapter.MenuViewAdapter;
import com.example.gamersparadise.data.Location;
import com.example.gamersparadise.data.Menu;
import com.example.gamersparadise.data.MenuType;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.*;

public class MenuViewActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_NEW_MENU = 1;
    public static final int REQUEST_CODE_EDIT_MENU = 2;

    private RecyclerView rvMenuView;
    private MenuViewAdapter adapter;
    private List<Location> locationList;
    private List<Menu> menuList;
    private List<MenuType> menuTypeList;
    private Authentication auth;
    private ArrayAdapter<String> spinnerAdapter;
    private Spinner spinnerLokasi;
    private View illustMenu, deletionConfirmationPopup;
    private Button btnNewMenu;
    private String locationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_view);

        auth = new Authentication();

        MaterialToolbar topAppBar = findViewById(R.id.top_app_bar);
        ImageView backButton = findViewById(R.id.toolbar_back_icon);
        TextView tvConfirmTitle = findViewById(R.id.tv_confirm_title);
        TextView tvConfirmMessage = findViewById(R.id.tv_confirm_message);

        rvMenuView = findViewById(R.id.rv_menu_view);
        illustMenu = findViewById(R.id.illust_menu);
        deletionConfirmationPopup = findViewById(R.id.deletion_confirmation_popup);
        btnNewMenu = findViewById(R.id.btn_new_menu);
        spinnerLokasi = findViewById(R.id.spinner_lokasi);

        rvMenuView.setLayoutManager(new LinearLayoutManager(this));
        locationList = new ArrayList<>();
        menuList = new ArrayList<>();
        adapter = new MenuViewAdapter(this, menuList);
        rvMenuView.setAdapter(adapter);

        tvConfirmTitle.setText("Yakin Hapus Menu?");
        tvConfirmMessage.setText("Menu yang sudah dihapus tidak dapat dikembalikan lagi");

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
                    btnNewMenu.setVisibility(View.GONE);
                    illustMenu.setVisibility(View.VISIBLE);
                    rvMenuView.setVisibility(View.GONE);
                } else {
                    String selectedLocation = (String) parent.getItemAtPosition(position);
                    locationId = locationList.get(position - 1).getId();
                    fetchMenuData(selectedLocation);

                    btnNewMenu.setOnClickListener(v -> {
                        Intent createMenu = new Intent(MenuViewActivity.this, MenuViewFormActivity.class);
                        createMenu.putExtra("locationId", locationId);
                        startActivityForResult(createMenu, REQUEST_CODE_NEW_MENU);
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        menuTypeList = new ArrayList<>();
        fetchMenuTypeData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQUEST_CODE_NEW_MENU || requestCode == REQUEST_CODE_EDIT_MENU)
                && resultCode == RESULT_OK) {
            int selectedPosition = spinnerLokasi.getSelectedItemPosition();

            if (selectedPosition > 0) {
                locationId = locationList.get(selectedPosition - 1).getId();
                String selectedLocation = (String) spinnerLokasi.getSelectedItem();

                fetchMenuData(selectedLocation);
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
                Toast.makeText(MenuViewActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
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

    private void fetchMenuTypeData() {
        auth.fetchCollectionData("menuTypes", new Authentication.FirebaseCollectionCallback() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                menuTypeList.clear();
                for (QueryDocumentSnapshot document : querySnapshot) {
                    MenuType menuType = document.toObject(MenuType.class);
                    menuType.setId(Integer.parseInt(document.getId()));
                    menuTypeList.add(menuType);
                }
                fetchLocationData();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(MenuViewActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchMenuData(String locationName) {
        locationId = null;

        for (Location location : locationList) {
            if (location.getName().equals(locationName)) {
                locationId = location.getId();
                break;
            }
        }

        if (locationId != null) {
            auth.fetchCollectionData("locations/" + locationId + "/menus", new Authentication.FirebaseCollectionCallback() {
                @Override
                public void onSuccess(QuerySnapshot querySnapshot) {
                    menuList.clear();
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        Menu menu = document.toObject(Menu.class);
                        menu.setId(document.getId());
                        menuList.add(menu);
                    }
                    adapter.setMenuTypeList(menuTypeList);
                    adapter.notifyDataSetChanged();
                    updateVisibility();
                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(MenuViewActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void updateVisibility() {
        if (menuList.isEmpty()) {
            illustMenu.setVisibility(View.VISIBLE);
            rvMenuView.setVisibility(View.GONE);
        } else {
            illustMenu.setVisibility(View.GONE);
            rvMenuView.setVisibility(View.VISIBLE);
        }
        btnNewMenu.setVisibility(View.VISIBLE);
    }

    public void showDeletionConfirmationPopup(Menu menu) {
        deletionConfirmationPopup.setVisibility(View.VISIBLE);

        Button btnConfirmYa = findViewById(R.id.btn_confirm_ya);
        Button btnConfirmTidak = findViewById(R.id.btn_confirm_tidak);

        btnConfirmYa.setOnClickListener(v -> {
            deleteMenu(menu);
            deletionConfirmationPopup.setVisibility(View.GONE);
        });

        btnConfirmTidak.setOnClickListener(v -> deletionConfirmationPopup.setVisibility(View.GONE));
    }

    private void deleteMenu(Menu menu) {
        if (menu.getImageUrl() != null && !menu.getImageUrl().isEmpty()) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(menu.getImageUrl());
            storageReference.delete().addOnSuccessListener(aVoid -> deleteMenuDocument(menu)).addOnFailureListener(e -> deleteMenuDocument(menu));
        } else {
            deleteMenuDocument(menu);
        }
    }

    private void deleteMenuDocument(Menu menu) {
        auth.deleteDocumentData("locations/" + menu.getLocationId() + "/menus",
                menu.getId(),
                new Authentication.FirebaseDocumentDeleteCallback() {
                    @Override
                    public void onSuccess() {
                        menuList.remove(menu);
                        adapter.notifyDataSetChanged();
                        updateVisibility();
                        Toast.makeText(MenuViewActivity.this, "Menu berhasil dihapus", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Toast.makeText(MenuViewActivity.this, "Gagal menghapus menu: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}