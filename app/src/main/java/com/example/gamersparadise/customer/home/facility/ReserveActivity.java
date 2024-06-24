package com.example.gamersparadise.customer.home.facility;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.*;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gamersparadise.Authentication;
import com.example.gamersparadise.R;
import com.example.gamersparadise.customer.home.facility.adapter.TimeSlotAdapter;
import com.example.gamersparadise.data.Facility;
import com.example.gamersparadise.data.Promotion;
import com.example.gamersparadise.data.PromotionType;
import com.example.gamersparadise.data.Reservation;
import com.example.gamersparadise.data.User;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ReserveActivity extends AppCompatActivity {

    private static final String TAG = "ReserveActivity";

    private User user;
    private Facility facility;
    private Promotion promotion;
    private PromotionType promotionType;
    private EditText edtName, edtPhone, edtDate;
    private RecyclerView rvTime;
    private ImageView facilityImg;
    private TextView tvFacilityName, tvFacilityCapacity, tvFacilityDetails, tvFacilityPrice, tvPaymentAmount, tvDeletePromotion;
    private View tfDate, promotionBar;
    private Button btnReserve;
    private ImageButton btnSeePromotion;
    private TimeSlotAdapter timeSlotAdapter;
    private Set<String> selectedTimeSlots;
    private float subtotal, discountNominal;
    private Authentication auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve);

        auth = new Authentication();

        MaterialToolbar topAppBar = findViewById(R.id.top_app_bar);
        ImageView backButton = findViewById(R.id.toolbar_back_icon);

        edtName = findViewById(R.id.edt_name);
        edtPhone = findViewById(R.id.edt_phone);
        edtDate = findViewById(R.id.edt_date);
        rvTime = findViewById(R.id.rv_time);
        facilityImg = findViewById(R.id.facility_img);
        tvFacilityName = findViewById(R.id.tv_facility_name);
        tvFacilityCapacity = findViewById(R.id.tv_facility_capacity);
        tvFacilityDetails = findViewById(R.id.tv_facility_details);
        tvFacilityPrice = findViewById(R.id.tv_facility_price);
        tvPaymentAmount = findViewById(R.id.tv_payment_amount);
        tfDate = findViewById(R.id.tf_date);
        promotionBar = findViewById(R.id.promotion_bar);
        btnReserve = findViewById(R.id.btn_reserve);
        btnSeePromotion = findViewById(R.id.btn_see_promotion);
        tvDeletePromotion = findViewById(R.id.tv_delete_promotion);

        setSupportActionBar(topAppBar);
        getSupportActionBar().setTitle(null);
        backButton.setOnClickListener(v -> onBackPressed());

        Intent intent = getIntent();
        if (intent.hasExtra("facility")) {
            facility = intent.getParcelableExtra("facility");
            if (facility != null) {
                displayFacilityDetails(facility);
            } else {
                Log.e(TAG, "Facility extra is null");
            }
        } else {
            Log.e(TAG, "Intent or facility extra is null");
        }

        selectedTimeSlots = new HashSet<>();

        tfDate.setOnClickListener(v -> showDatePicker(edtDate));

        promotionBar.setOnClickListener(v -> {
            Intent voucherIntent = new Intent(ReserveActivity.this, VoucherFacilityActivity.class);
            startActivityForResult(voucherIntent, 1);
        });

        fetchUserData();
        setupRecyclerView();
    }

    private void fetchUserData() {
        auth.fetchDocumentData("users/" + auth.getCurrentUser().getUid(), new Authentication.FirebaseDocumentCallback(){

            @Override
            public void onSuccess(DocumentSnapshot document) {
                user = document.toObject(User.class);
                if (user != null) {
                    edtName.setText(user.getName());
                }
            }
            @Override
            public void onFailure(String errorMessage) {
                Log.e(TAG, "Gagal mengambil data user: " + errorMessage);
            }
        });
    }

    private void displayFacilityDetails(Facility facility) {
        Glide.with(this).load(facility.getImageUrl()).into(facilityImg);
        tvFacilityName.setText(facility.getName());
        tvFacilityCapacity.setText(String.format("%s orang", facility.getCapacity()));
        tvFacilityPrice.setText(String.format("%s/jam", facility.getFormattedPrice()));
        tvFacilityDetails.setText(facility.getDetails());
    }

    private void showDatePicker(EditText editText) {
        Calendar calendar = Calendar.getInstance();

        if (!TextUtils.isEmpty(editText.getText().toString())) {
            try {
                String[] dateParts = editText.getText().toString().split("-");
                int day = Integer.parseInt(dateParts[0]);
                int month = Integer.parseInt(dateParts[1]) - 1;
                int year = Integer.parseInt(dateParts[2]);
                calendar.set(year, month, day);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    editText.setText(String.format("%02d-%02d-%04d", dayOfMonth, month + 1, year));
                    fetchReservationData();
                }, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    private void setupRecyclerView() {
        rvTime.setLayoutManager(new GridLayoutManager(this, 4));
        timeSlotAdapter = new TimeSlotAdapter(this::onTimeSlotClick);
        rvTime.setAdapter(timeSlotAdapter);
        populateTimeSlots();
    }

    private void populateTimeSlots() {
        List<String> timeSlots = new ArrayList<>();
        for (int i = 10; i < 24; i++) {
            timeSlots.add(String.format("%02d:00-%02d:00", i, (i + 1) % 24));
        }
        for (int i = 0; i < 4; i++) {
            timeSlots.add(String.format("%02d:00-%02d:00", i, (i + 1) % 24));
        }
        timeSlotAdapter.setTimeSlots(timeSlots);
    }

    private void onTimeSlotClick(String timeSlot) {
        if (selectedTimeSlots.contains(timeSlot)) {
            selectedTimeSlots.remove(timeSlot);
        } else {
            selectedTimeSlots.add(timeSlot);
        }
        timeSlotAdapter.setSelectedTimeSlots(selectedTimeSlots);
        calculatePayment();
    }

    private void calculatePayment() {
        subtotal = selectedTimeSlots.size() * facility.getPrice();
        if (promotion != null) {
            fetchPromotionTypeData(promotion);
            discountNominal = calculateDiscount(promotionType.getOperator(), promotion.getNominal());
        } else {
            discountNominal = 0;
        }
        float totalPayment = subtotal - discountNominal;
        tvPaymentAmount.setText(String.format("Rp%.2f", totalPayment));
    }

    private void fetchPromotionTypeData(Promotion promotion) {
        auth.fetchDocumentData("promotionTypes/" + promotion.getPromotionTypeId(), new Authentication.FirebaseDocumentCallback() {
            @Override
            public void onSuccess(DocumentSnapshot document) {
                promotionType = document.toObject(PromotionType.class);
                calculatePayment();
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e(TAG, "Gagal mengambil data promotion type: " + errorMessage);
            }
        });
    }

    private float calculateDiscount(String operator, float nominal) {
        switch (operator) {
            case "* 0.01 *":
                return subtotal * 0.01f * nominal;
            case "-":
                return nominal;
            default:
                return 0;
        }
    }

    public void onDeletePromotionClick(View view) {
        promotion = null;
        btnSeePromotion.setVisibility(View.VISIBLE);
        tvDeletePromotion.setVisibility(View.GONE);
        promotionBar.setClickable(true);
        calculatePayment();
    }

    public void onReserveClick(View view) {
        if (TextUtils.isEmpty(edtName.getText()) || TextUtils.isEmpty(edtPhone.getText()) ||
                TextUtils.isEmpty(edtDate.getText()) || selectedTimeSlots.isEmpty()) {
            Toast.makeText(this, "Harap isi semua field", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = auth.getCurrentUser().getUid();
        String customerName = edtName.getText().toString();
        String customerPhone = edtPhone.getText().toString();
        String date = edtDate.getText().toString();
        String timeSlots = String.join(", ", selectedTimeSlots);

        long now = System.currentTimeMillis();
        Reservation reservation = new Reservation(userId, facility.getId(), customerName, customerPhone, date + " " + timeSlots, promotion != null ? promotion.getId() : null, subtotal - discountNominal, false, "Reserved", now, now);

        auth.addDocumentData("reservations", reservation.toMap(), new Authentication.FirebaseDocumentAddCallback() {
            @Override
            public void onSuccess(String documentId) {
                Toast.makeText(ReserveActivity.this, "Reservation successful", Toast.LENGTH_SHORT).show();
                finish();
            }
            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(ReserveActivity.this, "Reservation failed: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchReservationData() {
        auth.fetchCollectionData("reservations", new Authentication.FirebaseCollectionCallback() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                List<String> reservedSlots = new ArrayList<>();
                for (QueryDocumentSnapshot document : querySnapshot) {
                    Reservation reservation = document.toObject(Reservation.class);
                    if (reservation.getFacilityId().equals(facility.getId()) && reservation.getReservationTime().startsWith(edtDate.getText().toString())) {
                        reservedSlots.addAll(reservation.getTimeSlots());
                    }
                }
                timeSlotAdapter.setReservedTimeSlots(reservedSlots);
            }
            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(ReserveActivity.this, "Gagal memuat reservasi: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.hasExtra("promotion")) {
            promotion = data.getParcelableExtra("promotion");
            btnSeePromotion.setVisibility(View.GONE);
            tvDeletePromotion.setVisibility(View.VISIBLE);
            promotionBar.setClickable(false);
            calculatePayment();
        }
    }
}