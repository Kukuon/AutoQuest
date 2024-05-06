package com.example.autoquest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.autoquest.databinding.AddCarLayoutBinding;

public class AddingCarActivity extends AppCompatActivity {

    private AddCarLayoutBinding addCarLayoutBinding;

    private DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addCarLayoutBinding = AddCarLayoutBinding.inflate(getLayoutInflater());
        setContentView(addCarLayoutBinding.getRoot());

        dbHandler = new DBHandler(AddingCarActivity.this);

        // Button Add Car into Database
        addCarLayoutBinding.addCarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String brand = addCarLayoutBinding.brandEditText.getText().toString();
                String model = addCarLayoutBinding.modelEditText.getText().toString();
                int year = Integer.parseInt(addCarLayoutBinding.yearEditText.getText().toString());
                int power = Integer.parseInt(addCarLayoutBinding.powerEditText.getText().toString());
                int price = Integer.parseInt(addCarLayoutBinding.priceEditText.getText().toString());

                if (brand.isEmpty() || model.isEmpty()) {
                    Toast.makeText(AddingCarActivity.this, "Пожалуйста, введите данные", Toast.LENGTH_SHORT).show();
                    return;
                }

                dbHandler.addNewCar(brand, model, year, power, price);

                Toast.makeText(AddingCarActivity.this, "Данные об авто добавлены", Toast.LENGTH_LONG).show();

                addCarLayoutBinding.brandEditText.setText("");
                addCarLayoutBinding.modelEditText.setText("");
                addCarLayoutBinding.yearEditText.setText("");
                addCarLayoutBinding.powerEditText.setText("");
                addCarLayoutBinding.priceEditText.setText("");
            }
        });

        // Button Go to CAR LIST ACTIVITY
        addCarLayoutBinding.backButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backToMainIntent = new Intent(AddingCarActivity.this, CarListActivity.class);
                startActivity(backToMainIntent);
            }
        });
    }
}
