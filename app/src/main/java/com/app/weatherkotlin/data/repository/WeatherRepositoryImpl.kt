package com.app.weatherkotlin.data.repository

import com.app.weatherkotlin.domain.model.Weather
import com.app.weatherkotlin.domain.model.WeatherRepository
import com.app.weatherkotlin.domain.util.Convertors
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherRepositoryImpl : WeatherRepository {
    private val apiKey = "3185a5e3271f50859b92ecd87272c922"
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/data/2.5/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val service = retrofit.create(WeatherApiService::class.java)
    override suspend fun getWeather(cityName: String): Weather {
        val response = service.getWeather(cityName, apiKey)
        return Weather(
            cityName = response.name,
            temperatureCelsius = response.main.temp,
            temperatureFahrenheit = Convertors.celsiusToFahrenheit(response.main.temp),
            latitude = response.coord.lat,
            longitude = response.coord.lon,
            timeZone = response.timezone
        )
    }
}