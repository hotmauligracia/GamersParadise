package com.example.gamersparadise.admin.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.gamersparadise.Authentication;
import com.example.gamersparadise.R;
import com.example.gamersparadise.admin.home.facility.FacilityViewActivity;
import com.example.gamersparadise.admin.home.location.LocationViewActivity;
import com.example.gamersparadise.admin.home.menu.MenuViewActivity;
import com.example.gamersparadise.admin.home.promotion.PromotionViewActivity;

public class HomeAdminFragment extends Fragment {

    public HomeAdminFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home_admin, container, false);

        TextView tvUsername = view.findViewById(R.id.tv_username);
        Button btnLogout = view.findViewById(R.id.btn_logout);

        Authentication auth = new Authentication();

        String username = auth.getCurrentUser().getDisplayName();
        tvUsername.setText(String.format("Hai, %s", username));

        btnLogout.setOnClickListener(v -> auth.logoutUser(requireContext()));

        CardView cvPromotion = view.findViewById(R.id.cv_promotion);
        cvPromotion.setOnClickListener(this::onPromotionViewClick);

        CardView cvLocation = view.findViewById(R.id.cv_location);
        cvLocation.setOnClickListener(this::onLocationViewClick);

        CardView cvFacility = view.findViewById(R.id.cv_facility);
        cvFacility.setOnClickListener(this::onFacilityViewClick);

        CardView cvMenu = view.findViewById(R.id.cv_menu);
        cvMenu.setOnClickListener(this::onMenuViewClick);

        return view;
    }

    public void onPromotionViewClick(View view) {
        Intent promotionActivity = new Intent(requireContext(), PromotionViewActivity.class);
        startActivity(promotionActivity);
    }

    public void onLocationViewClick(View view) {
        Intent locationActivity = new Intent(requireContext(), LocationViewActivity.class);
        startActivity(locationActivity);
    }

    public void onFacilityViewClick(View view) {
        Intent facilityActivity = new Intent(requireContext(), FacilityViewActivity.class);
        startActivity(facilityActivity);
    }

    public void onMenuViewClick(View view) {
        Intent menuActivity = new Intent(requireContext(), MenuViewActivity.class);
        startActivity(menuActivity);
    }
}