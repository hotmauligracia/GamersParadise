package com.example.gamersparadise;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onGoogleLoginClick(View view) {
    }

    public void onRegisterClick(View view) {
        Intent register = new Intent(LoginActivity.this,SignUpActivity.class);
        startActivity(register);
    }
}