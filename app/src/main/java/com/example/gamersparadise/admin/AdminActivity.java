package com.example.gamersparadise.admin;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gamersparadise.R;
import com.example.gamersparadise.admin.home.HomeAdminFragment;
import com.example.gamersparadise.admin.orders.OrdersFragment;
import com.example.gamersparadise.admin.reservations.ReservationsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationViewAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        bottomNavigationViewAdmin
                = findViewById(R.id.bottom_navigation_view_admin);
        bottomNavigationViewAdmin
                .setOnNavigationItemSelectedListener(this);
        bottomNavigationViewAdmin.setSelectedItemId(R.id.home_admin);
    }

    HomeAdminFragment homeAdminFragment = new HomeAdminFragment();
    ReservationsFragment reservationsFragment = new ReservationsFragment();
    OrdersFragment ordersFragment = new OrdersFragment();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.home_admin){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fl_fragment_admin, homeAdminFragment)
                    .commit();
            return true;
        } else if (item.getItemId() == R.id.reservation) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fl_fragment_admin, reservationsFragment)
                    .commit();
            return true;
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fl_fragment_admin, ordersFragment)
                    .commit();
            return true;
        }
    }
}