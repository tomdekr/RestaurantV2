package com.example.tom_d.restaurantv2;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tom_d.restaurantv2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends DialogFragment implements View.OnClickListener{
    private ToDoAdapter adapter;
    private ToDoDatabase db;
    public float totalPrice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_order_fragment, container, false);
        setRetainInstance(true);

        ListView list = (ListView) view.findViewById(R.id.list);
        db = ToDoDatabase.getInstance(getContext());

        Cursor cursor = db.selectAll();
        adapter = new ToDoAdapter(getContext(), cursor);
        list.setAdapter(adapter);

        try {
            while (cursor.moveToNext()) {
                String Price = cursor.getString(cursor.getColumnIndex("price"));
                Float PriceInt = Float.parseFloat(Price);

                String Amount = cursor.getString(cursor.getColumnIndex("amount"));
                Integer AmountInt = Integer.parseInt(Amount);

                Float totalItemPrice = PriceInt * AmountInt;
                totalPrice += totalItemPrice;
            }
        } finally {
        }
        TextView TotalPriceTextView = view.findViewById(R.id.totalPriceText);
        TotalPriceTextView.setText(Float.toString(totalPrice));

        Button clearOrder = view.findViewById(R.id.clear);
        clearOrder.setOnClickListener((View.OnClickListener) this);

        Button submitOrder = view.findViewById(R.id.submit);
        submitOrder.setOnClickListener((View.OnClickListener) this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case(R.id.clear):
                db = ToDoDatabase.getInstance(getContext());
                db.deleteAll();
                ListView list = (ListView) getView().findViewById(R.id.list);

                Cursor emptyCursor = db.selectAll();
                adapter.swapCursor(emptyCursor);

                list.setAdapter(adapter);

                TextView TotalPriceTextView = getView().findViewById(R.id.totalPriceText);
                TotalPriceTextView.setText(" ");
                break;

            case(R.id.submit):
                db = ToDoDatabase.getInstance(getContext());
                db.deleteAll();

                ListView list2 = (ListView) getView().findViewById(R.id.list);
                Cursor emptyCursor2 = db.selectAll();
                adapter.swapCursor(emptyCursor2);

                list2.setAdapter(adapter);

                TextView TotalPriceTextView2 = getView().findViewById(R.id.totalPriceText);
                TotalPriceTextView2.setText(" ");
                Submit(view);
                break;


        }
    }
        public void Submit(View view) {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url ="https://resto.mprog.nl/order";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            popUp(jsonObj.getString("preparation_time"));

                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);
    }

    public void popUp(String Item){
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle("Your order is being processed right now");
        alertDialog.setMessage("Your order's preparation time is: " + Item + " minutes");
        alertDialog.show();

    }

}