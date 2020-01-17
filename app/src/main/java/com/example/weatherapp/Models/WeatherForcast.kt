package com.example.weatherapp.Models

data class WeatherForcast(
    val message: String,
    val list: List<ListData>
)