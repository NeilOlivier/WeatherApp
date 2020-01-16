package com.example.weatherapp

import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.annotation.SuppressLint
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.view.View
import androidx.core.content.ContextCompat
import com.example.weatherapp.Helpers.WeatherService
import com.example.weatherapp.Interfaces.IWeatherCallback
import com.example.weatherapp.Models.Weather
import kotlinx.android.synthetic.main.activity_main.*


@SuppressLint("ByteOrderMark")
class MainActivity : AppCompatActivity(), IWeatherCallback {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var longitude = 0.0
    private var latitude = 0.0
    private var weatherData: Weather? = null
    val weatherActivity = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupLoadScreen()
        checkPermission()
    }

    fun setupLoadScreen(){
        sunnyView.visibility = View.GONE
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            123 -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    getLocation()
                } else {

                }
                return
            }
            else -> {

            }
        }
    }

    fun checkPermission(){

        if (ContextCompat.checkSelfPermission(
                this,
                ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {//Can add more as per requirement

            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    ACCESS_COARSE_LOCATION
                ),
                123
            )
        }
        else {
            getLocation()
        }
    }

    fun getLocation(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                if (location != null) {
                    latitude = location.latitude
                    longitude = location.longitude

                    getWeatherData(latitude, longitude)
                }
            }.addOnFailureListener {
                Log.d("test", it.toString())
            }
    }

    private fun getWeatherData(latitude : Double, long: Double ) {
        weatherData = WeatherService(this).getCurrentWeather(weatherActivity)
    }

    override fun onDataReceived(weatherData: Weather?){
        Log.d("callback", weatherData.toString())
        ProgressLoader.visibility = View.GONE
        sunnyView.visibility = View.VISIBLE

        val degreeSymbol = "°"

        currentMinValue.text = weatherData!!.main.temp_min.take(2) + degreeSymbol
        currentValue.text = weatherData!!.main.temp.take(2) + degreeSymbol
        currentMaxValue.text = weatherData!!.main.temp_max.take(2) + degreeSymbol
    }

}
