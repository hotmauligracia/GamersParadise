package com.example.gamersparadise.admin.orders;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.gamersparadise.R;

public class OrdersFragment extends Fragment {

    public OrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);

        RelativeLayout cardOrderItem = view.findViewById(R.id.card_order_item);
        cardOrderItem.setOnClickListener(this::onOrdersItemClick);

        return view;
    }

    public void onOrdersItemClick(View view) {
        Intent orderDetails = new Intent(requireContext(), OrderDetailsActivity.class);
        startActivity(orderDetails);
    }
}