package com.example.autoquest;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class GridAdapter extends BaseAdapter {

    private Context context;
    private List<Offer> offerList;

    public GridAdapter(Context context, List<Offer> items) {
        this.context = context;
        offerList = items;
    }

    // Очистить список элементов
    public void clear() {
        offerList.clear();
        notifyDataSetChanged();
    }

    // Добавить элемент в список
    public void addItem(Offer item) {
        offerList.add(item);
        notifyDataSetChanged();
    }

    public void setItems(List<Offer> items) {
        offerList = items;
    }

    @Override
    public int getCount() {
        return offerList.size();
    }

    @Override
    public Object getItem(int position) {
        return offerList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_item_layout, parent, false);

            holder.textViewBrand = (TextView) convertView.findViewById(R.id.brandTV); // Добавлено для бренда
            holder.textViewModel = (TextView) convertView.findViewById(R.id.modelTV); // Добавлено для модели
            holder.textViewGeneration = (TextView) convertView.findViewById(R.id.generationTV); // Добавлено для поколения
            holder.textViewPrice = (TextView) convertView.findViewById(R.id.priceTV); // Добавлено для цены
            holder.textViewYear = (TextView) convertView.findViewById(R.id.yearTV);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Offer offer = offerList.get(position);

        // устанвока текстовых полей
        holder.textViewBrand.setText(offer.getBrand()); // Установка бренда
        holder.textViewModel.setText(offer.getModel()); // Установка модели
        holder.textViewGeneration.setText(offer.getGeneration()); // Установка поколения
        holder.textViewPrice.setText(offer.getPrice() + " ₽"); // Установка цены
        holder.textViewYear.setText(offer.getYear()); // установка года

        // Загружаем первую фотографию из Firebase Storage
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("uploads/offer_images/" + offer.getOfferId() + "/");

        storageReference.listAll().addOnSuccessListener(listResult -> {
            if (!listResult.getItems().isEmpty()) {
                StorageReference firstImageRef = listResult.getItems().get(0);
                Log.d("OfferAdapter", "ListResult os not empty");
                final long ONE_MEGABYTE = 1024 * 1024;
                firstImageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        holder.imageView.setImageBitmap(bitmap);
                        Log.d("OfferAdapter", "Image loaded successfully");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("OfferAdapter", "Не удалось загрузить изображение", e);
                    }
                });
            }
        }).addOnFailureListener(e -> Log.e("OfferAdapter", "Не удалось загрузить изображение", e));

        return convertView;
    }

    public void updateOffers(List<Offer> offers) {
        this.offerList.clear();
        this.offerList.addAll(offers);
        notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView textViewBrand; // Добавлено для бренда
        TextView textViewModel; // Добавлено для модели
        TextView textViewGeneration; // Добавлено для поколения
        TextView textViewPrice; // Добавлено для цены
        TextView textViewYear;
        ImageView imageView; // изображение объявления
    }
}

