package com.example.gamersparadise.admin.home.facility;

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
import com.example.gamersparadise.data.Facility;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FacilityViewFormActivity extends AppCompatActivity {

    private static final String TAG = "FacilityViewFormActivity";
    private static final int PICK_IMAGE_REQUEST = 1;

    private View cardFacilityImg;
    private ImageButton btnUploadImg;
    private ImageView uploadedImgView;
    private EditText edtFacilityName, edtFacilityCapacity, edtFacilityPrice, edtFacilityDetails;
    private Authentication auth;
    private Facility facility;
    private Uri selectedImageUri;
    private String locationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility_view_form);

        auth = new Authentication();

        MaterialToolbar topAppBar = findViewById(R.id.top_app_bar);
        ImageView backButton = findViewById(R.id.toolbar_back_icon);
        ImageButton btnCancelUploadImg = findViewById(R.id.btn_cancel_upload_img);
        Button btnSave = findViewById(R.id.btn_save);

        cardFacilityImg = findViewById(R.id.card_facility_img);
        btnUploadImg = findViewById(R.id.btn_upload_img);
        uploadedImgView = findViewById(R.id.uploaded_img_view);
        edtFacilityName = findViewById(R.id.edt_facility_name);
        edtFacilityCapacity = findViewById(R.id.edt_facility_capacity);
        edtFacilityPrice = findViewById(R.id.edt_facility_price);
        edtFacilityDetails = findViewById(R.id.edt_facility_details);

        setSupportActionBar(topAppBar);
        getSupportActionBar().setTitle(null);
        backButton.setOnClickListener(v -> onBackPressed());

        Intent intent = getIntent();
        locationId = intent.getStringExtra("locationId");

        if (intent.hasExtra("facility")) {
            facility = intent.getParcelableExtra("facility");
            if (facility != null) {
                populateForm(facility);
            } else {
                Log.e(TAG, "Facility extra is null");
            }
        } else {
            Log.e(TAG, "Intent or facility extra is null");
        }

        btnUploadImg.setOnClickListener(v -> openImagePicker());
        btnCancelUploadImg.setOnClickListener(v -> cancelImageUpload());

        btnSave.setOnClickListener(v -> {
            if (validateInputs()) {
                if (facility == null) {
                    createNewFacility();
                } else {
                    updateFacility();
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

    private void updateImageUI() {
        if (selectedImageUri == null && (facility == null || facility.getImageUrl() == null)) {
            cardFacilityImg.setVisibility(View.GONE);
            btnUploadImg.setVisibility(View.VISIBLE);
        } else {
            cardFacilityImg.setVisibility(View.VISIBLE);
            btnUploadImg.setVisibility(View.GONE);
            if (selectedImageUri != null) {
                uploadedImgView.setImageURI(selectedImageUri);
            } else if (facility != null && facility.getImageUrl() != null) {
                Glide.with(this).load(facility.getImageUrl()).into(uploadedImgView);
            }
        }
    }

    private void populateForm(Facility facility) {
        edtFacilityName.setText(facility.getName());
        edtFacilityCapacity.setText(String.valueOf(facility.getCapacity()));
        edtFacilityPrice.setText(String.valueOf(facility.getPrice()));
        edtFacilityDetails.setText(facility.getDetails());
        updateImageUI();
    }

    private boolean validateInputs() {
        if (cardFacilityImg.getVisibility() == GONE ||
                TextUtils.isEmpty(edtFacilityName.getText().toString()) ||
                TextUtils.isEmpty(edtFacilityCapacity.getText().toString()) ||
                TextUtils.isEmpty(edtFacilityPrice.getText().toString()) ||
                TextUtils.isEmpty(edtFacilityDetails.getText().toString())) {
            Toast.makeText(this, R.string.error_field_kosong, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void createNewFacility() {
        Map<String, Object> data = new HashMap<>();
        data.put("name", edtFacilityName.getText().toString());
        data.put("capacity", Integer.parseInt(edtFacilityCapacity.getText().toString()));
        data.put("price", Float.parseFloat(edtFacilityPrice.getText().toString()));
        data.put("details", edtFacilityDetails.getText().toString());

        if (selectedImageUri != null) {
            uploadImageAndSaveFacility(data);
        } else {
            saveFacilityData(data, null);
        }
    }

    private void updateFacility() {
        Map<String, Object> data = new HashMap<>();
        data.put("name", edtFacilityName.getText().toString());
        data.put("capacity", Integer.parseInt(edtFacilityCapacity.getText().toString()));
        data.put("price", Float.parseFloat(edtFacilityPrice.getText().toString()));
        data.put("details", edtFacilityDetails.getText().toString());

        if (selectedImageUri != null) {
            uploadImageAndSaveFacility(data);
        } else {
            saveFacilityData(data, facility.getImageUrl());
        }
    }

    private void uploadImageAndSaveFacility(Map<String, Object> data) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("facility_images/" + System.currentTimeMillis() + ".jpg");
        storageReference.putFile(selectedImageUri)
                .addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl()
                        .addOnSuccessListener(uri -> saveFacilityData(data, uri.toString())))
                .addOnFailureListener(e -> Toast.makeText(FacilityViewFormActivity.this, "Gagal mengunggah gambar: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void saveFacilityData(Map<String, Object> data, @Nullable String imageUrl) {
        if (imageUrl != null) {
            data.put("imageUrl", imageUrl);
        }
        data.put("locationId", locationId);

        if (facility == null) {
            auth.addDocumentData("locations/" + locationId + "/facilities", data, new Authentication.FirebaseDocumentAddCallback() {
                @Override
                public void onSuccess(String documentId) {
                    Toast.makeText(FacilityViewFormActivity.this, "Fasilitas berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                    setResult(Activity.RESULT_OK);
                    finish();
                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(FacilityViewFormActivity.this, "Gagal menambahkan fasilitas: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            auth.updateDocumentData("locations/" + locationId + "/facilities", facility.getId(), data, new Authentication.FirebaseDocumentCallback() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Toast.makeText(FacilityViewFormActivity.this, "Fasilitas berhasil diperbarui", Toast.LENGTH_SHORT).show();
                    facility.setImageUrl(imageUrl);
                    updateImageUI();
                    setResult(Activity.RESULT_OK);
                    finish();
                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(FacilityViewFormActivity.this, "Gagal memperbarui fasilitas: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}