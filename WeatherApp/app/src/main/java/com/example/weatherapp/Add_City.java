package com.example.weatherapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Add_City extends AppCompatActivity {

    DBHelper DB;
    EditText City;
    TextView Weather;
    String Base_URL = "https://api.openweathermap.org/data/2.5/";
    String API_Key = "5efadddfff15aaceb0093e19a081f22e";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);
        DB = new DBHelper(this);
        City = findViewById(R.id.City);
        Weather = findViewById(R.id.display);
    }
    public void Check_Weather(View V)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Base_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Weather_API myApi = retrofit.create(Weather_API.class);
        Call<APIData> ApiData = myApi.getweather(City.getText().toString().trim(),API_Key);
        ApiData.enqueue(new Callback<APIData>() {
            @Override
            public void onResponse(Call<APIData> call, Response<APIData> response) {
                if(response.code() == 404)
                {
                    Toast.makeText(Add_City.this,"Please enter a valid city name.",Toast.LENGTH_LONG).show();
                }
                else if(!(response.isSuccessful()))
                {
                    Toast.makeText(Add_City.this,response.code(),Toast.LENGTH_LONG).show();
                }
                else {
                    APIData myData = response.body();
                    Main main = myData.getMain();
                    Double temp = main.getTemp();
                    Integer temperature = (int) (temp - 273.15);
                    Weather.setText(String.valueOf(String.valueOf(temperature) + " C"));
                    String Name = City.getText().toString();
                    Cursor cursor = DB.GetCities();
                    if (cursor.getCount() > 0) {
                        DB.InsertCity(Name, 0);
                    } else {
                        DB.InsertCity(Name, 1);
                    }
                    Intent intent = new Intent(Add_City.this, Cities.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<APIData> call, Throwable t) {
                Toast.makeText(Add_City.this,t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

}