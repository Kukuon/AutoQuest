package com.example.autoquest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class CarAdapter extends BaseAdapter {
    private List<Car> mData;
    private Context mContext;

    public CarAdapter(Context context, List<Car> data) {
        this.mContext = context;
        this.mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.list_item_layout, parent, false);
            holder = new ViewHolder();
            holder.titleTextView = convertView.findViewById(R.id.brandEditText);
            // Найдите другие представления и установите обработчики событий здесь
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Car car = mData.get(position);

        holder.titleTextView.setText(car.getBrand());
        // Установите другие данные машины в соответствующие представления здесь

        return convertView;
    }

    static class ViewHolder {
        TextView titleTextView;
        // Добавьте другие представления здесь
    }
}

