package com.example.autoquest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.ViewHolder> {
    private ArrayList<Car> arrayList;
    private Context context;

    // constructor
    public CarAdapter(ArrayList<Car> arrayList, Context context) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // on below line we are setting data
        // to our views of recycler view item.
        Car car = arrayList.get(position);
        holder.brandTV.setText(car.getBrand());
        holder.modelTV.setText(car.getModel());
        holder.yearTV.setText(String.valueOf(car.getYear()));
        holder.priceTV.setText(String.format("%s ₽", String.valueOf(car.getPrice())));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView brandTV, modelTV, yearTV, priceTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            brandTV = itemView.findViewById(R.id.brandTextView);
            modelTV = itemView.findViewById(R.id.modelTextView);
            yearTV = itemView.findViewById(R.id.yearTextView);
            priceTV = itemView.findViewById(R.id.priceValue);
        }
    }
}

