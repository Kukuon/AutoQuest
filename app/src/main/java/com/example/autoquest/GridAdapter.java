package com.example.autoquest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

public class GridAdapter extends BaseAdapter {

    private Context mContext;
    private List<GridItem> mItems;

    public GridAdapter(Context context, List<GridItem> items) {
        mContext = context;
        mItems = items;
    }

    // Очистить список элементов
    public void clear() {
        mItems.clear();
        notifyDataSetChanged();
    }

    // Добавить элемент в список
    public void addItem(GridItem item) {
        mItems.add(item);
        notifyDataSetChanged();
    }

    public void setItems(List<GridItem> items) {
        mItems = items;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_item_layout, parent, false);
            holder.textViewBrand = (TextView) convertView.findViewById(R.id.brandTV); // Добавлено для бренда
            holder.textViewModel = (TextView) convertView.findViewById(R.id.modelTV); // Добавлено для модели
            holder.textViewGeneration = (TextView) convertView.findViewById(R.id.generationTV); // Добавлено для поколения
            holder.textViewPrice = (TextView) convertView.findViewById(R.id.priceTV); // Добавлено для цены
            holder.textViewYear = (TextView) convertView.findViewById(R.id.yearTV);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        GridItem item = mItems.get(position);
        holder.textViewBrand.setText(item.getBrand()); // Установка бренда
        holder.textViewModel.setText(item.getModel()); // Установка модели
        holder.textViewGeneration.setText(item.getGeneration()); // Установка поколения
        holder.textViewPrice.setText(item.getPrice()); // Установка цены
        holder.textViewYear.setText(item.getYear());

        return convertView;
    }

    static class ViewHolder {
        TextView textViewBrand; // Добавлено для бренда
        TextView textViewModel; // Добавлено для модели
        TextView textViewGeneration; // Добавлено для поколения
        TextView textViewPrice; // Добавлено для цены
        TextView textViewYear;
    }
}

