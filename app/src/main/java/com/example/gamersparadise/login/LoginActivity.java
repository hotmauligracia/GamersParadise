package com.example.gamersparadise.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gamersparadise.admin.AdminActivity;
import com.example.gamersparadise.customer.CustomerActivity;
import com.example.gamersparadise.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(v -> {
            Intent adminActivity = new Intent(LoginActivity.this, AdminActivity.class);
            startActivity(adminActivity);
        });
    }

    public void onGoogleLoginClick(View view) {
        Intent customerActivity = new Intent(LoginActivity.this, CustomerActivity.class);
        startActivity(customerActivity);
    }

    public void onRegisterClick(View view) {
        Intent register = new Intent(LoginActivity.this,SignUpActivity.class);
        startActivity(register);
    }
}