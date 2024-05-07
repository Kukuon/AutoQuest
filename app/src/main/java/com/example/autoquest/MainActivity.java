package com.example.autoquest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.autoquest.databinding.MainActivityLayoutBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Car> arrayList;
    private  MainActivityLayoutBinding mainActivityLayoutBinding;
    private DBHandler dbHandler;
    private RecyclerView recyclerView;
    private CarAdapter carAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivityLayoutBinding = mainActivityLayoutBinding.inflate(getLayoutInflater());
        setContentView(mainActivityLayoutBinding.getRoot());

        arrayList = new ArrayList<>();
        dbHandler = new DBHandler(MainActivity.this);

        arrayList = dbHandler.readCars();

        carAdapter = new CarAdapter(arrayList, MainActivity.this);
        recyclerView = findViewById(R.id.RVList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(carAdapter);

        mainActivityLayoutBinding.addOfferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddOfferActivity.class);
                startActivity(intent);
            }
        });

        mainActivityLayoutBinding.accountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
            }
        });
    }
}