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
import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
import com.example.weatherapp.Helpers.WeatherService
import com.example.weatherapp.Interfaces.IWeatherCallback
import com.example.weatherapp.Models.CurrentWeather
import com.example.weatherapp.Models.WeatherForcast
import kotlinx.android.synthetic.main.activity_main.*


@SuppressLint("ByteOrderMark")
class MainActivity : AppCompatActivity(), IWeatherCallback{

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var longitude = 0.0
    private var latitude = 0.0
    private var weatherData: CurrentWeather? = null
    private var weatherDataForcast: WeatherForcast? = null
    val weatherActivity = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupLoadScreen()
        checkPermission()
    }

    fun setupLoadScreen(){
        sunnyView.visibility = View.GONE
        temperature.visibility = View.GONE
        weatherType.visibility = View.GONE
        linearLayout.visibility = View.GONE
        currentDay.visibility = View.GONE
        divider.visibility = View.GONE
        tuesdayLayout.visibility = View.GONE
        WednesdayLayout.visibility = View.GONE
        ThursdayLayout.visibility = View.GONE
        FridayLayout.visibility = View.GONE
        SaturdayLayout.visibility = View.GONE
    }

    fun SetupLoadedScreen(){
        sunnyView.visibility = View.VISIBLE
        temperature.visibility = View.VISIBLE
        weatherType.visibility = View.VISIBLE
        linearLayout.visibility = View.VISIBLE
        currentDay.visibility = View.VISIBLE
        divider.visibility = View.VISIBLE
        tuesdayLayout.visibility = View.VISIBLE
        WednesdayLayout.visibility = View.VISIBLE
        ThursdayLayout.visibility = View.VISIBLE
        FridayLayout.visibility = View.VISIBLE
        SaturdayLayout.visibility = View.VISIBLE
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
        weatherDataForcast = WeatherService(this).getForcastWeather(weatherActivity)
    }

    override fun onDataReceived(weatherData: CurrentWeather?){
        Log.d("callback", weatherData.toString())
        ProgressLoader.visibility = View.GONE
        setupWeatherSkins(weatherData!!.weather[0].description)
        //location on device check
        //different days fix
        sunnyView.visibility = View.VISIBLE
        SetupLoadedScreen()

        val degreeSymbol = "°"

        currentMinValue.text = weatherData!!.main.temp_min.take(2) + degreeSymbol
        currentValue.text = weatherData!!.main.temp.take(2) + degreeSymbol
        currentMaxValue.text = weatherData!!.main.temp_max.take(2) + degreeSymbol
    }

    override fun onDataForcastReceived(weatherData: WeatherForcast?) {
        Log.d("callbackForcast", weatherData.toString())
        tuesdayValue.text = weatherData!!.list[0].main.temp_max.take(2) + "°"
        wednesdayValue.text = weatherData!!.list[1].main.temp_max.take(2) + "°"
        thursdayValue.text = weatherData!!.list[2].main.temp_max.take(2) + "°"
        fridayValue.text = weatherData!!.list[3].main.temp_max.take(2) + "°"
        saturdayValue.text = weatherData!!.list[4].main.temp_max.take(2) + "°"
    }

    fun setupWeatherSkins(weather: String){
        if(weather == "clear sky") {
            sunnyView.setImageDrawable(
                ContextCompat.getDrawable(
                    applicationContext, // Context
                    R.drawable.sea_sunnypng // Drawable
                ))
            MainView.setBackgroundColor(ContextCompat.getColor(this, R.color.backgroundSunny))
        }
        else if(weather == "rainy"){
            sunnyView.setImageDrawable(
                ContextCompat.getDrawable(
                    applicationContext, // Context
                    R.drawable.sea_rainy // Drawable
                ))
            MainView.setBackgroundColor(ContextCompat.getColor(this, R.color.backgroundRainy))
        }
        else {
            sunnyView.setImageDrawable(
                ContextCompat.getDrawable(
                    applicationContext, // Context
                    R.drawable.sea_cloudy // Drawable
                ))
            MainView.setBackgroundColor(ContextCompat.getColor(this, R.color.backgroundCloudy))

         }
  }

}
