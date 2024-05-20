package com.example.autoquest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.autoquest.databinding.OfferActivityBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class OfferActivity extends AppCompatActivity {

    OfferActivityBinding binding;

    private List<String> imageUrls;

    private ImageAdapter imageAdapter;
    private ParameterAdapter parameterAdapter;

    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = OfferActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.returnButton.setOnClickListener(v -> startActivity(new Intent(OfferActivity.this, MainActivity.class)));

        String offerId = getIntent().getStringExtra("offerId");

        // Инициализируем списки для изображений и параметров
        imageUrls = new ArrayList<>();


        // Получение данных объявления
        databaseReference = FirebaseDatabase.getInstance().getReference("offers").child(offerId);
        DatabaseReference parametersRef = FirebaseDatabase.getInstance().getReference("offers").child(offerId);
        parametersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Получаем параметры из снимка данных
                    String brand = snapshot.child("brand").getValue(String.class);
                    String model = snapshot.child("model").getValue(String.class);
                    String generation = snapshot.child("generation").getValue(String.class);
                    String price = snapshot.child("price").getValue(String.class);
                    String year = snapshot.child("year").getValue(String.class);
                    String description = snapshot.child("description").getValue(String.class);
                    String enginePower = snapshot.child("enginePower").getValue(String.class);
                    String fuelConsumption = snapshot.child("fuelConsumption").getValue(String.class);
                    String ownerPhoneNumber = snapshot.child("ownerPhoneNumber").getValue(String.class);
                    String ownerId = snapshot.child("ownerId").getValue(String.class);

                    List<Parameter> parameters = new ArrayList<>();

                    // Создаем объекты параметров и добавляем их в список
                    parameters.add(new Parameter("Бренд", brand));
                    parameters.add(new Parameter("Модель", model));
                    parameters.add(new Parameter("Поколение", generation));
                    parameters.add(new Parameter("Цена", price));
                    parameters.add(new Parameter("Год", year));
                    parameters.add(new Parameter("Мощность двигателя", enginePower));
                    parameters.add(new Parameter("Расход топлива", fuelConsumption));
                    parameters.add(new Parameter("Номер владельца", ownerPhoneNumber));

                    binding.descriptionTV.setText(description);
                    binding.titleTV.setText(brand + " " + model + " " + generation);

                    GridView parametersGridView = findViewById(R.id.parametersGridView);
                    parameterAdapter = new ParameterAdapter(OfferActivity.this, parameters);
                    parametersGridView.setAdapter(parameterAdapter);

                    // Уведомляем адаптер об изменениях
                    parameterAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(OfferActivity.this, "Ошибка загрузки параметров", Toast.LENGTH_SHORT).show();
            }
        });


        // Получение списка файлов изображений из Firebase Storage
        storageReference = FirebaseStorage.getInstance().getReference("uploads/offer_images/" + offerId);
        storageReference.listAll().addOnSuccessListener(listResult -> {
            for (StorageReference item : listResult.getItems()) {
                // Получение URL-адреса для каждого изображения и добавление его в список
                item.getDownloadUrl().addOnSuccessListener(uri -> {
                    imageUrls.add(uri.toString());
                    // Уведомление адаптера об изменениях
                    imageAdapter.notifyDataSetChanged();
                }).addOnFailureListener(e -> {
                    // Обработка ошибок загрузки изображения
                    Toast.makeText(OfferActivity.this, "Ошибка загрузки изображения", Toast.LENGTH_SHORT).show();
                    Log.e("OfferActivity", "Ошибка загрузки изображения");
                });
            }
        }).addOnFailureListener(e -> {
            // Обработка ошибок получения списка изображений
            Toast.makeText(OfferActivity.this, "Ошибка получения списка изображений", Toast.LENGTH_SHORT).show();
            Log.e("OfferActivity", "Ошибка получения списка изображений");
        });




        RecyclerView imageScrollContainer = findViewById(R.id.imageScrollContainer);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        imageScrollContainer.setLayoutManager(layoutManager);
        imageAdapter = new ImageAdapter(this, imageUrls);
        imageScrollContainer.setAdapter(imageAdapter);
    }

    private void deleteOffer(Offer offer) {
        // Получите ссылку на базу данных Firebase
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("offers");
        // Удалите объявление из базы данных по его ключу
        databaseReference.child(offer.getOfferId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(OfferActivity.this, "Объявление успешно удалено", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(OfferActivity.this, "Ошибка удаления объявления", Toast.LENGTH_SHORT).show();
                Log.e("OfferActivity", "Ошибка удаления объявления", e);
            }
        });
    }
}
