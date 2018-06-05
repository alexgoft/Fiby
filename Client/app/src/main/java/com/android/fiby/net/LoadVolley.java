package com.android.fiby.net;

import android.content.Context;
import android.widget.Toast;

import com.android.fiby.activities.MainActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;


public class LoadVolley {
    private String user_id;
    private Context context;
    private MainActivity mainActivity;

//        final String HOST = "http://10.0.2.2:80/";
    final String HOST = "http://ec2-18-221-3-59.us-east-2.compute.amazonaws.com/";

    public LoadVolley(int user_id, Context context, MainActivity mainActivity) {

        this.user_id = Integer.toString(user_id);
        this.context = context;
        this.mainActivity = mainActivity;
    }

    public void getItems() {
        String url = HOST + "items";

        RequestQueue queue = Volley.newRequestQueue(context);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray json_array = new JSONArray(response);

                            if (json_array.length() == 0) {
                                Toast.makeText(context, "שגיאה: לא נמצאו פרטים לרכישה...", Toast.LENGTH_SHORT).show();
                            } else {
                                mainActivity.setFoodList(json_array);
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

    public void getSemiItems() {
        String url = HOST + "semi_items";

        RequestQueue queue = Volley.newRequestQueue(context);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray json_array = new JSONArray(response);

                            if (json_array.length() == 0) {
                                Toast.makeText(context, "שגיאה: לא נמצאו פרטים לרכישה...", Toast.LENGTH_SHORT).show();
                            } else {
                                mainActivity.addSemiItems(json_array);
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

    public void getHistory() {
        String url = HOST + "history?user_id=" + user_id;

        RequestQueue queue = Volley.newRequestQueue(context);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray json_array = new JSONArray(response);
                            mainActivity.setHistory(json_array);
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


    public void getFavourites() {
        String url = HOST + "favourites?user_id=" + user_id;

        RequestQueue queue = Volley.newRequestQueue(context);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray json_array = new JSONArray(response);
                            mainActivity.setFavourites(json_array);
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


}
