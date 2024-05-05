package com.example.autoquest;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.autoquest.databinding.RegistrationLayoutBinding;

public class RegistrationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RegistrationLayoutBinding registrationLayoutBinding;
        setContentView(registrationLayoutBinding.getRoot());


    }
}
