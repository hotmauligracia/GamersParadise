package com.example.gamersparadise.customer;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gamersparadise.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CustomerActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationViewCustomer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        bottomNavigationViewCustomer
                = findViewById(R.id.bottom_navigation_view_customer);
        bottomNavigationViewCustomer
                .setOnNavigationItemSelectedListener(this);
        bottomNavigationViewCustomer.setSelectedItemId(R.id.home_customer);
    }

    HomeCustomerFragment homeCustomerFragment = new HomeCustomerFragment();
    HistoryFragment historyFragment = new HistoryFragment();
    ProfileFragment profileFragment = new ProfileFragment();

    @Override
    public boolean
    onNavigationItemSelected(@NonNull MenuItem item)
    {

        if (item.getItemId() == R.id.home_customer){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fl_fragment_customer, homeCustomerFragment)
                    .commit();
            return true;
        } else if (item.getItemId() == R.id.history) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fl_fragment_customer, historyFragment)
                    .commit();
            return true;
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fl_fragment_customer, profileFragment)
                    .commit();
            return true;
        }
    }
}