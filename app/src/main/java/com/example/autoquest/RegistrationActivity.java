package com.example.autoquest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.autoquest.databinding.RegistrationLayoutBinding;

public class RegistrationActivity extends AppCompatActivity {

    private RegistrationLayoutBinding registrationLayoutBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registrationLayoutBinding = RegistrationLayoutBinding.inflate(getLayoutInflater());
        setContentView(registrationLayoutBinding.getRoot());

        // Button Go to LOGIN ACTIVITY
        registrationLayoutBinding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


    }
}
