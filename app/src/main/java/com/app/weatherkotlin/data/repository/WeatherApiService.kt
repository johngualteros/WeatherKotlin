package com.app.weatherkotlin.data.repository

import com.app.weatherkotlin.domain.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("weather")
    suspend fun getWeather(
        @Query("q") cityName: String,
        @Query("units") units: String = "metric",
        @Query("APPID") apiKey: String
    ): WeatherResponse
}