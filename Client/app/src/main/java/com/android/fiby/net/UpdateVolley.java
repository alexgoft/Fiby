package com.android.fiby.net;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.android.fiby.activities.MainActivity;
import com.android.fiby.structures.ItemToPurchase;
import com.android.fiby.structures.Order;
import com.android.fiby.structures.SemiItem;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Calendar;


public class UpdateVolley {

//    final static String HOST = "http://10.0.2.2:80/";
    final static String HOST = "http://ec2-18-221-3-59.us-east-2.compute.amazonaws.com/";

    public static void markAsLiked(final ItemToPurchase item, final Context context) {
        String url = HOST + "update_favourites?user_id="+MainActivity.userId+"&type=add&item_id="+item.getFoodItem().getId()+"&semi_items=";
        if(item.getSemiItems().size()>0){
            for(SemiItem semiItem : item.getSemiItems()){
                url += semiItem.getId() + "_";
            }
        }
        else{
            url += "-1";
        }

        RequestQueue queue = Volley.newRequestQueue(context);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray json_array = new JSONArray(response);

                            if (json_array.length() == 0) {
                                Toast.makeText(context, "שגיאה: הפריט לא הוסף למועדפים", Toast.LENGTH_SHORT).show();
                            } else {
                                item.setFavId(json_array.getInt(0));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                    }
                });

        queue.add(stringRequest);
    }


    public static void markAsUnliked(final ItemToPurchase item, final Context context) {
        String url = HOST + "update_favourites?type=remove&fav_id=" + item.getFavId();

        RequestQueue queue = Volley.newRequestQueue(context);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray json_array = new JSONArray(response);

                            if (json_array.length() == 0) {
                                Toast.makeText(context, "שגיאה: הפריט לא הוסר מהמועדפים", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

        queue.add(stringRequest);
    }


    public static void buy(final Order order, final Context context) {
        String url = HOST + "new_order?user_id=" + Integer.toString(MainActivity.userId)
                + "&date=" +  order.getDate() +
                "&items=";

        for (ItemToPurchase item : order.getShopList()){
            url += Integer.toString(item.getFoodItem().getId()) + "s" +
                    Double.toString(item.getFoodItem().getPrice());
            for(SemiItem semi : item.getSemiItems()){
                url += "s" + Integer.toString(semi.getId());
            }
            url += "$";
        }

        RequestQueue queue = Volley.newRequestQueue(context);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray json_array = new JSONArray(response);

                            if (json_array.length() == 0) {
                                Toast.makeText(context, "שגיאה: הפריט לא הוסר מהמועדפים", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

        queue.add(stringRequest);
    }

}
