package com.example.autoquest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ParameterAdapter extends BaseAdapter {
    private Context context;
    private List<Parameter> parameters;

    public ParameterAdapter(Context context, List<Parameter> parameters) {
        this.context = context;
        this.parameters = parameters;
    }

    @Override
    public int getCount() {
        return parameters.size();
    }

    @Override
    public Object getItem(int position) {
        return parameters.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_parameter, parent, false);
        }

        Parameter parameter = parameters.get(position);

        TextView nameTextView = convertView.findViewById(R.id.parameterName);
        TextView valueTextView = convertView.findViewById(R.id.parameterValue);

        nameTextView.setText(parameter.getName());
        valueTextView.setText(parameter.getValue());

        return convertView;
    }
}


