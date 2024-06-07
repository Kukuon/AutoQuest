package com.example.autoquest;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.autoquest.databinding.FragmentProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;

public class ProfileFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 22;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

    private StorageReference storageReferenceAvatar = FirebaseStorage.getInstance().getReference("uploads").child("user_avatars/" + firebaseAuth.getUid());
    private DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(firebaseUser.getUid());

    private FragmentProfileBinding binding;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Инициализация binding
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        // Проверяем, вошел ли пользователь в систему
        if (firebaseUser != null) {

            updateImageView();

            // Получение имени и телефона из RealtimeDatabase с добавлением слушателей изменений
            userRef.child("name").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String name = snapshot.getValue(String.class);
                    if (name != null) {
                        binding.usernameTV.setText(name);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Обработка ошибок
                }
            });

            userRef.child("phoneNumber").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String phoneNumber = snapshot.getValue(String.class);
                    if (phoneNumber != null) {
                        binding.phoneTV.setText(phoneNumber);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Обработка ошибок
                }
            });

            binding.emailTV.setText(firebaseUser.getEmail());
            binding.idTV.setText("ID " + firebaseUser.getUid());

            // кнопки в toolbar
            binding.informationButton.setOnClickListener(v -> startActivity(new Intent(getContext(), InformationActivity.class)));
            binding.exitButton.setOnClickListener(v -> showLogoutConfirmationDialog());

            // кнопка Мои объявления
            binding.myOffersButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (firebaseUser != null) {
                        startActivity(new Intent(getContext(), MyOffersActivity.class));
                    } else {
                        Toast.makeText(getContext(), "Невозможно открыть объявления", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            

            // кнопки редактирования профиля
            binding.changeNameButton.setOnClickListener(v -> showChangeDataDialog("Изменить имя", 0));
            binding.phoneTV.setOnClickListener(v -> showChangeDataDialog("Изменить телефон", 1));
            binding.passwordTV.setOnClickListener(v -> showChangeDataDialog("Изменить пароль", 2));
            // обновление аватарки
            binding.avatarImageView.setOnClickListener(v ->  selectImage());

            return binding.getRoot(); // Возвращаем корневой вид разметки
        } else {
            // В макете для неавторизованного пользователя
            View rootView = inflater.inflate(R.layout.fragment_unlogged, container, false);
            // Находим кнопку для перехода на активити входа в аккаунт
            Button goToLoginButton = rootView.findViewById(R.id.goToLoginButton);
            // Устанавливаем обработчик нажатия на кнопку
            goToLoginButton.setOnClickListener(v -> startActivity(new Intent(getContext(), LoginActivity.class)));
            return rootView;
        }
    }

    private void selectImage() {
        // Создаем Intent для выбора изображения из галереи
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Выберите изображение"), PICK_IMAGE_REQUEST);
    }

    private void updateImageView() {
        // Загружаем изображение в виде массива байт
        final long ONE_MEGABYTE = 1024 * 1024;
        storageReferenceAvatar.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Получаем загруженные байты изображения
                // Преобразуем их в Bitmap
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                binding.avatarImageView.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getContext(), "Ошибка в загрузке фото профиля", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            // Получаем URI выбранного изображения
            Uri filePath = data.getData();

            // Загружаем изображение в Firebase Storage
            if (filePath != null) {
                storageReferenceAvatar
                        .putFile(filePath)
                        .addOnSuccessListener(taskSnapshot -> {
                            // Изображение успешно загружено
                            Toast.makeText(getActivity(), "Изображение успешно загружено", Toast.LENGTH_SHORT).show();
                            updateImageView();
                        })
                        .addOnFailureListener(e -> {
                            // Возникла ошибка при загрузке изображения
                            Toast.makeText(getActivity(), "Ошибка загрузки изображения: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });

            }
        }
    }


    private void showLogoutConfirmationDialog() {
        // Создаем билдер для AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        // Получаем LayoutInflater
        LayoutInflater inflater = this.getLayoutInflater();

        // Создаем View для диалогового окна из пользовательского макета
        View dialogView = inflater.inflate(R.layout.dialog_confirmation, null);
        builder.setView(dialogView);

        // Настраиваем содержимое диалогового окна
        TextView title = dialogView.findViewById(R.id.dialog_title);
        title.setText("Выйти из аккаунта?");

        ImageButton acceptButton = dialogView.findViewById(R.id.acceptButton);
        ImageButton cancelButton = dialogView.findViewById(R.id.cancelButton);

        AlertDialog dialog = builder.create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new ProfileFragment()).commit();
                dialog.dismiss();
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


    private void showChangeDataDialog(String parameterName, int mode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_change_data, null);
        builder.setView(dialogView);

        TextView title = dialogView.findViewById(R.id.dialogTitle);
        EditText editText = dialogView.findViewById(R.id.newDataInput);
        Button saveButton = dialogView.findViewById(R.id.saveButton);

        title.setText(parameterName);

        AlertDialog dialog = builder.create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newData = editText.getText().toString();

                if (firebaseUser != null) {
                    if (!newData.isEmpty()) {
                        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                                .child("users").child(firebaseUser.getUid());

                        switch (mode) {
                            case 0:
                                userRef.child("name").setValue(newData)
                                        .addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(requireContext(), "Имя изменено на " + newData, Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(requireContext(), "Не удалось изменить имя", Toast.LENGTH_SHORT).show();
                                            }
                                            dialog.dismiss();
                                        });
                                break;

                            case 1:
                                userRef.child("phoneNumber").setValue(newData)
                                        .addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(requireContext(), "Телефон изменен на " + newData, Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(requireContext(), "Не удалось изменить телефон", Toast.LENGTH_SHORT).show();
                                            }
                                            dialog.dismiss();
                                        });
                                break;

                            case 2:
                                firebaseUser.updatePassword(newData)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d("UpdatePassword", "User password updated.");
                                                    Toast.makeText(getContext(), "Пароль успешно изменен", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Log.d("UpdatePassword", "Error password not updated.", task.getException());
                                                    Toast.makeText(getContext(), "Ошибка при изменении пароля", Toast.LENGTH_SHORT).show();
                                                }
                                                dialog.dismiss();
                                            }
                                        });
                                break;
                        }
                    } else {
                        editText.setError("Поле ввода должно быть заполнено");
                    }
                }
            }
        });
        dialog.show();
    }
}
