package com.example.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    String Name_of_City;
    TextView Weather;
    TextView City;
    DBHelper DB;
    String Base_URL = "https://api.openweathermap.org/data/2.5/";
    String API_Key = "5efadddfff15aaceb0093e19a081f22e";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DB = new DBHelper(this);
        Weather = findViewById(R.id.Main);
        City = findViewById(R.id.City_ka_Name);
        Name_of_City = "";
        Name_of_City = DB.GetDefaultCity();
        Display_Weather(Name_of_City);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_items,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.Locations:
                Intent intent = new Intent(MainActivity.this, Cities.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void Display_Weather(String Name)
    {
        if(!(Name.equals(""))) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Base_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Weather_API myApi = retrofit.create(Weather_API.class);
            Call<APIData> ApiData = myApi.getweather(Name, API_Key);
            ApiData.enqueue(new Callback<APIData>() {
                @Override
                public void onResponse(Call<APIData> call, Response<APIData> response) {
                    if (response.code() == 404) {
                        Toast.makeText(MainActivity.this, "Please enter a valid city name.", Toast.LENGTH_LONG).show();
                    } else if (!(response.isSuccessful())) {
                        Toast.makeText(MainActivity.this, response.code(), Toast.LENGTH_LONG).show();
                    } else {
                        City.setText(capitalize(Name));
                        APIData myData = response.body();
                        Main main = myData.getMain();
                        Double temp = main.getTemp();
                        Integer temperature = (int) (temp - 273.15);
                        Integer max = (int) (main.getTempMax() - 273.15);
                        Integer min = (int) (main.getTempMin() - 273.15);
                        Integer feelsLike = (int) (main.getFeelsLike() - 273.15);
                        Integer humidity = (int) (main.getHumidity());
                        Integer pressure = (int) (main.getPressure());
                        String Temperature = String.valueOf(temperature) + " C" + "\n\n" + String.valueOf(max) + "/" + String.valueOf(min) + "  (max/min)" + "\n\n";
                        Temperature += "Feels like " + String.valueOf(feelsLike) + " C" + "\n\n" + "Humidity " + humidity + "\n\n";
                        Temperature += "Pressure " + String.valueOf(pressure);
                        Weather.setText(Temperature);
                    }
                }

                @Override
                public void onFailure(Call<APIData> call, Throwable t) {
                    Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
        else {
            Weather.setText("No default city is available");
        }
    }
    private String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }
}