package com.example.gamersparadise.customer.profile;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.gamersparadise.R;
import com.example.gamersparadise.onboarding.LoginActivity;

public class ProfileFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        Button medsos = view.findViewById(R.id.btn_medsos);
        medsos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.instagram.com/gamersparadise.idn");
                Intent instagram = new Intent(Intent.ACTION_VIEW, uri);
                instagram.setPackage("com.instagram.android");
                try {
                    startActivity(instagram);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, uri));
                }
            }
        });

        Button aboutUs = view.findViewById(R.id.btn_about_us);
        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent aboutDeveloperIntent = new Intent(getActivity(), AboutDeveloper.class);
                startActivity(aboutDeveloperIntent);
            }
        });

        Button logout = view.findViewById(R.id.btn_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear user session data
                clearSessionData();

                // Navigate to the login screen
                Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(loginIntent);
            }
        });

        return view;
    }

    private void clearSessionData() {
        // Assuming user session data is stored in SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }


    public ProfileFragment() {
        // Required empty public constructor
    }
}
