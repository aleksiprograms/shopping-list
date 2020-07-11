package com.aleksiprograms.shoppinglist.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aleksiprograms.shoppinglist.R;
import com.aleksiprograms.shoppinglist.tools.DateConverter;
import com.aleksiprograms.shoppinglist.tools.List;

import java.util.ArrayList;

public class ListsAdapter extends ArrayAdapter<List> {

    public ListsAdapter(@NonNull Context context, ArrayList<List> lists) {
        super(context, R.layout.list_row, lists);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View view = layoutInflater.inflate(R.layout.list_row, null);
        List list = getItem(position);
        TextView textView = (TextView) view.findViewById(R.id.textViewListName);
        String name = "";
        for (int i = 0; i < list.getItems().size(); i++) {
            if (i == list.getItems().size() - 1) {
                name = name + list.getItems().get(i).getName();
            } else {
                name = name + list.getItems().get(i).getName() + ", ";
            }
        }
        textView.setText(name);
        TextView tvRowTime = (TextView) view.findViewById(R.id.textViewListTime);
        tvRowTime.setText(DateConverter.millisToDate(list.getTime()));
        return view;
    }
}