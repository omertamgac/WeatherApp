package com.example.weatherapp

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.weatherapp.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.roundToInt

    class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    private var weatherModels: ArrayList<WeatherModel>?=null
    private var job : Job?= null
    private val API_KEY = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)
        loadData("Istanbul")
    }

    fun search(view : View){
        val search:String=binding.srcText.text.toString()
        loadData(search)
        binding.srcText.text.clear()
    }

    private fun loadData(city:String) {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherAPI::class.java)

        job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = retrofit.getData(city,API_KEY)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            val weatherModel = response.body()
                            weatherModel?.let {
                           //City name
                            binding.txtCity.text=it.name

                                //calculate time
                                val dg=it.weather[0].main
                                val currentTime = System.currentTimeMillis()/1000
                                val sunriseTimestamp=it.sys.sunrise as? Long
                                val sunsetTimestamp = it.sys.sunset as? Long

                                //Making necessary changes according to time and weather conditions.
                                if (currentTime in sunriseTimestamp!!..sunsetTimestamp!!) {
                                    binding.root.setBackgroundResource(R.drawable.gradient_background)
                                    when(dg){
                                        "Drizzle"-> binding.imgSymbol.setImageResource(R.drawable.dizzle)
                                        "Rain"-> binding.imgSymbol.setImageResource(R.drawable.rainn)
                                        "Snow"-> binding.imgSymbol.setImageResource(R.drawable.snoww)
                                        "Clear"-> binding.imgSymbol.setImageResource(R.drawable.clear)
                                        "Clouds"-> binding.imgSymbol.setImageResource(R.drawable.clouds)
                                        else-> binding.imgSymbol.setImageResource(R.drawable.fogg)
                                    }
                                } else {
                                    binding.root.setBackgroundResource(R.drawable.gradient_background_night)
                                    when(dg){
                                        "Drizzle"-> binding.imgSymbol.setImageResource(R.drawable.ndizzle)
                                        "Rain"-> binding.imgSymbol.setImageResource(R.drawable.nrain)
                                        "Snow"-> binding.imgSymbol.setImageResource(R.drawable.snoww)
                                        "Clear"-> binding.imgSymbol.setImageResource(R.drawable.nightclear)
                                        "Clouds"-> binding.imgSymbol.setImageResource(R.drawable.clouds)
                                        else-> binding.imgSymbol.setImageResource(R.drawable.fogg)
                                    }
                                }
                              //adjusting wind speed and humidity
                                val windSpeed = it.wind.speed
                                val humidity = it.main.humidity
                                binding.txtNem.text=windSpeed.toString()+"%"
                                binding.txtHumditiy.text=humidity.toString()+"km/h"
                                //weather temperature
                             val degree=it.main.temp-273.15
                            binding.txtDegree.text=degree.roundToInt().toString()+"°"
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                println(e)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }
}



