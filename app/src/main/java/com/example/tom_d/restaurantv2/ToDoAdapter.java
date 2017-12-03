package com.example.tom_d.restaurantv2;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by Tom_D on 11/30/2017.
 */

public class ToDoAdapter extends CursorAdapter {


    public ToDoAdapter(Context context, Cursor cursor) {
        super(context, cursor, R.layout.content_my_adapter);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.content_my_adapter, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView title = view.findViewById(R.id.title);
        TextView price = view.findViewById(R.id.price);
        TextView amount = view.findViewById(R.id.amount);
        String Price = cursor.getString(cursor.getColumnIndex("price"));
        Float PriceInt = Float.parseFloat(Price);
        String Amount = cursor.getString(cursor.getColumnIndex("amount"));
        Integer AmountInt = Integer.parseInt(Amount);
        Float totalItemPrice = PriceInt * AmountInt;

        title.setText(cursor.getString(cursor.getColumnIndex("name")));
        price.setText("€" + totalItemPrice.toString() + ",-");
        amount.setText(cursor.getString(cursor.getColumnIndex("amount")));
    }
}