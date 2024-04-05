package com.example.gamersparadise.admin.home;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gamersparadise.R;

public class HomeAdminFragment extends Fragment {

    public HomeAdminFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home_admin, container, false);

        return view;
    }

    public void onPromotionViewClick(View view) {
    }

    public void onLocationViewClick(View view) {
    }

    public void onFacilityViewClick(View view) {
    }

    public void onMenuViewClick(View view) {
    }
}