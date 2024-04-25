//package com.app.weatherkotlin.presentation.screens.MainScreen
//
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.material3.Button
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.tooling.preview.Preview
//import com.app.weatherkotlin.data.repository.WeatherRepositoryImpl
//import com.app.weatherkotlin.domain.model.Weather
//import com.app.weatherkotlin.domain.use_case.GetWeatherUseCase
//import kotlinx.coroutines.DelicateCoroutinesApi
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.GlobalScope
//import kotlinx.coroutines.launch
//
//@OptIn(DelicateCoroutinesApi::class)
//@Composable
//fun WeatherScreen() {
////    val coroutineScope = rememberCoroutineScope()
////    val context = LocalContext.current
////    val getWeatherUseCase = GetWeatherUseCase(WeatherRepositoryImpl())
////
////    val cityName = "Medellin"
////
////    val weatherState = remember { mutableStateOf<Weather?>(null) }
//
//    Column(
//        modifier = Modifier.fillMaxSize(),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
////        val weather = weatherState.value
//        if (weather != null) {
//            Text(text = "City: ${weather.cityName}")
//            Text(text = "Temperature: ${weather.temperatureCelsius}°C")
//            Text(text = "Latitude: ${weather.latitude}")
//            Text(text = "Longitude: ${weather.longitude}")
//        } else {
//            Text(text = "Loading weather...")
//        }
//        Button(
//            onClick = {
//                GlobalScope.launch(Dispatchers.IO) {
//                    val updatedWeather = getWeatherUseCase(cityName = cityName)
//                    weatherState.value = updatedWeather
//                }
//            }
//        ) {
//            Text(text = "Actualizar Clima")
//        }
//    }
//}
//
//@Preview
//@Composable
//fun PreviewWeatherScreen() {
//    WeatherScreen()
//}