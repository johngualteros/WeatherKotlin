package com.app.weatherkotlin.data.repository

import com.app.weatherkotlin.domain.model.Weather
import com.app.weatherkotlin.domain.model.WeatherRepository

class WeatherRepositoryImpl : WeatherRepository {
    override suspend fun getWeather(cityName: String): Weather {
        return Weather(
            cityName = "Medellin",
            temperatureCelsius = 37.0,
            temperatureFahrenheit = 98.6,
            latitude = 6.230833,
            longitude = -75.590556,
            timeZone = "America/Bogota"
        )
    }
}