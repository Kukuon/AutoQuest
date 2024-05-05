package com.example.autoquest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.example.autoquest.databinding.AddCarLayoutBinding;
import com.example.autoquest.databinding.CarListLayoutBinding;

import java.util.ArrayList;
import java.util.List;

public class CarListActivity extends AppCompatActivity {

    List<Car> carList = new ArrayList<>();
    private CarListLayoutBinding carListBinding;
    private AddCarLayoutBinding addCarLayoutBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        carListBinding = CarListLayoutBinding.inflate(getLayoutInflater());
        setContentView(carListBinding.getRoot());

        carListBinding.addOfferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent switchToCarAddActivityIntent = new Intent(CarListActivity.this, AddCarActivity.class);
                startActivity(switchToCarAddActivityIntent);
            }
        });


        CarAdapter adapter = new CarAdapter(this, carList);

        ListView listView = findViewById(R.id.carList);
        listView.setAdapter(adapter);


    }

    protected void saveToDB() {
        SQLiteDatabase database = new CarDatabaseHelper(this).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.Car.COLUMN_BRAND, addCarLayoutBinding.brandEditText.getText().toString());
        values.put(DBContract.Car.COLUMN_MODEL, addCarLayoutBinding.modelEditText.getText().toString());
        values.put(DBContract.Car.COLUMN_YEAR, addCarLayoutBinding.yearEditText.getText().toString());
        values.put(DBContract.Car.COLUMN_PRICE, addCarLayoutBinding.priceEditText.getText().toString());
        values.put(DBContract.Car.COLUMN_POWER, addCarLayoutBinding.powerEditText.getText().toString());

        long newRowId = database.insert(DBContract.Car.TABLE_NAME, null, values);
    }
}