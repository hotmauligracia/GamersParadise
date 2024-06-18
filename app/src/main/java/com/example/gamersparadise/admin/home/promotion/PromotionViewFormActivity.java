package com.example.gamersparadise.admin.home.promotion;

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
import com.example.gamersparadise.data.Promotion;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

public class PromotionViewFormActivity extends AppCompatActivity {

    private static final String TAG = "PromotionViewFormActivity";
    private static final int PICK_IMAGE_REQUEST = 1;

    private View inputDiscPercentage, inputDiscMinus, cardPromotionImg;
    private ImageButton btnUploadImg;
    private ImageView uploadedImgView;
    private Spinner spinnerPromotionType;
    private EditText edtPromotionName, edtPromotionCode, edtPromotionPercentage, edtPromotionMinus, edtPromotionMinOrder,edtPromotionStartTime, edtPromotionEndTime, edtPromotionDesc;
    private float promotionNominal;
    private Authentication auth;
    private Promotion promotion;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion_view_form);

        MaterialToolbar topAppBar = findViewById(R.id.top_app_bar);
        ImageView backButton = findViewById(R.id.toolbar_back_icon);
        ImageButton btnCancelUploadImg = findViewById(R.id.btn_cancel_upload_img);
        Button btnSave = findViewById(R.id.btn_save);

        cardPromotionImg = findViewById(R.id.card_promotion_img);
        btnUploadImg = findViewById(R.id.btn_upload_img);
        uploadedImgView = findViewById(R.id.uploaded_img_view);
        spinnerPromotionType = findViewById(R.id.spinner_promotion_type);
        inputDiscPercentage = findViewById(R.id.input_disc_percentage);
        inputDiscMinus = findViewById(R.id.input_disc_minus);
        edtPromotionName = findViewById(R.id.edt_promotion_name);
        edtPromotionCode = findViewById(R.id.edt_promotion_code);
        edtPromotionPercentage = findViewById(R.id.edt_promotion_percentage);
        edtPromotionMinus = findViewById(R.id.edt_promotion_minus);
        edtPromotionMinOrder = findViewById(R.id.edt_promotion_min_order);
        edtPromotionStartTime = findViewById(R.id.edt_promotion_start_time);
        edtPromotionEndTime = findViewById(R.id.edt_promotion_end_time);
        edtPromotionDesc = findViewById(R.id.edt_promotion_desc);

        setSupportActionBar(topAppBar);
        getSupportActionBar().setTitle(null);
        backButton.setOnClickListener(v -> onBackPressed());

        Intent intent = getIntent();
        if (intent.hasExtra("promotion")) {
            promotion = intent.getParcelableExtra("promotion");
            if (promotion != null) {
                populateForm(promotion);
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
                if (promotion == null) {
                    createNewPromotion();
                } else {
                    updatePromotion();
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
        if (selectedImageUri == null && (promotion == null || promotion.getImageUrl() == null)) {
            cardPromotionImg.setVisibility(View.GONE);
            btnUploadImg.setVisibility(View.VISIBLE);
        } else {
            cardPromotionImg.setVisibility(View.VISIBLE);
            btnUploadImg.setVisibility(View.GONE);
            if (selectedImageUri != null) {
                uploadedImgView.setImageURI(selectedImageUri);
            } else if (promotion != null && promotion.getImageUrl() != null) {
                Glide.with(this).load(promotion.getImageUrl()).into(uploadedImgView);
            }
        }
    }

    private void populateForm(Promotion promotion) {
        edtPromotionName.setText(promotion.getName());
        edtPromotionCode.setText(promotion.getCode());

        edtPromotionMinOrder.setText(String.valueOf(promotion.getMinimumOrder()));
        edtPromotionStartTime.setText(String.valueOf(promotion.getStartTime()));
        edtPromotionEndTime.setText(String.valueOf(promotion.getEndTime()));
        edtPromotionDesc.setText(promotion.getDescription());
        updateImageUI();
    }

    private boolean validateInputs() {
        if (cardPromotionImg.getVisibility() == GONE ||
                TextUtils.isEmpty(edtPromotionName.getText().toString()) ||
                TextUtils.isEmpty(edtPromotionCode.getText().toString()) ||
                (TextUtils.isEmpty(edtPromotionPercentage.getText().toString()) || TextUtils.isEmpty(edtPromotionMinus.getText().toString())) ||
                TextUtils.isEmpty(edtPromotionMinOrder.getText().toString()) ||
                TextUtils.isEmpty(edtPromotionStartTime.getText().toString()) ||
                TextUtils.isEmpty(edtPromotionEndTime.getText().toString()) ||
                TextUtils.isEmpty(edtPromotionDesc.getText().toString())) {
            Toast.makeText(this, R.string.error_field_kosong, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void createNewPromotion() {
        Map<String, Object> data = new HashMap<>();
        data.put("name", edtPromotionName.getText().toString());
        data.put("code", edtPromotionCode.getText().toString());

        promotionNominal = Float.parseFloat(null);

        if (TextUtils.isEmpty(edtPromotionPercentage.getText().toString())) {
            if (!TextUtils.isEmpty(edtPromotionMinus.getText().toString())) {
                try {
                    promotionNominal = Float.parseFloat(edtPromotionMinus.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Nominal diskon tidak valid", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            try {
                promotionNominal = Float.parseFloat(edtPromotionPercentage.getText().toString());
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Nominal diskon tidak valid", Toast.LENGTH_SHORT).show();
            }
        }
        data.put("nominal", promotionNominal);

        data.put("minimumOrder", Float.parseFloat(edtPromotionMinOrder.getText().toString()));

        LocalDateTime startTime = null;
        try {
            startTime = LocalDateTime.parse(edtPromotionStartTime.getText().toString());
        } catch (DateTimeParseException e) {
            Toast.makeText(this, "Format waktu mulai tidak valid", Toast.LENGTH_SHORT).show();
        }
        data.put("startTime", startTime);

        LocalDateTime endTime = null;
        try {
            endTime = LocalDateTime.parse(edtPromotionEndTime.getText().toString());
        } catch (DateTimeParseException e) {
            Toast.makeText(this, "Format waktu mulai tidak valid", Toast.LENGTH_SHORT).show();
        }
        data.put("endTime", endTime);

        data.put("description", edtPromotionDesc.getText().toString());

        if (selectedImageUri != null) {
            uploadImageAndSavePromotion(data);
        } else {
            savePromotionData(data, null);
        }
    }

    private void updatePromotion() {
        Map<String, Object> data = new HashMap<>();
        data.put("name", edtPromotionName.getText().toString());
        data.put("code", edtPromotionCode.getText().toString());

        promotionNominal = Float.parseFloat(null);

        if (TextUtils.isEmpty(edtPromotionPercentage.getText().toString())) {
            if (!TextUtils.isEmpty(edtPromotionMinus.getText().toString())) {
                try {
                    promotionNominal = Float.parseFloat(edtPromotionMinus.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Nominal diskon tidak valid", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            try {
                promotionNominal = Float.parseFloat(edtPromotionPercentage.getText().toString());
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Nominal diskon tidak valid", Toast.LENGTH_SHORT).show();
            }
        }
        data.put("nominal", promotionNominal);

        data.put("minimumOrder", Float.parseFloat(edtPromotionMinOrder.getText().toString()));

        LocalDateTime startTime = null;
        try {
            startTime = LocalDateTime.parse(edtPromotionStartTime.getText().toString());
        } catch (DateTimeParseException e) {
            Toast.makeText(this, "Format waktu mulai tidak valid", Toast.LENGTH_SHORT).show();
        }
        data.put("startTime", startTime);

        LocalDateTime endTime = null;
        try {
            endTime = LocalDateTime.parse(edtPromotionEndTime.getText().toString());
        } catch (DateTimeParseException e) {
            Toast.makeText(this, "Format waktu mulai tidak valid", Toast.LENGTH_SHORT).show();
        }
        data.put("endTime", endTime);

        data.put("description", edtPromotionDesc.getText().toString());

        if (selectedImageUri != null) {
            uploadImageAndSavePromotion(data);
        } else {
            savePromotionData(data, promotion.getImageUrl());
        }
    }

    private void uploadImageAndSavePromotion(Map<String, Object> data) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("promotion_images/" + System.currentTimeMillis() + ".jpg");
        storageReference.putFile(selectedImageUri)
                .addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl()
                        .addOnSuccessListener(uri -> savePromotionData(data, uri.toString())))
                .addOnFailureListener(e -> Toast.makeText(PromotionViewFormActivity.this, "Gagal mengunggah gambar: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void savePromotionData(Map<String, Object> data, @Nullable String imageUrl) {
        if (imageUrl != null) {
            data.put("imageUrl", imageUrl);
        }

        if (promotion == null) {
            auth.addDocumentData("promotions", data, new Authentication.FirebaseDocumentAddCallback() {
                @Override
                public void onSuccess(String documentId) {
                    Toast.makeText(PromotionViewFormActivity.this, "Promosi berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                    setResult(Activity.RESULT_OK);
                    finish();
                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(PromotionViewFormActivity.this, "Gagal menambahkan promosi: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            auth.updateDocumentData("promotions", promotion.getId(), data, new Authentication.FirebaseDocumentCallback() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Toast.makeText(PromotionViewFormActivity.this, "Promosi berhasil diperbarui", Toast.LENGTH_SHORT).show();
                    promotion.setImageUrl(imageUrl);
                    updateImageUI();
                    setResult(Activity.RESULT_OK);
                    finish();
                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(PromotionViewFormActivity.this, "Gagal memperbarui promosi: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}