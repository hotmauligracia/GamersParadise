package com.example.gamersparadise.admin.reservations;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.gamersparadise.R;

public class ReservationsFragment extends Fragment {

    public ReservationsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_reservations, container, false);

        RelativeLayout cardReservationItem = view.findViewById(R.id.card_reservation_item);
        cardReservationItem.setOnClickListener(this::onReservationsItemClick);

        return view;
    }

    public void onReservationsItemClick(View view) {
        Intent reservationDetails = new Intent(requireContext(), ReservationDetailsActivity.class);
        startActivity(reservationDetails);
    }
}