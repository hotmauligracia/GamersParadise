package com.example.gamersparadise.customer.home.menu;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gamersparadise.R;
import com.example.gamersparadise.data.Menu;

public class ViewMenuActivity extends AppCompatActivity {

    private static final String TAG = "ViewMenuActivity";

    private Menu menu;
    private ImageView imgViewMenu;
    private TextView tvViewFacilityName, tvViewFacilityCapacity, tvViewFacilityPrice, tvViewFacilityDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_menu);
    }
}