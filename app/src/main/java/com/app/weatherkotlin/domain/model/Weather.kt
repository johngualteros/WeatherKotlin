package com.app.weatherkotlin.domain.model

data class Weather(
    val cityName: String,
    val temperatureCelsius: Double,
    val temperatureFahrenheit: Double,
    val latitude: Double,
    val longitude: Double,
    val timeZone: String
)

interface WeatherRepository {
    suspend fun getWeather(cityName: String): Weather
}