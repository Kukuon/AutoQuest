package com.example.autoquest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.autoquest.databinding.CarListLayoutBinding;

import java.util.ArrayList;
import java.util.List;

public class CarListActivity extends AppCompatActivity {

    private ArrayList<Car> arrayList;
    private CarListLayoutBinding carListBinding;
    private DBHandler dbHandler;
    private RecyclerView recyclerView;
    private CarAdapter carAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        carListBinding = CarListLayoutBinding.inflate(getLayoutInflater());
        setContentView(carListBinding.getRoot());

        arrayList = new ArrayList<>();
        dbHandler = new DBHandler(CarListActivity.this);

        arrayList = dbHandler.readCars();

        carAdapter = new CarAdapter(arrayList, CarListActivity.this);
        recyclerView = findViewById(R.id.RVList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CarListActivity.this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(carAdapter);

        carListBinding.addOfferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CarListActivity.this, AddingCarActivity.class);
                startActivity(intent);
            }
        });
    }
}