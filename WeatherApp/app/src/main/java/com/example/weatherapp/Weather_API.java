package com.example.weatherapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Weather_API {
    @GET("weather")
    Call<APIData> getweather(@Query("q") String CityName,
                             @Query("appid") String API_Key);
}
