package com.example.autoquest;

import static com.example.autoquest.MainActivity.LOG;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.autoquest.databinding.FragmentHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private GridAdapter gridAdapter;

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    private FragmentHomeBinding fragmentHomeBinding;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GridFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentHomeBinding = FragmentHomeBinding.inflate(getLayoutInflater());
        gridAdapter = new GridAdapter(getActivity(), new ArrayList<>());
        fragmentHomeBinding.gridElement.setAdapter(gridAdapter);

        readDataFromFirestore();

        CollectionReference offersRef = FirebaseFirestore.getInstance().collection("offers");
        offersRef.addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.w("Firestore", "Listen failed.", error);
                return;
            }

            // Очищаем текущие данные в адаптере
            gridAdapter.clear();

            // Обновляем данные адаптера с новыми данными из Firestore
            for (DocumentSnapshot doc : value.getDocuments()) {
                // Получаем данные документа и добавляем их в адаптер
                GridItem item = doc.toObject(GridItem.class);
                gridAdapter.addItem(item);
            }

            // Уведомляем адаптер об изменениях
            gridAdapter.notifyDataSetChanged();
        });

        return fragmentHomeBinding.getRoot();

    }

    private void readDataFromFirestore() {
        firestore.collection("offers").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(LOG, "Данные получены");
                List<GridItem> items = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String brand = document.getString("brand");
                    String model = document.getString("model");
                    String generation = document.getString("generation");
                    String price = document.getString("price");
                    String year = document.getString("year");

                    GridItem item = new GridItem(brand, model, generation, price, year);
                    items.add(item);
                }
                gridAdapter.setItems(items);
                gridAdapter.notifyDataSetChanged();
            } else {
                Log.d(LOG, "Error getting documents from firestore", task.getException());
            }
        });
    }
}