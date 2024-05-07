package com.example.autoquest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.autoquest.databinding.AddOfferActivityLayoutBinding;

public class AddOfferActivity extends AppCompatActivity {

    private AddOfferActivityLayoutBinding addOfferActivityLayoutBinding;

    private DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addOfferActivityLayoutBinding = AddOfferActivityLayoutBinding.inflate(getLayoutInflater());
        setContentView(addOfferActivityLayoutBinding.getRoot());

        dbHandler = new DBHandler(AddOfferActivity.this);

        // Button Add Car into Database
        addOfferActivityLayoutBinding.addCarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String brand = addOfferActivityLayoutBinding.brandEditText.getText().toString();
                String model = addOfferActivityLayoutBinding.modelEditText.getText().toString();
                int year = Integer.parseInt(addOfferActivityLayoutBinding.yearEditText.getText().toString());
                int power = Integer.parseInt(addOfferActivityLayoutBinding.powerEditText.getText().toString());
                int price = Integer.parseInt(addOfferActivityLayoutBinding.priceEditText.getText().toString());

                if (brand.isEmpty() || model.isEmpty()) {
                    Toast.makeText(AddOfferActivity.this, "Пожалуйста, введите данные", Toast.LENGTH_SHORT).show();
                    return;
                }

                dbHandler.addNewCar(brand, model, year, power, price);

                Toast.makeText(AddOfferActivity.this, "Данные об авто добавлены", Toast.LENGTH_LONG).show();

                addOfferActivityLayoutBinding.brandEditText.setText("");
                addOfferActivityLayoutBinding.modelEditText.setText("");
                addOfferActivityLayoutBinding.yearEditText.setText("");
                addOfferActivityLayoutBinding.powerEditText.setText("");
                addOfferActivityLayoutBinding.priceEditText.setText("");
            }
        });

        // Button Go to CAR LIST ACTIVITY
        addOfferActivityLayoutBinding.backButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backToMainIntent = new Intent(AddOfferActivity.this, MainActivity.class);
                startActivity(backToMainIntent);
            }
        });
    }
}
