package com.app.weatherkotlin.presentation.screens.MainScreen

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.weatherkotlin.R
import com.app.weatherkotlin.data.repository.WeatherRepositoryImpl
import com.app.weatherkotlin.domain.model.Weather
import com.app.weatherkotlin.domain.use_case.GetWeatherUseCase
import com.app.weatherkotlin.presentation.screens.FavoritesScreen.FavoriteActivity
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.security.Permissions


@OptIn(DelicateCoroutinesApi::class)
@Composable
fun MainScreen(onLogoutClick: () -> Unit) {
    val coroutineScope = rememberCoroutineScope() // Remember a coroutine scope
    val context = LocalContext.current
    val getWeatherUseCase = GetWeatherUseCase(WeatherRepositoryImpl())
    var cityName by remember { mutableStateOf("Medellin") }
    val weatherState = remember { mutableStateOf<Weather?>(null) }

    val weatherColorMapper: (String) -> Color = { weather ->
        when (weather) {
            "Rain" -> Color(0xFF91A1FC)
            "Clear" -> Color(0xFFFCE191)
            "Clouds" -> Color(0xFF2E90BB)
            "Thunderstorm" -> Color(0xFF63519E)
            else -> Color(0xFF342564)
        }
    }

    ContainerMain(weatherColorMapper(weatherState.value?.weatherMain ?: ""), onLogoutClick) {
        Column {
            val weather = weatherState.value
            SearchInput(cityName = cityName, onSearch = { newCityName ->
                cityName = newCityName
                coroutineScope.launch(Dispatchers.IO) {
                    val updatedWeather = getWeatherUseCase(cityName)
                    weatherState.value = updatedWeather
                }
            })
            Result(weather)
        }
    }
}

fun goToFavorites(context: Context) {
    context.startActivity(Intent(context, FavoriteActivity::class.java))
    (context as Activity).finish()
}

@Composable
fun ContainerMain(
    color: Color = Color(0XFF3D81F1),
    onLogoutClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .background(color)
            .fillMaxSize()
            .padding(16.dp)
    ) {
        content()

        // floating action button
        FloatingActionButton(
            onClick = onLogoutClick,
            containerColor = Color.White.copy(alpha = 0.5f),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            shape = CircleShape
        ) {
            Icon(Icons.Default.ExitToApp, contentDescription = "Logout Icon")
        }
    }


}

@Composable
fun Result(weather: Weather?, modifier: Modifier = Modifier) {
    val isMarkedAsFavorite = remember { mutableStateOf(false) }
    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        if(weather != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Spacer(modifier = Modifier.size(16.dp))
                Row(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Icon(
                        Icons.Default.StarOutline,
                        contentDescription = "Search Icon",
                        tint = if (isMarkedAsFavorite.value) Color(0xFFFF7E7E) else Color.White,
                        modifier = Modifier
                            .size(48.dp)
                            .clickable(onClick = {
                                isMarkedAsFavorite.value = !isMarkedAsFavorite.value
                            })
                    )
                    Text(
                        text = weather.cityName,
                        style = TextStyle(
                            fontSize = 30.sp,
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold
                        ),
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Image(
                    painter = painterResource(
                        id = when (weather.weatherMain) {
                            "Rain" -> R.drawable.rain
                            "Clear" -> R.drawable.clear
                            "Clouds" -> R.drawable.cloud
                            "Thunderstorm" -> R.drawable.thunder
                            else -> R.drawable.wind
                        }
                    ),
                    contentDescription = "Descripción de tu imagen",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                )
                Text(
                    text = "${weather.temperatureCelsius}°C",
                    style = TextStyle(
                        fontSize = 90.sp,
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold
                    ),
                    modifier = Modifier.padding(8.dp)
                )
                Text(
                    text = "${weather.temperatureFahrenheit}°F",
                    style = TextStyle(
                        fontSize = 24.sp,
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold
                    ),
                    modifier = Modifier.padding(8.dp)
                )
                Text(
                    text = "Lat: ${weather.latitude}",
                    style = TextStyle(
                        fontSize = 18.sp,
                        color = Color.White.copy(alpha = 0.8f),
                        fontWeight = FontWeight.Normal
                    ),
                    modifier = Modifier.padding(8.dp)
                )
                Text(
                    text = "Lon: ${weather.longitude}",
                    style = TextStyle(
                        fontSize = 18.sp,
                        color = Color.White.copy(alpha = 0.8f),
                        fontWeight = FontWeight.Normal
                    ),
                    modifier = Modifier.padding(8.dp)
                )
                Text(
                    text = "Zona Horaria: ${weather.timeZone}",
                    style = TextStyle(
                        fontSize = 18.sp,
                        color = Color.White.copy(alpha = 0.8f),
                        fontWeight = FontWeight.Normal
                    ),
                    modifier = Modifier.padding(8.dp)
                )

                if (isMarkedAsFavorite.value) {
                    Button(
                        onClick = {
                            goToFavorites(context)
                        },
                        modifier = Modifier
                            .padding(8.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        )
                    ) {
                        Text(
                            text = "Ir a favoritos",
                            style = TextStyle(
                                fontSize = 16.sp,
                                color = Color.Black,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        } else {
            Text(text = "Aún no has buscado una ciudad", style = TextStyle(
                fontSize = 20.sp,
                color = Color.White
            ))
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchInput(
    cityName: String, // City name passed as a parameter
    onSearch: (String) -> Unit // Callback function for search action
) {
    var text by remember { mutableStateOf(TextFieldValue("")) } // Text state
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextField(
            modifier = Modifier
//                .fillMaxWidth()
                .weight(1f)
                .padding(8.dp),
            value = text,
            onValueChange = {
                text = it
            },
            textStyle = TextStyle(
                fontSize = 20.sp,
                color = Color.White
            ),
            label = { Text(text = "Buscar ciudad") }, // Label in Spanish
            placeholder = { Text(text = "Ejemplo Medellin") }, // Placeholder in Spanish
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Color.White,
                containerColor = Color.White.copy(alpha = 0.2f),
                unfocusedLabelColor = Color.White,
                focusedLabelColor = Color.White,
                unfocusedPlaceholderColor = Color.White,
            ),
            shape = RoundedCornerShape(10.dp),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = {
                onSearch(text.text)
            },
            modifier = Modifier
                .height(56.dp)
                .width(110.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White.copy(alpha = 0.4f),
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Buscar",
                style = TextStyle(
                    fontSize = 16.sp,
                    color = Color.White
                )
            )
        }
    }

}