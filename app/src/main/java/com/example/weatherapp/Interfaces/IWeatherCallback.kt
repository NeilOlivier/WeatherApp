package com.example.weatherapp.Interfaces

import com.example.weatherapp.Models.CurrentWeather
import com.example.weatherapp.Models.WeatherForcast

interface IWeatherCallback{
    fun onDataReceived(weatherData: CurrentWeather?)
    fun onDataForcastReceived(weatherData: WeatherForcast?)
}