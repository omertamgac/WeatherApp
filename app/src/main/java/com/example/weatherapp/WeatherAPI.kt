package com.example.weatherapp

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {

    

    @GET("weather")
    suspend fun getData(@Query("q") city: String, @Query("appid") apiKey: String): Response<WeatherModel>
}
