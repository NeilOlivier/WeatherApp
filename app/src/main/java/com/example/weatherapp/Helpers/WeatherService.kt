package com.example.weatherapp.Helpers

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class WeatherService(var context: Context) {

    fun getCurrentWeather(): String{

        val queue = Volley.newRequestQueue(context)
        val url: String = "https://samples.openweathermap.org/data/2.5/weather?lat=3225&lon=139&appid=b6907d289e10d714a6e88b30761fae22"
        var result = ""

        // Request a string response from the provided URL.
        val stringReq = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                Log.d("volley",response.toString())
            },
            Response.ErrorListener {
                Log.d("volley","fail")

            })
        queue.add(stringReq)

        return result;
    }

}