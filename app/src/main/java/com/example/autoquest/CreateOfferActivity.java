package com.example.autoquest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.autoquest.databinding.CreateOfferActivityLayoutBinding;
import com.example.autoquest.databinding.CreateOfferActivityLayoutBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class CreateOfferActivity extends AppCompatActivity {

    private CreateOfferActivityLayoutBinding addOfferActivityLayoutBinding;
    private static final String TAG = "AddOfferActivity";
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();;
    private CollectionReference collectionReference = firestore.collection("offers");;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addOfferActivityLayoutBinding = CreateOfferActivityLayoutBinding.inflate(getLayoutInflater());
        setContentView(addOfferActivityLayoutBinding.getRoot());

        // Button Go to CAR LIST ACTIVITY
        addOfferActivityLayoutBinding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateOfferActivity.this, MainActivity.class));
            }
        });

        addOfferActivityLayoutBinding.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GridItem item = new GridItem(
                        addOfferActivityLayoutBinding.brandET.getText().toString(),
                        addOfferActivityLayoutBinding.modelET.getText().toString(),
                        addOfferActivityLayoutBinding.generationET.getText().toString(),
                        addOfferActivityLayoutBinding.priceET.getText().toString(),
                        addOfferActivityLayoutBinding.yearET.getText().toString()
                );

                // adding data to firestore database
                addDataToFirestore(item);
                startActivity(new Intent(CreateOfferActivity.this, MainActivity.class));
            }
        });
    }

    private void addDataToFirestore(GridItem item) {
        collectionReference.add(item).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                Toast.makeText(CreateOfferActivity.this, "Данные успешно добавлены", Toast.LENGTH_SHORT).show();
                addOfferActivityLayoutBinding.brandET.setText("");
                addOfferActivityLayoutBinding.modelET.setText("");
                addOfferActivityLayoutBinding.generationET.setText("");
                addOfferActivityLayoutBinding.priceET.setText("");
                addOfferActivityLayoutBinding.yearET.setText("");
                startActivity(new Intent(CreateOfferActivity.this, MainActivity.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error adding document", e);
                Toast.makeText(CreateOfferActivity.this, "Не удалось добавить данные", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
