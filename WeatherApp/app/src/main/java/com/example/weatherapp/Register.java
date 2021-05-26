package com.example.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Register extends AppCompatActivity {

    EditText name, email,password;
    Button register;
    boolean isNameValid, isEmailValid, isPasswordValid;
    DBHelper_Login db = new DBHelper_Login(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password =  findViewById(R.id.password);
        register =  findViewById(R.id.register);

        register.setOnClickListener(v -> SetValidation());
    }

    public void SetValidation() {
        // Check for a valid name.
        if (name.getText().toString().isEmpty()) {
            name.setError(getResources().getString(R.string.name_error));
            isNameValid = false;
        } else  {
            isNameValid = true;

        }

        // Check for a valid email address.
        if (email.getText().toString().isEmpty()) {
            email.setError(getResources().getString(R.string.email_error));
            isEmailValid = false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            email.setError(getResources().getString(R.string.error_invalid_email));
            isEmailValid = false;
        }
        else  {
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
        String naMe = name.getText().toString().trim();
        long res = db.register(naMe, pwd, eMail);
        if (isNameValid && isEmailValid && isPasswordValid && res>0) {
            Toast.makeText(getApplicationContext(), "Successfully Registered", Toast.LENGTH_SHORT).show();
            Intent registerIntent = new Intent(this, MainActivity.class);
            startActivity(registerIntent);
        }
        else
        {
            Toast.makeText(this,"Register Error",Toast.LENGTH_SHORT).show();
        }
    }

}