package com.example.autoquest;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.autoquest.databinding.ActivityMyOffersBinding;
import com.example.autoquest.databinding.FragmentHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyOffersActivity extends AppCompatActivity {

    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference databaseReference;

    private ActivityMyOffersBinding binding;

    private List<Offer> userOffers;
    private GridAdapter gridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyOffersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userOffers = new ArrayList<>();
        gridAdapter = new GridAdapter(this, userOffers);

        if (currentUser != null) {
            String userId = currentUser.getUid();

            DatabaseReference offersRef = FirebaseDatabase.getInstance().getReference("offers");
            // Получаем объявления, созданные текущим пользователем
            offersRef.orderByChild("ownerId").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    userOffers.clear();
                    for (DataSnapshot offerSnapshot : dataSnapshot.getChildren()) {
                        Offer offer = offerSnapshot.getValue(Offer.class);
                        if (offer != null) {
                            userOffers.add(offer);
                        }
                    }
                    gridAdapter.notifyDataSetChanged();

                    gridAdapter = new GridAdapter(MyOffersActivity.this, userOffers);
                    binding.gridOffers.setAdapter(gridAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Обработка ошибок при получении данных
                }
            });
        }
        binding.returnButton.setOnClickListener(v -> startActivity(new Intent(MyOffersActivity.this, MainActivity.class)));
    }
}
