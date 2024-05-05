package com.example.autoquest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.autoquest.databinding.AddCarLayoutBinding;

public class AddCarActivity extends AppCompatActivity {
    private AddCarLayoutBinding addCarLayoutBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        addCarLayoutBinding = AddCarLayoutBinding.inflate(getLayoutInflater());
        setContentView(addCarLayoutBinding.getRoot());

        addCarLayoutBinding.backButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backToMainIntent = new Intent(AddCarActivity.this, CarListActivity.class);
                startActivity(backToMainIntent);
            }
        });

        addCarLayoutBinding.addCarbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
