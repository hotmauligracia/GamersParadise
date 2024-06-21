package com.example.gamersparadise.admin.home.promotion;

import static com.example.gamersparadise.data.Promotion.dateTimeFormatter;

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
import com.example.gamersparadise.admin.home.promotion.adapter.PromotionViewAdapter;
import com.example.gamersparadise.data.Promotion;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PromotionViewActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_NEW_PROMOTION = 1;
    public static final int REQUEST_CODE_EDIT_PROMOTION = 2;
    private RecyclerView rvPromotionView;
    private PromotionViewAdapter adapter;
    private List<Promotion> promotionList;
    private Authentication auth;
    private View illustPromo, deletionConfirmationPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion_view);

        auth = new Authentication();

        MaterialToolbar topAppBar = findViewById(R.id.top_app_bar);
        ImageView backButton = findViewById(R.id.toolbar_back_icon);
        TextView tvConfirmTitle = findViewById(R.id.tv_confirm_title);
        TextView tvConfirmMessage = findViewById(R.id.tv_confirm_message);

        illustPromo = findViewById(R.id.illust_promo);
        deletionConfirmationPopup = findViewById(R.id.deletion_confirmation_popup);
        rvPromotionView = findViewById(R.id.rv_promotion_view);

        rvPromotionView.setLayoutManager(new LinearLayoutManager(this));
        promotionList = new ArrayList<>();
        adapter = new PromotionViewAdapter(this, promotionList);
        rvPromotionView.setAdapter(adapter);

        tvConfirmTitle.setText("Yakin Hapus Promosi?");
        tvConfirmMessage.setText("Promosi yang sudah dihapus tidak dapat dikembalikan lagi");

        setSupportActionBar(topAppBar);
        getSupportActionBar().setTitle(null);
        backButton.setOnClickListener(v -> onBackPressed());

        Button btnNewPromotion = findViewById(R.id.btn_new_promotion);
        btnNewPromotion.setOnClickListener(v -> {
            Intent createPromotion = new Intent(PromotionViewActivity.this, PromotionViewFormActivity.class);
            startActivityForResult(createPromotion, REQUEST_CODE_NEW_PROMOTION);
        });

        fetchPromotionData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQUEST_CODE_NEW_PROMOTION || requestCode == REQUEST_CODE_EDIT_PROMOTION)
                && resultCode == RESULT_OK) {
            fetchPromotionData();
        }
    }

    private void fetchPromotionData() {
        promotionList.clear();
        auth.fetchCollectionData("promotions", new Authentication.FirebaseCollectionCallback() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                for (QueryDocumentSnapshot document : querySnapshot) {
                    Promotion promotion = document.toObject(Promotion.class);
                    promotion.setId(document.getId());

                    String startTimeStr = document.getString("startTime");
                    String endTimeStr = document.getString("endTime");
                    if (startTimeStr != null && endTimeStr != null) {
                        promotion.setStartTimeAsLocalDateTime(LocalDateTime.parse(startTimeStr, dateTimeFormatter));
                        promotion.setEndTimeAsLocalDateTime(LocalDateTime.parse(endTimeStr, dateTimeFormatter));
                    }

                    Double nominalValue = document.getDouble("nominal");
                    if (nominalValue != null) {
                        promotion.setNominal(nominalValue.floatValue());
                    }

                    Double minOrderValue = document.getDouble("minimumOrder");
                    if (minOrderValue != null) {
                        promotion.setMinimumOrder(minOrderValue.floatValue());
                    }

                    promotionList.add(promotion);
                }
                adapter.notifyDataSetChanged();
                updateVisibility();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(PromotionViewActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateVisibility() {
        if (promotionList.isEmpty()) {
            illustPromo.setVisibility(View.VISIBLE);
            rvPromotionView.setVisibility(View.GONE);
        } else {
            illustPromo.setVisibility(View.GONE);
            rvPromotionView.setVisibility(View.VISIBLE);
        }
    }

    public void showDeletionConfirmationPopup(Promotion promotion) {
        deletionConfirmationPopup.setVisibility(View.VISIBLE);

        Button btnConfirmYa = findViewById(R.id.btn_confirm_ya);
        Button btnConfirmTidak = findViewById(R.id.btn_confirm_tidak);

        btnConfirmYa.setOnClickListener(v -> {
            deletePromotion(promotion);
            deletionConfirmationPopup.setVisibility(View.GONE);
        });

        btnConfirmTidak.setOnClickListener(v -> deletionConfirmationPopup.setVisibility(View.GONE));
    }

    private void deletePromotion(Promotion promotion) {
        if (promotion.getImageUrl() != null && !promotion.getImageUrl().isEmpty()) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(promotion.getImageUrl());
            storageReference.delete().addOnSuccessListener(aVoid -> deletePromotionDocument(promotion)).addOnFailureListener(e -> deletePromotionDocument(promotion));
        } else {
            deletePromotionDocument(promotion);
        }
    }

    private void deletePromotionDocument(Promotion promotion) {
        auth.deleteDocumentData("promotions", promotion.getId(), new Authentication.FirebaseDocumentDeleteCallback() {
            @Override
            public void onSuccess() {
                promotionList.remove(promotion);
                adapter.notifyDataSetChanged();
                updateVisibility();
                Toast.makeText(PromotionViewActivity.this, "Data promosi terhapus", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(PromotionViewActivity.this, "Gagal menghapus data promosi: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}