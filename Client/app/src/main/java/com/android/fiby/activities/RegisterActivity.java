package com.android.fiby.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.fiby.Colors;
import com.android.fiby.R;
import com.android.fiby.net.RegisterVolley;

public class RegisterActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText etFirstname = (EditText) findViewById(R.id.etFirstname);
        final EditText etLastname = (EditText) findViewById(R.id.etLastname);
        final EditText etEmail = (EditText) findViewById(R.id.etEmail);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                etPassword.setGravity(Gravity.LEFT);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        final Button bSignUp = (Button) findViewById(R.id.btSignUp);
        bSignUp.setTextColor(Colors.disactiveTextBtn);
        bSignUp.setBackgroundColor(Colors.disactiveBackgroundBtn);
        bSignUp.setOnClickListener(getDisactive());

        etFirstname.addTextChangedListener(getTextLisetner(etFirstname, etLastname, etEmail, etPassword, bSignUp));
        etLastname.addTextChangedListener(getTextLisetner(etFirstname, etLastname, etEmail, etPassword, bSignUp));
        etEmail.addTextChangedListener(getTextLisetner(etFirstname, etLastname, etEmail, etPassword, bSignUp));
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>0){
                    etPassword.setGravity(Gravity.LEFT);
                }
                else {
                    etPassword.setGravity(Gravity.RIGHT);
                }
                checkRegister(etFirstname, etLastname, etEmail, etPassword, bSignUp);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private TextWatcher getTextLisetner(final EditText firstName, final EditText lastName, final EditText email, final EditText password,
                                        final Button bSignUp){
        return new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkRegister(firstName, lastName, email, password, bSignUp);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
    }


    private void checkRegister(EditText firstName, EditText lastName, EditText email, EditText password,
                               Button bSignUp){
        if(firstName.getText().toString().length() > 1 && lastName.getText().toString().length() > 1
                && email.getText().toString().length() > 3 && email.getText().toString().contains("@")
                && password.getText().toString().length() > 5){
            bSignUp.setTextColor(Colors.activeTextBtn);
            bSignUp.setBackgroundColor(Colors.activeBackgroundBtn);
            bSignUp.setOnClickListener(getActive(firstName, lastName, email, password));
        }
        else {
            bSignUp.setTextColor(Colors.disactiveTextBtn);
            bSignUp.setBackgroundColor(Colors.disactiveBackgroundBtn);
            bSignUp.setOnClickListener(getDisactive());

        }
    }

    private View.OnClickListener getActive(final EditText firstName, final EditText lastName,
                                           final EditText email, final EditText password){
        return new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                RegisterVolley register_request = new RegisterVolley(RegisterActivity.this, password.getText().toString(),
                        firstName.getText().toString(), lastName.getText().toString(), email.getText().toString());
                register_request.makeRequest();
            }
        };
    }

    private View.OnClickListener getDisactive(){
        return new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Toast.makeText(RegisterActivity.this.getBaseContext(), "יש למלא את הפרטים כראוי:\n- לפחות שני תווים לשם פרטי ושם משפחה\n- דוא''ל תקין\n- לפחות 6 תווים לסיסמא", Toast.LENGTH_SHORT).show();
            }
        };
    }

}
