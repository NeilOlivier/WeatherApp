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
import android.location.LocationManager
import android.view.View
import android.widget.Toast
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
    private val weatherActivity = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Hidding the items till we have data back from the API
        setupLoadScreen()

        // Checking if location is turned on so the we can use lon and lat to call API
        checkPermission()
    }

    fun setupLoadScreen(){
        sunnyView.visibility = View.GONE
        temperature.visibility = View.GONE
        weatherType.visibility = View.GONE
        daysLinearLayout.visibility = View.GONE
        iconLinearLayout.visibility = View.GONE
        tempLinearLayout.visibility = View.GONE
        divider.visibility = View.GONE
    }

    fun SetupLoadedScreen(){
        sunnyView.visibility = View.VISIBLE
        temperature.visibility = View.VISIBLE
        weatherType.visibility = View.VISIBLE
        divider.visibility = View.VISIBLE
        daysLinearLayout.visibility = View.VISIBLE
        iconLinearLayout.visibility = View.VISIBLE
        tempLinearLayout.visibility = View.VISIBLE
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            123 -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    getLocation()
                }
                else{
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(
                            ACCESS_COARSE_LOCATION
                        ),
                        123
                    )
                }
                return
            }
        }
    }

    fun checkPermission(){
        if (ActivityCompat.checkSelfPermission(
                this,
                ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
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
        if (isLocationEnabled()) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        latitude = location.latitude
                        longitude = location.longitude

                        getWeatherData(latitude, longitude)
                    }
                }.addOnFailureListener {
                    Log.d("test", it.toString())
                }
        }
        else
        {
            val duration = Toast.LENGTH_LONG

            val toast = Toast.makeText(applicationContext, "Please turn on location", duration)
            toast.show()
        }
    }

    private fun getWeatherData(latitude : Double, long: Double ) {
        weatherData = WeatherService(this).getCurrentWeather(weatherActivity)
        weatherDataForcast = WeatherService(this).getForcastWeather(weatherActivity)
    }

    // Current API endpoint callback
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

    // Forcast API endpoint callback
    override fun onDataForcastReceived(weatherData: WeatherForcast?) {
        Log.d("callbackForcast", weatherData.toString())
        tuesdayValue.text = weatherData!!.list[0].main.temp_max.take(2) + "°"
        wednesdayValue.text = weatherData!!.list[1].main.temp_max.take(2) + "°"
        thursdayValue.text = weatherData!!.list[2].main.temp_max.take(2) + "°"
        fridayValue.text = weatherData!!.list[3].main.temp_max.take(2) + "°"
        saturdayValue.text = weatherData!!.list[4].main.temp_max.take(2) + "°"
    }

    // This is to change the skin based on the weather
    fun setupWeatherSkins(weather: String){
        if(weather == "clear sky") {
            sunnyView.setImageDrawable(
                ContextCompat.getDrawable(
                    applicationContext, // Context
                    R.drawable.sea_sunnypng // Drawable
                ))

            setIconImage(R.drawable.clear)

            MainView.setBackgroundColor(ContextCompat.getColor(this, R.color.backgroundSunny))
        }
        else if(weather == "rainy"){
            sunnyView.setImageDrawable(
                ContextCompat.getDrawable(
                    applicationContext, // Context
                    R.drawable.sea_rainy // Drawable
                ))

            setIconImage(R.drawable.rain)

            MainView.setBackgroundColor(ContextCompat.getColor(this, R.color.backgroundRainy))
        }
        else {
            sunnyView.setImageDrawable(
                ContextCompat.getDrawable(
                    applicationContext, // Context
                    R.drawable.sea_cloudy // Drawable
                ))

            setIconImage(R.drawable.partlysunny)

            MainView.setBackgroundColor(ContextCompat.getColor(this, R.color.backgroundCloudy))

         }
    }

    fun setIconImage(resId: Int){
        tuesdayImage.setImageDrawable(ContextCompat.getDrawable(applicationContext, resId))
        wednesdayImage.setImageDrawable(ContextCompat.getDrawable(applicationContext, resId))
        thursdayImage.setImageDrawable(ContextCompat.getDrawable(applicationContext, resId))
        fridayImage.setImageDrawable(ContextCompat.getDrawable(applicationContext, resId))
        saturdayImage.setImageDrawable(ContextCompat.getDrawable(applicationContext, resId))

    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }
}
