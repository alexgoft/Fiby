package com.android.fiby.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.fiby.Colors;
import com.android.fiby.R;
import com.android.fiby.net.LoginVolley;
import com.android.fiby.net.RegisterVolley;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

//    final String HOST = "http://10.0.2.2:80/";
    final String HOST = "http://ec2-18-221-3-59.us-east-2.compute.amazonaws.com/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText email = (EditText) findViewById(R.id.etLoginEmail);
        final EditText password = (EditText) findViewById(R.id.etLoginPassword);
        final Button bSignIn  = (Button) findViewById(R.id.btLogIn);
        final TextView bSignUp  = (TextView) findViewById(R.id.btLogInSignUp);

        // Set Listeners
        email.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkRegister(email, password, bSignIn);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>0){
                    password.setGravity(Gravity.LEFT);
                }
                else {
                    password.setGravity(Gravity.RIGHT);
                }
                checkRegister(email, password, bSignIn);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        bSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUpIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(signUpIntent);
            }
        });


        // Design the page
        bSignIn.setTextColor(Colors.disactiveTextBtn);
        bSignIn.setBackgroundColor(Colors.disactiveBackgroundBtn);
        bSignIn.setOnClickListener(getDisactive());
        password.setGravity(Gravity.RIGHT);


        // Keep the user login
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("com.android.fiby", Context.MODE_PRIVATE);

        int id = preferences.getInt("USER_ID", -1);
        String first_name = preferences.getString("USER_FIRST_NAME", null);
        String last_name = preferences.getString("USER_LAST_NAME", null);

        if (id != -1){
            Intent signInIntent = new Intent(this, MainActivity.class);
            signInIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            signInIntent.putExtra("USER_FIRST_NAME", first_name);
            signInIntent.putExtra("USER_LAST_NAME", last_name);
            signInIntent.putExtra("USER_ID", id);

            this.startActivity(signInIntent);
        }
    }


    private void checkRegister(EditText email, EditText password, Button logIn){
        if(email.getText().toString().length() > 3 && email.getText().toString().contains("@")
                && password.getText().toString().length() > 5){
            logIn.setTextColor(Colors.activeTextBtn);
            logIn.setBackgroundColor(Colors.activeBackgroundBtn);
            logIn.setOnClickListener(getActive(email, password));
        }
        else {
            logIn.setTextColor(Colors.disactiveTextBtn);
            logIn.setBackgroundColor(Colors.disactiveBackgroundBtn);
            logIn.setOnClickListener(getDisactive());
        }
    }

    private View.OnClickListener getActive(final EditText email, final EditText password){
        return new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                LoginVolley login_request = new LoginVolley(LoginActivity.this,
                        email.getText().toString(),
                        password.getText().toString());
                login_request.makeRequest(view);
            }
        };
    }

    private View.OnClickListener getDisactive(){
        return new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this.getBaseContext(), "יש למלא את הפרטים כראוי:\n- דוא''ל תקין\n- לפחות 6 תווים לסיסמא", Toast.LENGTH_SHORT).show();
            }
        };
    }
}
