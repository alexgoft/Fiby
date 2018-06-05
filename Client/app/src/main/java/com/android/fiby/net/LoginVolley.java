package com.android.fiby.net;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;
import android.view.View;

import com.android.fiby.activities.MainActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginVolley {

    private String username;
    private String password;
    private Context context;

//        final String HOST = "http://10.0.2.2:80/";
    final String HOST = "http://ec2-18-221-3-59.us-east-2.compute.amazonaws.com/";



    public LoginVolley(Context context, String username, String password) {
        this.context = context;
        this.username = username;
        this.password = password;
    }

    public void makeRequest(final View a) {
        String url = HOST + "get_user?type=login&email=" + username +"&password=" + password;

        
        RequestQueue queue = Volley.newRequestQueue(context);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray json_array = new JSONArray(response);

                            if (json_array.length() == 0) {
                                Toast.makeText(context, "פרטי התחברות שגויים...", Toast.LENGTH_SHORT).show();
                            } else {

                                SharedPreferences preferences = context.getSharedPreferences("com.android.fiby", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor;

                                editor = preferences.edit();
                                editor.putString("USER_FIRST_NAME", (String)((JSONObject)json_array.get(0)).get("first_name"));
                                editor.putString("USER_LAST_NAME", (String)((JSONObject)json_array.get(0)).get("last_name"));
                                editor.putInt("USER_ID", (int)((JSONObject)json_array.get(0)).get("user_id"));
                                editor.apply();

                                Intent signInIntent = new Intent(context, MainActivity.class);
                                signInIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                signInIntent.putExtra("USER_FIRST_NAME", (String)((JSONObject)json_array.get(0)).get("first_name"));
                                signInIntent.putExtra("USER_LAST_NAME", (String)((JSONObject)json_array.get(0)).get("last_name"));
                                signInIntent.putExtra("USER_ID", (int)((JSONObject)json_array.get(0)).get("user_id"));
                                context.startActivity(signInIntent);
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