package com.example.weatherapp.Models

data class CurrentWeather(
    val coord: Coordinates,
    val weather: List<WeatherDetails>,
    val main: Main
)