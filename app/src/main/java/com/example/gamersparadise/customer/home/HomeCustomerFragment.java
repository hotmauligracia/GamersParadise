package com.example.gamersparadise.customer.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.gamersparadise.Authentication;
import com.example.gamersparadise.R;
import com.example.gamersparadise.customer.home.facility.FacilityActivity;
import com.example.gamersparadise.customer.home.menu.MenuActivity;

public class HomeCustomerFragment extends Fragment {

    public HomeCustomerFragment(){
        // require a empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home_customer, container, false);

        TextView tvUsername = view.findViewById(R.id.textView);
        Authentication auth = new Authentication();

        String username = auth.getCurrentUser().getDisplayName();
        tvUsername.setText(String.format("Hai, %s", username));

        TextView tvLabel1 = view.findViewById(R.id.tv_label1);
        tvLabel1.setOnClickListener(this::onFacilitiesClick);

        TextView tvLabel2 = view.findViewById(R.id.tv_label2);
        tvLabel2.setOnClickListener(this::onMenuClick);

        return view;
    }

    public void onFacilitiesClick(View view) {
        Intent reservasi = new Intent(requireContext(), FacilityActivity.class);
        startActivity(reservasi);
    }

    public void onMenuClick(View view) {
        Intent menu = new Intent(requireContext(), MenuActivity.class);
        startActivity(menu);
    }
}
