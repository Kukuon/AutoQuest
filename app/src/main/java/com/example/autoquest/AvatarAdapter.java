package com.example.autoquest;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import java.util.List;

public class AvatarAdapter extends BaseAdapter {

    private Context mContext;
    private List<Bitmap> mBitmaps;

    public AvatarAdapter(Context context, List<Bitmap> bitmaps) {
        mContext = context;
        mBitmaps = bitmaps;
    }

    @Override
    public int getCount() {
        return mBitmaps.size();
    }

    @Override
    public Object getItem(int position) {
        return mBitmaps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 250)); // Установите размеры изображения
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageBitmap(mBitmaps.get(position));
        return imageView;
    }
}
