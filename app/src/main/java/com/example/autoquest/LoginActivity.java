package com.example.autoquest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.autoquest.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();;
    private ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EditText emailInput = binding.emailInput;
        EditText passwordInput = binding.passwordInput;

        binding.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();
                if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    if (!password.isEmpty()) {
                        firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Toast.makeText(LoginActivity.this, "Успешный вход " + firebaseAuth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity.this, "Не удалось войти", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        passwordInput.setError("Пароль не может быть пустым");
                    }
                } else if (email.isEmpty()){
                    emailInput.setError("Почта не может быть пустой");
                } else {
                    emailInput.setError("Пожалуйста, введите почту правильно");
                }
            }
        });
        binding.continueWithoutAccountButton.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, MainActivity.class)));
        binding.resetPasswordButton.setOnClickListener(v -> showDialogResetPassword());
        binding.signupButton.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, SignUpActivity.class)));
    }

    private void showDialogResetPassword () {
    // Создаем билдер для AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Получаем LayoutInflater
        LayoutInflater inflater = this.getLayoutInflater();

        // Создаем View для диалогового окна из пользовательского макета
        View dialogView = inflater.inflate(R.layout.dialog_change_data, null);
        builder.setView(dialogView);

        TextView title = dialogView.findViewById(R.id.dialogTitle);
        Button acceptButton = dialogView.findViewById(R.id.saveButton);
        EditText editText = dialogView.findViewById(R.id.newDataInput);

        title.setText("Восстановление пароля");
        editText.setHint("E-mail");
        editText.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(this,R.drawable.mail), null, null, null);
        acceptButton.setText("Отправить");

        AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editText.getText().toString().trim();
                if (email.isEmpty()) {
                    editText.setError("Введите ваш email");
                    dialog.dismiss();
                    return;
                }

                firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Инструкции по восстановлению пароля отправлены на ваш email", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(LoginActivity.this, "Ошибка отправки инструкции по восстановлению пароля", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }
                    }
                });
            }
        });
        dialog.show();
    }
}
