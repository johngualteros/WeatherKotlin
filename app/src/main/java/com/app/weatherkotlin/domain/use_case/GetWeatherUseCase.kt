package com.app.weatherkotlin.domain.use_case

import com.app.weatherkotlin.domain.model.Weather
import com.app.weatherkotlin.domain.model.WeatherRepository

class GetWeatherUseCase(private val weatherRepository: WeatherRepository) {
    suspend operator fun invoke(cityName: String): Weather {
        return weatherRepository.getWeather(cityName)
    }
}