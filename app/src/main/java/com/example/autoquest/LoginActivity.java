package com.example.autoquest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.autoquest.databinding.LoginLayoutBinding;


public class LoginActivity extends AppCompatActivity {

    private LoginLayoutBinding loginBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBinding = LoginLayoutBinding.inflate(getLayoutInflater());
        setContentView(loginBinding.getRoot());


        // Button Go to CAR LIST ACTIVITY  without registration
        loginBinding.noAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, CarListActivity.class);
                startActivity(intent);
            }
        });

        // Button Go to REGISTRATION ACTIVITY
        loginBinding.registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }
}
