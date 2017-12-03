package com.example.tom_d.restaurantv2;


import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends ListFragment {
    public ArrayList<String> list = new ArrayList<String>();
    public String theCategories;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_menu_fragment, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = this.getArguments();
        theCategories = arguments.getString("category");

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getContext());

        String url = "https://resto.mprog.nl/menu";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONArray menuArray;

                        try {
                            JSONObject newObject = (JSONObject) new JSONTokener(response).nextValue();

                            menuArray = newObject.getJSONArray("items");
                            for (int i = 0; i < menuArray.length(); i++) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                    if (Objects.equals(menuArray.getJSONObject(i).getString("category"), theCategories)) {
                                        addItemToArray(menuArray.getJSONObject(i).getString("name"));
                                        storePrice(menuArray.getJSONObject(i).getString("name"), menuArray.getJSONObject(i).getString("price"));

                                    }
                                }

                            }
                            SetAdapter();

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        ToDoDatabase db;
        db = ToDoDatabase.getInstance(getContext());


        SharedPreferences yourOrderPrefs = getContext().getSharedPreferences("PriceStore", getContext().MODE_PRIVATE);
        String price = yourOrderPrefs.getString(String.valueOf(l.getItemAtPosition(position)), null);

        db.insert(String.valueOf(l.getItemAtPosition(position)), price);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);

    }

    public void addItemToArray(String Item) {
        list.add(Item);
    }

    public void SetAdapter() {
        this.setListAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, list));

    }

    public void storePrice(String Item, String price) {

        SharedPreferences yourOrderPrefs = getContext().getSharedPreferences("PriceStore", getContext().MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = yourOrderPrefs.edit();
        prefsEditor.putString(Item, price);

        prefsEditor.commit();

    }

}