package com.example.autoquest;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import java.util.List;

public class GridAdapter extends BaseAdapter {

    private Context context;
    private List<Offer> offerList;

    public GridAdapter(Context context, List<Offer> offers) {
        this.context = context;
        this.offerList = offers;
    }

    public void clear() {
        offerList.clear();
        notifyDataSetChanged();
    }

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
            convertView = LayoutInflater.from(context).inflate(R.layout.fragment_home_grid_item, parent, false);

            holder.textViewBrand = convertView.findViewById(R.id.brandTV);
            holder.textViewModel = convertView.findViewById(R.id.modelTV);
            holder.textViewGeneration = convertView.findViewById(R.id.generationTV);
            holder.textViewPrice = convertView.findViewById(R.id.priceTV);
            holder.textViewYear = convertView.findViewById(R.id.yearTV);
            holder.imageView = convertView.findViewById(R.id.imageView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Offer offer = offerList.get(position);

        holder.textViewBrand.setText(offer.getBrand());
        holder.textViewModel.setText(offer.getModel());
        holder.textViewGeneration.setText(offer.getGeneration());
        holder.textViewPrice.setText(offer.getPrice() + " ₽");
        holder.textViewYear.setText(offer.getYear());

        StorageReference storageReference = FirebaseStorage.getInstance().getReference("uploads/offer_images/" + offer.getOfferId() + "/");
        storageReference.listAll().addOnSuccessListener(listResult -> {
            if (!listResult.getItems().isEmpty()) {
                StorageReference firstImageRef = listResult.getItems().get(0);
                firstImageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    Picasso.get()
                            .load(uri)
                            .placeholder(R.drawable.no_image_svg1)
                            .error(R.drawable.no_image_svg1)
                            .into(holder.imageView);
                }).addOnFailureListener(e -> {
                    holder.imageView.setImageResource(R.drawable.no_image_svg1);
                });
            } else {
                holder.imageView.setImageResource(R.drawable.no_image_svg1);
            }
        }).addOnFailureListener(e -> {
            holder.imageView.setImageResource(R.drawable.no_image_svg1);
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Обработка нажатия на элемент
                Offer selectedOffer = offerList.get(position);
                Intent intent = new Intent(context, OfferActivity.class);
                intent.putExtra("offerId", selectedOffer.getOfferId());
                context.startActivity(intent);
            }
        });


        return convertView;
    }

    public void updateOffers(List<Offer> offers) {
        this.offerList.clear();
        this.offerList.addAll(offers);
        notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView textViewBrand;
        TextView textViewModel;
        TextView textViewGeneration;
        TextView textViewPrice;
        TextView textViewYear;
        ImageView imageView;
    }
}
