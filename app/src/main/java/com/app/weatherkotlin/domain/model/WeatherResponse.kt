package com.app.weatherkotlin.domain.model

import com.google.gson.annotations.SerializedName

data class WeatherResponse (
    @SerializedName("coord") val coord: Coord,
    @SerializedName("weather") val weather: List<WeatherR>,
    @SerializedName("base") val base: String,
    @SerializedName("main") val main: Main,
    @SerializedName("visibility") val visibility: Long,
    @SerializedName("wind") val wind: Wind,
    @SerializedName("clouds") val clouds: Clouds,
    @SerializedName("dt") val dt: Long,
    @SerializedName("sys") val sys: Sys,
    @SerializedName("timezone") val timezone: Long,
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("cod") val cod: Long
)

data class Clouds (
    @SerializedName("all") val all: Long
)

data class Coord (
    @SerializedName("lon") val lon: Double,
    @SerializedName("lat") val lat: Double
)

data class Main (
    @SerializedName("temp") val temp: Double,
    @SerializedName("feels_like") val feelsLike: Double,
    @SerializedName("temp_min") val tempMin: Double,
    @SerializedName("temp_max") val tempMax: Double,
    @SerializedName("pressure") val pressure: Long,
    @SerializedName("humidity") val humidity: Long,
    @SerializedName("sea_level") val seaLevel: Long,
    @SerializedName("grnd_level") val grndLevel: Long
)

data class Sys (
    @SerializedName("country") val country: String,
    @SerializedName("sunrise") val sunrise: Long,
    @SerializedName("sunset") val sunset: Long
)

data class WeatherR (
    @SerializedName("id") val id: Long,
    @SerializedName("main") val main: String,
    @SerializedName("description") val description: String,
    @SerializedName("icon") val icon: String
)

data class Wind (
    @SerializedName("speed") val speed: Double,
    @SerializedName("deg") val deg: Long,
    @SerializedName("gust") val gust: Double
)