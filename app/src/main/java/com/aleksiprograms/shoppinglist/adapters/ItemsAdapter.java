package com.aleksiprograms.shoppinglist.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aleksiprograms.shoppinglist.R;
import com.aleksiprograms.shoppinglist.tools.Item;

import java.util.ArrayList;

public class ItemsAdapter extends ArrayAdapter<Item> {

    public ItemsAdapter(@NonNull Context context, ArrayList<Item> items) {
        super(context, R.layout.item_row, items);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View view = layoutInflater.inflate(R.layout.item_row, null);
        Item item = getItem(position);
        TextView textView = (TextView) view.findViewById(R.id.textViewItemName);
        textView.setText(item.getName());
        if (item.isBought()) {
            textView.setPaintFlags(
                    textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            textView.setTextColor(
                    getContext().getResources().getColor(R.color.colorRed));
        } else {
            textView.setPaintFlags(
                    textView.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            textView.setTextColor(
                    getContext().getResources().getColor(R.color.colorWhite));
        }
        return view;
    }
}