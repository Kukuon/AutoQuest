package com.example.autoquest;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.autoquest.databinding.FragmentFavoritesBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment {

    FragmentFavoritesBinding binding;

    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    private DatabaseReference databaseReferenceOffers = FirebaseDatabase.getInstance().getReference("offers");
    private DatabaseReference databaseReferenceUser;
    private GridAdapter gridAdapter;

    private List<Offer> favoriteOfferList = new ArrayList<>();

    public FavoritesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFavoritesBinding.inflate(getLayoutInflater());
        gridAdapter = new GridAdapter(getActivity(), new ArrayList<>());
        binding.gridOffers.setAdapter(gridAdapter);

        if (firebaseUser != null) {
            databaseReferenceUser = FirebaseDatabase.getInstance().getReference("users")
                                                                .child(firebaseUser.getUid())
                                                                .child("favorites_offers");
            loadFavoriteOffers();

            return binding.getRoot();
        } else {
            View rootView = inflater.inflate(R.layout.fragment_unlogged, container, false);
            Button goToLoginButton = rootView.findViewById(R.id.goToLoginButton);

            goToLoginButton.setOnClickListener(v -> startActivity(new Intent(getContext(), LoginActivity.class)));

            return rootView;
        }
    }


    private void loadFavoriteOffers() {
        databaseReferenceUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                List<String> favoriteOfferIds = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    favoriteOfferIds.add(snapshot.getKey());
                }
                fetchOffers(favoriteOfferIds);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Failed to read value.", databaseError.toException());
            }
        });
    }


    private void fetchOffers(List<String> offerIds) {
        favoriteOfferList.clear();
        for (String offerId : offerIds) {
            databaseReferenceOffers.child(offerId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Offer offer = snapshot.getValue(Offer.class);
                    if (offer != null) {
                        offer.setOfferId(snapshot.getKey()); // Используем ключ узла как ID
                        favoriteOfferList.add(offer);
                        gridAdapter.updateOffers(favoriteOfferList);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("Firebase", "Failed to read value.", databaseError.toException());
                }
            });
        }
    }
}