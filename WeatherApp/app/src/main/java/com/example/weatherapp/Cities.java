package com.example.weatherapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class Cities extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView LV_Cities;
    HashMap<String, Integer> Cities_Map;
    String[] Cities_List;
    DBHelper DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities);
        LV_Cities = findViewById(R.id.LV_Cities);
        DB = new DBHelper(this);
        Cursor cursor = DB.GetCities();
        Cities_Map = new HashMap<String, Integer>();
        if(cursor.getCount() > 0){
            while(cursor.moveToNext()){
                Cities_Map.put(cursor.getString(0), cursor.getInt(1));
            }
        }
        Cities_List = Cities_Map.keySet().toArray(new String[Cities_Map.keySet().size()]);
        ArrayAdapter<String> CitiesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,Cities_List);
        LV_Cities.setAdapter(CitiesAdapter);
        LV_Cities.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String City = parent.getItemAtPosition(position).toString();
        Intent intent = new Intent(Cities.this, Display_weather_of_a_City.class);
        intent.putExtra("CityName", City);
        startActivity(intent);
    }

    public void Add_City(View V){
        Intent intent = new Intent(Cities.this, Add_City.class);
        startActivity(intent);
    }
}