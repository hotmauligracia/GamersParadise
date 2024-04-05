package com.example.gamersparadise.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.gamersparadise.R;
import com.example.gamersparadise.customer.reservasi.ReservasiActivity;

public class HomeCustomerFragment extends Fragment {

    public HomeCustomerFragment(){
        // require a empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home_customer, container, false);

        TextView tvLabel1 = view.findViewById(R.id.tv_label1);

        tvLabel1.setOnClickListener(this::onFacilitiesClick);

        return view;
    }

    public void onFacilitiesClick(View view) {
        Intent reservasi = new Intent(requireContext(), ReservasiActivity.class);
        startActivity(reservasi);
    }

    public void onMenuClick(View view) {
    }
}
