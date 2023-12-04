package com.example.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.roundToInt

//https://api.openweathermap.org/data/2.5/weather?q=(City)&appid=3ffd969e38680186fbfda2f1a729a18d
class MainActivity : AppCompatActivity() {

    private val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    private var weatherModels: ArrayList<WeatherModel>?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadData()
    }

    private fun loadData() {
        val retrofit=Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        //API ile retrofit'i bağlama
        val service = retrofit.create(WeatherAPI::class.java)
        val call=service.getData()

        call.enqueue(object: Callback<WeatherModel>{
            override fun onResponse(
                call: Call<WeatherModel>,
                response: Response<WeatherModel>
            ) {
                if (response.isSuccessful){
                    response.body()?.let {
                        val weatherModel = response.body()
                        weatherModel?.let {
                            println(it.name)
                          //  println(it.main.temp-273.15)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<WeatherModel>, t: Throwable) {
                t.printStackTrace()
                println("Başarısız")
            }

        })
    }


}