package com.example.gamersparadise.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gamersparadise.Authentication;
import com.example.gamersparadise.admin.AdminActivity;
import com.example.gamersparadise.customer.CustomerActivity;
import com.example.gamersparadise.R;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword;
    private Authentication auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        Button btnLogin = findViewById(R.id.btn_login);

        auth = new Authentication();

        btnLogin.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(LoginActivity.this,
                        R.string.error_field_kosong,
                                Toast.LENGTH_SHORT)
                        .show();
                return;
            }

            auth.loginUser(LoginActivity.this, email, password, new Authentication.FirebaseLoginCallback() {
                @Override
                public void onSuccess(boolean isAdmin) {
                    if (isAdmin) {
                        startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                    } else {
                        startActivity(new Intent(LoginActivity.this, CustomerActivity.class));
                    }
                    finish();
                }

                @Override
                public void onFailure() {
                    Toast.makeText(LoginActivity.this, "Login gagal.", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    public void onGoogleLoginClick(View view) {
        Toast.makeText(this, R.string.coming_soon, Toast.LENGTH_SHORT).show();
    }

    public void onRegisterClick(View view) {
        Intent register = new Intent(LoginActivity.this,SignUpActivity.class);
        startActivity(register);
    }
}