package com.example.weatherapp.Interfaces

import com.example.weatherapp.Models.Weather

interface IWeatherCallback{
    fun onDataReceived(weatherData: Weather?)
}