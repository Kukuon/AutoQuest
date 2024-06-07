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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_grid, parent, false);

            holder.textViewTitle = convertView.findViewById(R.id.titleTV);
            holder.textViewPrice = convertView.findViewById(R.id.priceTV);
            holder.textViewYear = convertView.findViewById(R.id.yearTV);
            holder.imageView = convertView.findViewById(R.id.imageView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Offer offer = offerList.get(position);

        // установка основных параметров
        holder.textViewTitle.setText(offer.getBrand() + " " + offer.getModel() + " " + offer.getGeneration());
        holder.textViewPrice.setText(offer.getPrice() + " ₽");
        holder.textViewYear.setText(offer.getYear());

        // установка изображения
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("uploads/offer_images/" + offer.getOfferId() + "/");
        storageReference.listAll().addOnSuccessListener(listResult -> {
            if (!listResult.getItems().isEmpty()) {
                StorageReference firstImageRef = listResult.getItems().get(0);
                firstImageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    Picasso.get()
                            .load(uri)
                            .placeholder(R.drawable.image_svg)
                            .error(R.drawable.image_svg)
                            .into(holder.imageView);
                    holder.imageView.setImageResource(R.drawable.no_svg);
                    holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }).addOnFailureListener(e -> {
                    holder.imageView.setImageResource(R.drawable.image_svg);
                });
            } else {
                holder.imageView.setImageResource(R.drawable.image_svg);
            }
        }).addOnFailureListener(e -> {
            holder.imageView.setImageResource(R.drawable.image_svg);
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
        TextView textViewTitle;
        TextView textViewPrice;
        TextView textViewYear;
        ImageView imageView;
    }
}
