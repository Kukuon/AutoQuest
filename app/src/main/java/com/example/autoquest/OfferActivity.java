package com.example.autoquest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.autoquest.databinding.ActivityOfferBinding;
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

    ActivityOfferBinding binding;

    private List<String> imageUrls;

    private ImageAdapter imageAdapter;
    private ParameterAdapter parameterAdapter;


    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

    private DatabaseReference parametersRef, userRef;
    private StorageReference storageReferenceImages, storageReferenceAvatar;

    private String ownerPhoneNumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOfferBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Получение ID объявления
        String offerId = getIntent().getStringExtra("offerId");

        // Инициализируем списки для изображений и параметров
        imageUrls = new ArrayList<>();

        binding.returnButton.setOnClickListener(v -> startActivity(new Intent(OfferActivity.this, MainActivity.class)));
        binding.addFavoriteOfferButton.setOnClickListener(v -> toggleFavoriteOffer(offerId));

        loadOfferData(offerId);

        loadOfferImages(offerId);

        binding.showPhoneButton.setOnClickListener(v -> showPhoneNumberDialog());
    }


    private void loadOfferData(String offerId) {
        // Получение данных объявления
        parametersRef = FirebaseDatabase.getInstance().getReference("offers").child(offerId);


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
                    ownerPhoneNumber = snapshot.child("ownerPhoneNumber").getValue(String.class);
                    String ownerId = snapshot.child("ownerId").getValue(String.class);

                    List<Parameter> parameters = new ArrayList<>();

                    // загрузка автарки владельца объявления
                    loadUserAvatar(ownerId);

                    // Создаем объекты параметров и добавляем их в список
                    parameters.add(new Parameter("Бренд", brand));
                    parameters.add(new Parameter("Модель", model));
                    parameters.add(new Parameter("Поколение", generation));
                    parameters.add(new Parameter("Год", year));
                    parameters.add(new Parameter("Мощность двигателя", enginePower));
                    parameters.add(new Parameter("Расход топлива", fuelConsumption));



                    binding.titleTV.setText(brand + " " + model + " " + generation);
                    binding.priceTV.setText(price + " ₽");
                    binding.descriptionTV.setText(description);

                    // установка имени пользователя в объявлении
                    userRef = FirebaseDatabase.getInstance().getReference("users").child(ownerId);
                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            binding.usernameTV.setText(snapshot.child("name").getValue(String.class));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });


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
    }


    private void loadOfferImages(String offerId) {
        // Получение списка файлов изображений из Firebase Storage
        storageReferenceImages = FirebaseStorage.getInstance().getReference("uploads/offer_images/" + offerId);
        storageReferenceImages.listAll().addOnSuccessListener(listResult -> {
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


    private void loadUserAvatar(String ownerId) {
        storageReferenceAvatar = FirebaseStorage.getInstance().getReference("uploads").child("user_avatars/" + ownerId);
        // Загружаем аватар в виде массива байт
        final long ONE_MEGABYTE = 1024 * 1024;
        storageReferenceAvatar.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                binding.avatarImageView.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Обработка ошибки
                Toast.makeText(OfferActivity.this, "Ошибка в загрузке фото профиля", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void showPhoneNumberDialog() {
        // Создаем билдер для AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Получаем LayoutInflater
        LayoutInflater inflater = this.getLayoutInflater();

        // Создаем View для диалогового окна из пользовательского макета
        View dialogView = inflater.inflate(R.layout.dialog_confirmation, null);
        builder.setView(dialogView);

        // Настраиваем содержимое диалогового окна
        TextView phoneTextView = dialogView.findViewById(R.id.dialog_phone_number);
        TextView title = dialogView.findViewById(R.id.dialog_title);
        title.setText("Позвонить на номер?");
        phoneTextView.setText(ownerPhoneNumber);

        ImageButton acceptButton = dialogView.findViewById(R.id.acceptButton);
        ImageButton cancelButton = dialogView.findViewById(R.id.cancelButton);

        // Создаем и показываем AlertDialog
        AlertDialog dialog = builder.create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                    dialIntent.setData(Uri.parse("tel:" + ownerPhoneNumber));
                    startActivity(dialIntent);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Не удалось открыть совершить вызов", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
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


    private void toggleFavoriteOffer(String offerId) {
        if (firebaseUser != null) {
            // Получаем ссылку на узел с избранными объявлениями пользователя
            userRef = FirebaseDatabase.getInstance().getReference("users").child(firebaseAuth.getUid()).child("favorites_offers");

            // Добавляем слушатель для получения текущего состояния избранных объявлений
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<String> favoritesList = new ArrayList<>();
                    if (snapshot.exists()) {
                        for (DataSnapshot favoriteSnapshot : snapshot.getChildren()) {
                            String favoriteOfferId = favoriteSnapshot.getValue(String.class);
                            favoritesList.add(favoriteOfferId);
                        }
                    }

                    // Проверяем, есть ли уже это объявление в избранном
                    if (favoritesList.contains(offerId)) {
                        // Если да, удаляем его
                        favoritesList.remove(offerId);
                        userRef.setValue(favoritesList).addOnSuccessListener(aVoid -> {
                            Toast.makeText(OfferActivity.this, "Объявление удалено из избранного", Toast.LENGTH_SHORT).show();
                            // Обновить кнопку или UI
                            binding.addFavoriteOfferButton.setImageResource(R.drawable.favorite_off_svgrepo_com);
                        }).addOnFailureListener(e -> {
                            Toast.makeText(OfferActivity.this, "Ошибка удаления из избранного", Toast.LENGTH_SHORT).show();
                            Log.e("OfferActivity", "Ошибка удаления из избранного", e);
                        });
                    } else {
                        // Если нет, добавляем его
                        favoritesList.add(offerId);
                        userRef.setValue(favoritesList).addOnSuccessListener(aVoid -> {
                            Toast.makeText(OfferActivity.this, "Объявление добавлено в избранное", Toast.LENGTH_SHORT).show();
                            // Обновить кнопку или UI
                            binding.addFavoriteOfferButton.setImageResource(R.drawable.favorite_svgrepo_com);
                        }).addOnFailureListener(e -> {
                            Toast.makeText(OfferActivity.this, "Ошибка добавления в избранное", Toast.LENGTH_SHORT).show();
                            Log.e("OfferActivity", "Ошибка добавления в избранное", e);
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(OfferActivity.this, "Ошибка получения избранного", Toast.LENGTH_SHORT).show();
                    Log.e("OfferActivity", "Ошибка получения избранного", error.toException());
                }
            });
        } else {
            Toast.makeText(this, "Чтобы добавить в избранное, необходимо войти в аккаунт", Toast.LENGTH_SHORT).show();
        }
    }


    private void editOffer() {
    }
}
