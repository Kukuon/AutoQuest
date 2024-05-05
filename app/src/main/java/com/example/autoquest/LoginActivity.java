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

        loginBinding.noAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent switchToCarListIntent = new Intent(LoginActivity.this, CarListActivity.class);
                startActivity(switchToCarListIntent);
            }
        });
    }

}
