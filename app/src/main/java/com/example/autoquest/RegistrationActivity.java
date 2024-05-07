package com.example.autoquest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.autoquest.databinding.RegistrationActivityLayoutBinding;

public class RegistrationActivity extends AppCompatActivity {

    private RegistrationActivityLayoutBinding registrationActivityLayoutBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registrationActivityLayoutBinding = RegistrationActivityLayoutBinding.inflate(getLayoutInflater());
        setContentView(registrationActivityLayoutBinding.getRoot());

        // Button Go to LOGIN ACTIVITY
        registrationActivityLayoutBinding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


    }
}
