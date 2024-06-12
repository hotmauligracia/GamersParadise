package com.example.gamersparadise.onboarding;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gamersparadise.Authentication;
import com.example.gamersparadise.R;
import com.example.gamersparadise.data.User;
import com.google.android.material.appbar.MaterialToolbar;

public class SignUpActivity extends AppCompatActivity {

    private EditText edtName, edtUsername, edtEmail, edtPassword, edtConfirmPassword;
    private CheckBox cbApproval;
    private Button btnSignUp;
    private Authentication auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edtName = findViewById(R.id.edt_name);
        edtUsername = findViewById(R.id.edt_username);
        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        edtConfirmPassword = findViewById(R.id.edt_confirm_password);
        cbApproval = findViewById(R.id.cb_approval);
        btnSignUp = findViewById(R.id.btn_sign_up);

        auth = new Authentication();

        MaterialToolbar topAppBar = findViewById(R.id.top_app_bar);
        setSupportActionBar(topAppBar);

        getSupportActionBar().setTitle(null);

        ImageView backButton = findViewById(R.id.toolbar_back_icon);
        backButton.setOnClickListener(v -> onBackPressed());

        btnSignUp.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String username = edtUsername.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();
            String confirmPassword = edtConfirmPassword.getText().toString().trim();

            if (!cbApproval.isChecked()) {
                Toast.makeText(SignUpActivity.this,
                        R.string.harap_menyetujui_syarat_dan_ketentuan,
                                Toast.LENGTH_SHORT)
                        .show();
                return;
            }

            if (password.equals(confirmPassword)) {
                User user = new User(name, username, email, password, false);
                auth.registerUser(SignUpActivity.this, user);
            } else {
                Toast.makeText(SignUpActivity.this,
                        R.string.kata_sandi_tidak_sama,
                                Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    public void onGoogleSignUpClick(View view) {
    }
}