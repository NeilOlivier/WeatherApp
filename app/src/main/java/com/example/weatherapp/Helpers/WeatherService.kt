package com.example.weatherapp.Helpers

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.weatherapp.Interfaces.IWeatherCallback
import com.example.weatherapp.Models.CurrentWeather
import com.example.weatherapp.Models.WeatherForcast
import com.google.gson.Gson

class WeatherService(var context: Context) {

    fun getCurrentWeather(data: IWeatherCallback): CurrentWeather?{
        val queue = Volley.newRequestQueue(context)
        val url: String = "https://samples.openweathermap.org/data/2.5/weather?lat=3225&lon=139&appid=b6907d289e10d714a6e88b30761fae22"
        var weatherObj: CurrentWeather? = null;

        // Request a string response from the provided URL.
        val stringReq = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                Log.d("volley",response.toString())
                var gson = Gson()
                weatherObj = gson.fromJson<CurrentWeather>(response.toString(), CurrentWeather::class.java)
                data.onDataReceived(weatherObj);
            },
            Response.ErrorListener {
                Log.d("volley","fail")

            })
        queue.add(stringReq)

        return weatherObj;
    }

    fun getForcastWeather(data: IWeatherCallback): WeatherForcast?{
        val queue = Volley.newRequestQueue(context)
        val url: String = "https://samples.openweathermap.org/data/2.5/forecast?lat=35&lon=139&appid=b6907d289e10d714a6e88b30761fae22"
        var weatherObj: WeatherForcast? = null;

        // Request a string response from the provided URL.
        val stringReq = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                Log.d("volleyForcast",response.toString())
                var gson = Gson()
                weatherObj = gson.fromJson<WeatherForcast>(response.toString(), WeatherForcast::class.java)
                data.onDataForcastReceived(weatherObj);
            },
            Response.ErrorListener {
                Log.d("volley",it.toString())

            })
        queue.add(stringReq)

        return weatherObj;
    }

}