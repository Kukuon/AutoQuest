package com.example.autoquest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.autoquest.databinding.LoginActivityLayoutBinding;


public class LoginActivity extends AppCompatActivity {

    private LoginActivityLayoutBinding loginActivityLayoutBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginActivityLayoutBinding = LoginActivityLayoutBinding.inflate(getLayoutInflater());
        setContentView(loginActivityLayoutBinding.getRoot());


        // Button Go to CAR LIST ACTIVITY  without registration
        loginActivityLayoutBinding.noAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Button Go to REGISTRATION ACTIVITY
        loginActivityLayoutBinding.registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }
}
