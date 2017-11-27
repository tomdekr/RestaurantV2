package com.example.tom_d.restaurantv2;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

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
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends ListFragment {
    public ArrayList<String> menuList = new ArrayList<String>();
    public String selectedCategory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_menu_fragment, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = this.getArguments();
        selectedCategory = arguments.getString("category");

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getContext());

        String url = "https://resto.mprog.nl/menu";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //Log.d("my_test", response);
                        JSONArray menuArray;

                        try {
                            JSONObject newObject = (JSONObject) new JSONTokener(response).nextValue();
                            //ArrayList<String> listItems = new ArrayList<String>();

                            menuArray = newObject.getJSONArray("items");
                            for (int i = 0; i < menuArray.length(); i++) {
                                //mTextView.setText(menuArray.getJSONObject(i).getString("name"));
                                //listItems.add(menuArray.getJSONObject(i).getString("name"));
                                if(Objects.equals(menuArray.getJSONObject(i).getString("category"), selectedCategory)){
                                    addItemToArray(menuArray.getJSONObject(i).getString("name"));}

                                //addItemToArrayPrice(menuArray.getJSONObject(i).getString("price"));
                            }
                            SetAdapter();

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                        //mTextView.setText(listItems.toString());

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        //mTextView.setText(listItems.toString());
        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,  R.layout.activity_main, R.id.text, animalList);
        //simpleList.setAdapter(arrayAdapter);

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);

    }

    public void addItemToArray(String Item) {
        menuList.add(Item);
    }

    public void SetAdapter() {
        Log.d("array", menuList.toString());
        this.setListAdapter(new ArrayAdapter<String>(getContext(),  android.R.layout.simple_list_item_1, menuList));

    }
}