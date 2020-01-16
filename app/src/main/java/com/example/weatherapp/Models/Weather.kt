package com.example.weatherapp.Models

data class Weather(
    val coord: Coordinates,
    val main: Main,
    val wind: Wind,
    val clouds: Clouds
    )