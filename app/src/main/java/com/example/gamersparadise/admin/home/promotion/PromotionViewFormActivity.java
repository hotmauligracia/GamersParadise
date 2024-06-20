package com.example.gamersparadise.admin.home.promotion;

import static android.view.View.GONE;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import com.example.gamersparadise.data.PromotionType;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class PromotionViewFormActivity extends AppCompatActivity {

    private static final String TAG = "PromotionViewFormActivity";
    private static final int PICK_IMAGE_REQUEST = 1;

    private View inputDiscPercentage, inputDiscMinus, cardPromotionImg;
    private ImageButton btnUploadImg, btnCancelUploadImg;
    private ImageView uploadedImgView;
    private List<PromotionType> promotionTypeList;
    private ArrayAdapter<String> spinnerAdapter;
    private Spinner spinnerPromotionType;
    private EditText edtPromotionName, edtPromotionCode, edtPromotionPercentage, edtPromotionMinus, edtPromotionMinOrder,edtPromotionStartTime, edtPromotionEndTime, edtPromotionDesc;
    private float promotionNominal;
    private Authentication auth;
    private Promotion promotion;
    private Uri selectedImageUri;
    private int promotionTypeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion_view_form);

        auth = new Authentication();
        promotionTypeList = new ArrayList<>();

        MaterialToolbar topAppBar = findViewById(R.id.top_app_bar);
        ImageView backButton = findViewById(R.id.toolbar_back_icon);
        Button btnSave = findViewById(R.id.btn_save);

        cardPromotionImg = findViewById(R.id.card_promotion_img);
        View cardWaktuPromoMulai = findViewById(R.id.card_waktu_promo_mulai);
        View cardWaktuPromoBerakhir = findViewById(R.id.card_waktu_promo_berakhir);
        btnUploadImg = findViewById(R.id.btn_upload_img);
        btnCancelUploadImg = findViewById(R.id.btn_cancel_upload_img);
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
                Log.e(TAG, "Promotion extra is null");
            }
        } else {
            Log.e(TAG, "Intent or promotion extra is null");
        }

        ArrayList<String> promosi = new ArrayList<>();
        promosi.add("Pilih tipe promo di sini");

        spinnerAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, promosi) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }
        };
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPromotionType.setAdapter(spinnerAdapter);
        spinnerPromotionType.setSelection(0);

        fetchPromotionTypeData();

        spinnerPromotionType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    inputDiscPercentage.setVisibility(View.GONE);
                    inputDiscMinus.setVisibility(View.GONE);
                } else {
                    PromotionType selectedType = promotionTypeList.get(position - 1);
                    promotionTypeId = selectedType.getId();
                    togglePromotionInput(selectedType.getName());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        cardWaktuPromoMulai.setOnClickListener(v -> showDateTimePicker(edtPromotionStartTime));
        cardWaktuPromoBerakhir.setOnClickListener(v -> showDateTimePicker(edtPromotionEndTime));

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

    private void fetchPromotionTypeData() {
        auth.fetchCollectionData("promotionTypes", new Authentication.FirebaseCollectionCallback() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                promotionTypeList.clear();
                for (QueryDocumentSnapshot document : querySnapshot) {
                    PromotionType promotionType = document.toObject(PromotionType.class);
                    promotionType.setId(Integer.parseInt(document.getId()));
                    promotionTypeList.add(promotionType);
                }
                updatePromotionSpinner();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(PromotionViewFormActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updatePromotionSpinner() {
        List<String> tipePromosiNames = new ArrayList<>();
        tipePromosiNames.add("Pilih tipe promo di sini");
        for (PromotionType promotionType : promotionTypeList) {
            tipePromosiNames.add(promotionType.getName());
        }
        spinnerAdapter.clear();
        spinnerAdapter.addAll(tipePromosiNames);
        spinnerAdapter.notifyDataSetChanged();
        if (promotion != null) {
            setSpinnerSelection(promotion.getPromotionTypeId());
        }
    }

    private void setSpinnerSelection(int promotionTypeId) {
        for (int i = 0; i < promotionTypeList.size(); i++) {
            if (promotionTypeList.get(i).getId() == promotionTypeId) {
                spinnerPromotionType.setSelection(i + 1);
                break;
            }
        }
    }

    private void togglePromotionInput(String promotionTypeName) {
        if (promotionTypeName.equalsIgnoreCase("Diskon Persentase")) {
            inputDiscPercentage.setVisibility(View.VISIBLE);
            inputDiscMinus.setVisibility(GONE);
        } else if (promotionTypeName.equalsIgnoreCase("Diskon Pengurangan")) {
            inputDiscPercentage.setVisibility(GONE);
            inputDiscMinus.setVisibility(View.VISIBLE);
        } else {
            inputDiscPercentage.setVisibility(GONE);
            inputDiscMinus.setVisibility(GONE);
        }
    }

    private void showDateTimePicker(EditText editText) {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(PromotionViewFormActivity.this,
                (view, year, month, dayOfMonth) -> {
            calendar.set(year, month, dayOfMonth);
            new TimePickerDialog(PromotionViewFormActivity.this,
                    (timeView, hourOfDay, minute) -> {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                LocalDateTime localDateTime = LocalDateTime.of(
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH) + 1, // Month is 0-based in Calendar
                        calendar.get(Calendar.DAY_OF_MONTH),
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE)
                );
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
                editText.setText(localDateTime.format(formatter));
            },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true)
                    .show();
        },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    private void populateForm(Promotion promotion) {
        edtPromotionName.setText(promotion.getName());
        edtPromotionCode.setText(promotion.getCode());
        edtPromotionPercentage.setText(promotion.getNominal() != 0 ? String.valueOf(promotion.getNominal()) : "");
        edtPromotionMinus.setText(promotion.getNominal() != 0 ? String.valueOf(promotion.getNominal()) : "");
        edtPromotionMinOrder.setText(String.valueOf(promotion.getMinimumOrder()));
        edtPromotionStartTime.setText(promotion.getStartTime());
        edtPromotionEndTime.setText(promotion.getEndTime());
        edtPromotionDesc.setText(promotion.getDescription());

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
            btnUploadImg.setOnClickListener(v -> openImagePicker());
            uploadedImgView.setImageDrawable(null);
        } else {
            cardPromotionImg.setVisibility(View.VISIBLE);
            btnUploadImg.setVisibility(View.GONE);
            if (selectedImageUri != null) {
                uploadedImgView.setImageURI(selectedImageUri);
            } else if (promotion != null && promotion.getImageUrl() != null) {
                Glide.with(this).load(promotion.getImageUrl()).into(uploadedImgView);
            }
            btnCancelUploadImg.setOnClickListener(v -> cancelImageUpload());
        }
    }

    private boolean validateInputs() {
        if (cardPromotionImg.getVisibility() == GONE ||
                TextUtils.isEmpty(edtPromotionName.getText().toString()) ||
                TextUtils.isEmpty(edtPromotionCode.getText().toString()) ||
                spinnerPromotionType.getSelectedItemPosition() == 0 ||
                (promotionTypeId == 1 && TextUtils.isEmpty(edtPromotionPercentage.getText().toString())) ||
                (promotionTypeId == 2 && TextUtils.isEmpty(edtPromotionMinus.getText().toString())) ||
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
        data.put("promotionTypeId", promotionTypeId);

        if (inputDiscPercentage.getVisibility() == View.VISIBLE) {
            promotionNominal = Float.parseFloat(edtPromotionPercentage.getText().toString());
        } else {
            promotionNominal = Float.parseFloat(edtPromotionMinus.getText().toString());
        }
        data.put("nominal", promotionNominal);
        data.put("minimumOrder", Float.parseFloat(edtPromotionMinOrder.getText().toString()));

        LocalDateTime startTime = LocalDateTime.parse(edtPromotionStartTime.getText().toString(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        LocalDateTime endTime = LocalDateTime.parse(edtPromotionEndTime.getText().toString(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        data.put("startTime", startTime);
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
        data.put("promotionTypeId", promotionTypeId);

        if (inputDiscPercentage.getVisibility() == View.VISIBLE) {
            promotionNominal = Float.parseFloat(edtPromotionPercentage.getText().toString());
        } else {
            promotionNominal = Float.parseFloat(edtPromotionMinus.getText().toString());
        }
        data.put("nominal", promotionNominal);
        data.put("minimumOrder", Float.parseFloat(edtPromotionMinOrder.getText().toString()));

        LocalDateTime startTime = LocalDateTime.parse(edtPromotionStartTime.getText().toString(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        LocalDateTime endTime = LocalDateTime.parse(edtPromotionEndTime.getText().toString(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        data.put("startTime", startTime);
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

        String startTime = edtPromotionStartTime.getText().toString();
        String endTime = edtPromotionEndTime.getText().toString();

        data.put("startTime", startTime);
        data.put("endTime", endTime);

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