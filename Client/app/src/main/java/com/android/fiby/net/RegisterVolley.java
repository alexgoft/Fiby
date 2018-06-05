package com.android.fiby.net;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.android.fiby.activities.LoginActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;


public class RegisterVolley {
    private String password;
    private String first_name;
    private String last_name;
    private String email;

    private Context context;

//    final String HOST = "http://10.0.2.2:80/";
    final String HOST = "http://ec2-18-221-3-59.us-east-2.compute.amazonaws.com/";
    private boolean granted_access;

    public RegisterVolley(Context context, String password, String first_name,
                          String last_name, String email) {
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.context = context;

    }

    public void makeRequest() {
        String url = HOST + "set_user?type=user" +
                "&first_name=" + first_name +
                "&last_name=" + last_name +
                "&password=" + password +
                "&email=" + email;

        RequestQueue queue = Volley.newRequestQueue(context);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray json_array = new JSONArray(response);

                            if (json_array.length() == 0) {
                                Toast.makeText(context, "הדוא\"ל כבר רשום", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "רישום בוצע בהצלחה!", Toast.LENGTH_SHORT).show();
                                Intent signUpIntent = new Intent(context, LoginActivity.class);
                                context.startActivity(signUpIntent);
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
}

