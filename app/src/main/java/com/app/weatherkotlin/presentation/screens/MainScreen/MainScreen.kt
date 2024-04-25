package com.app.weatherkotlin.presentation.screens.MainScreen

import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.weatherkotlin.R

@Composable
fun MainScreen() {
    ContainerMain(Color(0XFF342564)) {
        Column() {
            SearchInput()
            Result()
        }
    }
}


@Composable
fun ContainerMain(
    color: Color = Color(0XFF3D81F1),
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .background(color)
            .fillMaxSize()
            .padding(16.dp)
    ) {
        content()
    }
}

@Composable
fun Result(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.size(16.dp))
            Row(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "Medellin",
                    style = TextStyle(
                        fontSize = 30.sp,
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold
                    ),
                    modifier = Modifier.padding(8.dp)
                )
            }
            Image(
                painter = painterResource(id = R.drawable.rain),
                contentDescription = "Descripción de tu imagen",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            )
            Text(
                text = "37°C",
                style = TextStyle(
                    fontSize = 90.sp,
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold
                ),
                modifier = Modifier.padding(8.dp)
            )
            Text(
                text = "117°F",
                style = TextStyle(
                    fontSize = 24.sp,
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold
                ),
                modifier = Modifier.padding(8.dp)
            )
            Text(
                text = "Lat: 6.230833",
                style = TextStyle(
                    fontSize = 18.sp,
                    color = Color.White.copy(alpha = 0.8f),
                    fontWeight = FontWeight.Normal
                ),
                modifier = Modifier.padding(8.dp)
            )
            Text(
                text = "Lon: -75.590556",
                style = TextStyle(
                    fontSize = 18.sp,
                    color = Color.White.copy(alpha = 0.8f),
                    fontWeight = FontWeight.Normal
                ),
                modifier = Modifier.padding(8.dp)
            )
            Text(
                text = "Zona Horaria: America/Bogota",
                style = TextStyle(
                    fontSize = 18.sp,
                    color = Color.White.copy(alpha = 0.8f),
                    fontWeight = FontWeight.Normal
                ),
                modifier = Modifier.padding(8.dp)
            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchInput(modifier: Modifier = Modifier) {
    var text by remember { mutableStateOf(TextFieldValue("")) }
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        value = text,
        onValueChange = {
            text = it
        },
        textStyle = TextStyle(
            fontSize = 20.sp,
            color = Color.White
        ),
        label = { Text(text = "Buscar ciudad") },
        placeholder = { Text(text = "Ejemplo Medellin") },
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
}

@Preview
@Composable
fun MainScreenPreview() {
    MainScreen()
}