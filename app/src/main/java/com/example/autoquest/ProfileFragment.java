package com.example.autoquest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.autoquest.databinding.FragmentProfileLoggedBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProfileFragment extends Fragment {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

    private BottomNavigationView bottomNavigationView;

    private FragmentProfileLoggedBinding binding;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Проверяем, вошел ли пользователь в систему
        if (firebaseUser != null) {
            binding = FragmentProfileLoggedBinding.inflate(inflater, container, false);
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

            // Получаем ссылку на узел с информацией о пользователе
            DatabaseReference userRef = databaseReference.child("users").child(firebaseUser.getUid());
            StorageReference storageRef = FirebaseStorage.getInstance().getReference("uploads").child("user_avatars/" + firebaseAuth.getUid());

            // Получение имени и телефона из RealtimeDatabase
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String name = snapshot.child("name").getValue(String.class);
                    String phoneNumber = snapshot.child("phoneNumber").getValue(String.class);

                    // Установка значений для TextView
                    binding.usernameTV.setText(name);
                    binding.phoneTV.setText(phoneNumber);
                    binding.emailTV.setText(firebaseUser.getEmail());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

            // Загружаем изображение в виде массива байт
            final long ONE_MEGABYTE = 1024 * 1024;
            storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    // Получаем успешно загруженные байты изображения
                    // Преобразуем их в Bitmap
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    binding.avatarImageView.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Обработка ошибки
                }
            });

            // выход из аккаунта firebase
            binding.exitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    firebaseAuth.signOut();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new ProfileFragment()).commit();
                }
            });

            return binding.getRoot(); // Возвращаем корневой вид разметки
        } else {
            // В макете для неавторизованного пользователя
            View rootView = inflater.inflate(R.layout.fragment_profile_unlogged, container, false);

            // Находим кнопку для перехода на активити входа в аккаунт
            Button goToLoginButton = rootView.findViewById(R.id.goToLoginButton);

            // Устанавливаем обработчик нажатия на кнопку
            goToLoginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Запускаем активити входа в аккаунт
                    startActivity(new Intent(getContext(), LoginActivity.class));
                }
            });
            return rootView;
        }
    }

}