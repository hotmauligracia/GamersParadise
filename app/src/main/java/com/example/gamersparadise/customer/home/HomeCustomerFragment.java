package com.example.gamersparadise.customer.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.gamersparadise.Authentication;
import com.example.gamersparadise.R;
import com.example.gamersparadise.customer.home.facility.FacilityActivity;
import com.example.gamersparadise.customer.home.menu.MenuActivity;
import com.example.gamersparadise.customer.home.notification.NotificationActivity;
import com.example.gamersparadise.customer.profile.ProfileFragment;

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

        ImageView imgNotif = view.findViewById(R.id.image_notif);
        imgNotif.setOnClickListener(this::onNotifClick);

        ImageView imgProfile = view.findViewById(R.id.image_profile);
        imgProfile.setOnClickListener(this::onProfileClick);

        return view;
    }

    private void onProfileClick(View view) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace or add the ProfileFragment to your layout
        ProfileFragment profileFragment = new ProfileFragment();
        fragmentTransaction.replace(R.id.fl_fragment_customer, profileFragment);
        fragmentTransaction.addToBackStack(null); // Optional, to add the transaction to the back stack
        fragmentTransaction.commit();
    }

    private void onNotifClick(View view) {
        Intent notif = new Intent(requireContext(), NotificationActivity.class);
        startActivity(notif);
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
