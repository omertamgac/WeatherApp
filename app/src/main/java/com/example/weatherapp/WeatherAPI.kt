package com.example.weatherapp

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface WeatherAPI {

    //https://api.openweathermap.org/data/2.5/
    @GET("weather?q=Istabul&appid=3ffd969e38680186fbfda2f1a729a18d")
    suspend fun getData(): Response<WeatherModel>
}