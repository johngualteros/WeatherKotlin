package com.app.weatherkotlin.presentation.screens.FavoritesScreen

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.weatherkotlin.MainActivity
import com.app.weatherkotlin.domain.model.Credentials
import com.app.weatherkotlin.domain.model.Favorite
import com.app.weatherkotlin.presentation.screens.LoginScreen.LoginActivity
import com.app.weatherkotlin.presentation.screens.LoginScreen.goToRegister
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.reflect.KFunction1


class FavoriteActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var fireStore: FirebaseFirestore

    // Public properties
    private lateinit var currentUser: FirebaseUser

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            this.currentUser = currentUser
        }
        if (currentUser == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        fireStore = FirebaseFirestore.getInstance()
        setContent{
            FavoriteScreen(
                deleteFavorite = ::deleteFavorite,
                getFavorites = ::getFavorites
            )
        }
    }

    fun deleteFavorite(city: String) {
        fireStore.collection("favorites")
            .whereEqualTo("city", city)
            .whereEqualTo("userId", currentUser.uid)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    fireStore.collection("favorites").document(document.id).delete()
                }
            }
            .addOnFailureListener { exception ->
                Log.w("deleteFavorite", "Error getting documents: ", exception)
            }
    }

    fun getFavorites(callback: (List<Favorite>) -> Unit) {
        fireStore.collection("favorites")
            .whereEqualTo("userId", currentUser.uid)
            .get()
            .addOnSuccessListener { documents ->
                val favorites = mutableListOf<Favorite>()
                for (document in documents) {
                    favorites.add(
                        Favorite(
                            id = document.id,
                            name = document.data["city"].toString(),
                            userId = document.data["userId"].toString()
                        )
                    )
                }
                callback(favorites)
            }
            .addOnFailureListener { exception ->
                Log.w("getFavorites", "Error getting documents: ", exception)
                callback(emptyList())
            }
    }
}

fun goBack(context: Context) {
    context.startActivity(Intent(context, MainActivity::class.java))
    (context as Activity).finish()
}

@Composable
fun FavoriteScreen(
    deleteFavorite: KFunction1<String, Unit>,
    getFavorites: KFunction1<(List<Favorite>) -> Unit, Unit>
) {
    Surface {
        var credentials by remember { mutableStateOf(Credentials()) }
        val context = LocalContext.current
        val myFavorites = remember { mutableStateOf<List<Favorite>>(emptyList()) }

        getFavorites() {
            myFavorites.value = it
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 40.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                "Volver",
                modifier = Modifier.clickable { goBack(context) },
                style = TextStyle(
                    fontSize = 17.sp,
                    color = Color(0xFF24BDFF),
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline
                ),
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                "Mis Favoritos",
                style = TextStyle(
                    fontSize = 30.sp,
                    color = Color(0xFF222222),
                    fontWeight = FontWeight.ExtraBold
                ),
            )
            if(myFavorites.value.isNotEmpty()) {
                myFavorites.value.forEach { it ->
                    ListViewElement(it.name) {
                        deleteFavorite(it.name)
                        getFavorites() {
                            myFavorites.value = it
                        }
                    }
                }
            } else {
                Text(
                    "No tienes favoritos",
                    style = TextStyle(
                        fontSize = 20.sp,
                        color = Color(0xFF222222),
                    )
                )
            }
        }
    }
}

@Composable
fun ListViewElement(text: String, onRemoveClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                style = TextStyle(
                    fontSize = 20.sp,
                    color = Color(0xFF222222),
                )
            )
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF7575)),
                onClick = { onRemoveClick() }) {
                Text(
                    text = "Quitar",
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = Color.White,
                    )
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color(0xFFD3D3D3))
        )

    }
}