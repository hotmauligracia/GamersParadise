package com.example.gamersparadise.admin.home.menu;

import static android.view.View.GONE;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.*;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.gamersparadise.Authentication;
import com.example.gamersparadise.R;
import com.example.gamersparadise.data.Menu;
import com.example.gamersparadise.data.MenuType;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.*;

public class MenuViewFormActivity extends AppCompatActivity {

    private static final String TAG = "MenuViewFormActivity";
    private static final int PICK_IMAGE_REQUEST = 1;

    private View cardMenuImg;
    private ImageButton btnUploadImg, btnCancelUploadImg;
    private ImageView uploadedImgView;
    private List<MenuType> menuTypeList;
    private ArrayAdapter<String> spinnerAdapter;
    private Spinner spinnerMenuType;
    private EditText edtMenuName, edtMenuDesc, edtMenuPrice;
    private Authentication auth;
    private Menu menu;
    private Uri selectedImageUri;
    private String locationId;
    private int menuTypeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_view_form);

        auth = new Authentication();
        menuTypeList = new ArrayList<>();

        MaterialToolbar topAppBar = findViewById(R.id.top_app_bar);
        ImageView backButton = findViewById(R.id.toolbar_back_icon);
        Button btnSave = findViewById(R.id.btn_save);

        cardMenuImg = findViewById(R.id.card_menu_img);
        btnUploadImg = findViewById(R.id.btn_upload_img);
        btnCancelUploadImg = findViewById(R.id.btn_cancel_upload_img);
        uploadedImgView = findViewById(R.id.uploaded_img_view);
        spinnerMenuType = findViewById(R.id.spinner_menu_type);
        edtMenuName = findViewById(R.id.edt_menu_name);
        edtMenuDesc = findViewById(R.id.edt_menu_desc);
        edtMenuPrice = findViewById(R.id.edt_menu_price);

        setSupportActionBar(topAppBar);
        getSupportActionBar().setTitle(null);
        backButton.setOnClickListener(v -> onBackPressed());

        Intent intent = getIntent();
        locationId = getIntent().getStringExtra("locationId");

        if (intent.hasExtra("menu")) {
            menu = intent.getParcelableExtra("menu");
            if (menu != null) {
                populateForm(menu);
            } else {
                Log.e(TAG, "Menu extra is null");
            }
        } else {
            Log.e(TAG, "Intent or menu extra is null");
        }

        ArrayList<String> tipeMenu = new ArrayList<>();
        tipeMenu.add("Pilih tipe menu di sini");

        spinnerAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, tipeMenu) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }
        };
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMenuType.setAdapter(spinnerAdapter);
        spinnerMenuType.setSelection(0);

        fetchMenuTypeData();

        spinnerMenuType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    MenuType selectedType = menuTypeList.get(position - 1);
                    menuTypeId = selectedType.getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btnSave.setOnClickListener(v -> {
            if (validateInputs()) {
                if (menu == null) {
                    createNewMenu();
                } else {
                    updateMenu();
                }
            }
        });

        updateImageUI();
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);

                uploadedImgView.setImageBitmap(bitmap);
                updateImageUI();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void cancelImageUpload() {
        selectedImageUri = null;
        updateImageUI();
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
                updateMenuSpinner();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(MenuViewFormActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateMenuSpinner() {
        List<String> tipeMenuNames = new ArrayList<>();
        tipeMenuNames.add("Pilih tipe menu di sini");
        for (MenuType menuType : menuTypeList) {
            tipeMenuNames.add(menuType.getName());
        }
        spinnerAdapter.clear();
        spinnerAdapter.addAll(tipeMenuNames);
        spinnerAdapter.notifyDataSetChanged();
        if (menu != null) {
            setSpinnerSelection(menu.getMenuTypeId());
        }
    }

    private void setSpinnerSelection(int menuTypeId) {
        for (int i = 0; i < menuTypeList.size(); i++) {
            if (menuTypeList.get(i).getId() == menuTypeId) {
                spinnerMenuType.setSelection(i + 1);
                break;
            }
        }
    }

    private void populateForm(Menu menu) {
        edtMenuName.setText(menu.getName());
        edtMenuDesc.setText(menu.getDescription());
        edtMenuPrice.setText(String.valueOf(menu.getPrice()));
        setSpinnerSelection(menu.getMenuTypeId());
        updateImageUI();
    }

    private void updateImageUI() {
        if (selectedImageUri == null && (menu == null || menu.getImageUrl() == null)) {
            cardMenuImg.setVisibility(View.GONE);
            btnUploadImg.setVisibility(View.VISIBLE);
            btnUploadImg.setOnClickListener(v -> openImagePicker());
        } else {
            cardMenuImg.setVisibility(View.VISIBLE);
            btnUploadImg.setVisibility(View.GONE);
            btnCancelUploadImg.setOnClickListener(v -> cancelImageUpload());
            if (selectedImageUri != null) {
                uploadedImgView.setImageURI(selectedImageUri);
            } else if (menu != null && menu.getImageUrl() != null) {
                Glide.with(this).load(menu.getImageUrl()).into(uploadedImgView);
            }
        }
    }

    private boolean validateInputs() {
        if (cardMenuImg.getVisibility() == GONE ||
                TextUtils.isEmpty(edtMenuName.getText().toString()) ||
                TextUtils.isEmpty(edtMenuDesc.getText().toString()) ||
                spinnerMenuType.getSelectedItemPosition() == 0 ||
                TextUtils.isEmpty(edtMenuPrice.getText().toString())) {
            Toast.makeText(this, R.string.error_field_kosong, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void createNewMenu() {
        Map<String, Object> data = new HashMap<>();
        data.put("name", edtMenuName.getText().toString());
        data.put("description", edtMenuDesc.getText().toString());
        data.put("menuTypeId", menuTypeId);
        data.put("price", Float.parseFloat(edtMenuPrice.getText().toString()));

        if (selectedImageUri != null) {
            uploadImageAndSaveMenu(data);
        } else {
            saveMenuData(data, null);
        }
    }

    private void updateMenu() {
        Map<String, Object> data = new HashMap<>();
        data.put("name", edtMenuName.getText().toString());
        data.put("description", edtMenuDesc.getText().toString());
        data.put("menuTypeId", menuTypeId);
        data.put("price", Float.parseFloat(edtMenuPrice.getText().toString()));

        if (selectedImageUri != null) {
            uploadImageAndSaveMenu(data);
        } else {
            saveMenuData(data, menu.getImageUrl());
        }
    }

    private void uploadImageAndSaveMenu(Map<String, Object> data) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("menu_images/" + System.currentTimeMillis() + ".jpg");
        storageReference.putFile(selectedImageUri)
                .addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl()
                        .addOnSuccessListener(uri -> saveMenuData(data, uri.toString())))
                .addOnFailureListener(e -> Toast.makeText(MenuViewFormActivity.this, "Gagal mengunggah gambar: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void saveMenuData(Map<String, Object> data, String imageUrl) {
        if (imageUrl != null) {
            data.put("imageUrl", imageUrl);
        }

        data.put("locationId", locationId);

        if (menu == null) {
            auth.addDocumentData("locations/" + locationId + "/menus", data, new Authentication.FirebaseDocumentAddCallback() {
                @Override
                public void onSuccess(String documentId) {
                    Toast.makeText(MenuViewFormActivity.this, "Menu berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                    setResult(Activity.RESULT_OK);
                    finish();
                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(MenuViewFormActivity.this, "Gagal menambahkan menu: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            auth.updateDocumentData("locations/" + locationId + "/menus", menu.getId(), data, new Authentication.FirebaseDocumentCallback() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Toast.makeText(MenuViewFormActivity.this, "Menu berhasil diperbarui", Toast.LENGTH_SHORT).show();
                    menu.setImageUrl(imageUrl);
                    updateImageUI();
                    setResult(Activity.RESULT_OK);
                    finish();
                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(MenuViewFormActivity.this, "Gagal memperbarui menu: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}