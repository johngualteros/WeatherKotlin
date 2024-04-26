package com.app.weatherkotlin.data.repository

import com.app.weatherkotlin.domain.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("weather?q=alaska&units=metric&APPID=3185a5e3271f50859b92ecd87272c922")
    suspend fun getWeather(
        @Query("ciudad") cityName: String,
        @Query("clave_api") apiKey: String
    ): WeatherResponse
}