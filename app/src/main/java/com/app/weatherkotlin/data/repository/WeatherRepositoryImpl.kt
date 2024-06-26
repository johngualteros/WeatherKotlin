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
        try {
            val response = service.getWeather(cityName, "metric" , apiKey)
            return Weather(
                cityName = response.name,
                temperatureCelsius = response.main.temp,
                temperatureFahrenheit = Convertors.celsiusToFahrenheit(response.main.temp),
                latitude = response.coord.lat,
                longitude = response.coord.lon,
                timeZone = response.timezone,
                weatherMain = response.weather[0].main
            )
        } catch (e: Exception) {
            return Weather(
                cityName = "Ciudad no encontrada",
                temperatureCelsius = 0.0,
                temperatureFahrenheit = 0.0,
                latitude = 0.0,
                longitude = 0.0,
                timeZone = 0,
                weatherMain = "default"
            )
        }
    }
}