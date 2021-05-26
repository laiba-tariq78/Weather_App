package com.example.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    EditText email, password;
    Button login;
    TextView register;
    boolean isEmailValid, isPasswordValid;
    DBHelper_Login db = new DBHelper_Login(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email =  findViewById(R.id.email);
        password =  findViewById(R.id.password);
        login = findViewById(R.id.login);
        register =  findViewById(R.id.register);


        login.setOnClickListener(v -> SetValidation());

        register.setOnClickListener(v -> {
            // redirect to RegisterActivity
            Intent registerIntent = new Intent(Login.this,Register.class);
            startActivity(registerIntent);
        });
    }

    public void SetValidation() {
        // Check for a valid email address.
        if (email.getText().toString().isEmpty()) {
            email.setError(getResources().getString(R.string.email_error));
            isEmailValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            email.setError(getResources().getString(R.string.error_invalid_email));
            isEmailValid = false;
        } else  {
            isEmailValid = true;

        }

        // Check for a valid password.
        if (password.getText().toString().isEmpty()) {
            password.setError(getResources().getString(R.string.password_error));
            isPasswordValid = false;
        }
        else if (password.getText().length() < 4) {
            password.setError(getResources().getString(R.string.error_invalid_password));
            isPasswordValid = false;
        }
        else  {
            isPasswordValid = true;
        }
        String eMail = email.getText().toString().trim();
        String pwd = password.getText().toString().trim();
        boolean res = db.Login(eMail, pwd);
        if (isEmailValid && isPasswordValid && res) {
            Toast.makeText(getApplicationContext(), "Login Successful.", Toast.LENGTH_SHORT).show();
            Intent registerIntent = new Intent(Login.this, MainActivity.class);
            startActivity(registerIntent);
        }
        else
        {
            Toast.makeText(Login.this,"Invalid Credentials",Toast.LENGTH_SHORT).show();
        }
    }
}