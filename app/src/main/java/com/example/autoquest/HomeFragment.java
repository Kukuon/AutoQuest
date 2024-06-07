package com.example.autoquest;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.autoquest.databinding.FragmentHomeBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("offers");
    private GridAdapter gridAdapter;
    private List<Offer> offerList = new ArrayList<>();
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        gridAdapter = new GridAdapter(getActivity(), new ArrayList<>());
        binding.gridOffers.setAdapter(gridAdapter);

        loadOffers();

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firebaseUser != null) {
                    startActivity(new Intent(getContext(), CreateOfferActivity.class));
                } else {
                    Toast.makeText(getContext(), "Чтобы добавить объявление необходимо войти", Toast.LENGTH_LONG).show();
                }
            }
        });

        return binding.getRoot();
    }


    private void loadOffers() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Offer offer = snapshot.getValue(Offer.class);
                    if (offer != null) {
                        offer.setOfferId(snapshot.getKey()); // Используем ключ узла как ID
                        offerList.add(offer);
                    }
                }
                gridAdapter.updateOffers(offerList);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Failed to read value.", databaseError.toException());
            }
        });
    }
}
