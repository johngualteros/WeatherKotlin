package com.app.weatherkotlin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.app.weatherkotlin.domain.model.Favorite
import com.app.weatherkotlin.presentation.screens.LoginScreen.LoginActivity
import com.app.weatherkotlin.presentation.screens.MainScreen.MainScreen
import com.app.weatherkotlin.ui.theme.WeatherKotlinTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : ComponentActivity() {
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

    fun signOut() {
        auth.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    fun addToFavorite(city: String) {
        var isFavorite = false

        favoriteByCityToThisUserExists(city) { exists ->
            isFavorite = exists
        }

        if(!isFavorite) {
            val favorite = hashMapOf(
                "city" to city,
                "userId" to currentUser.uid
            )

            fireStore.collection("favorites")
                .add(favorite)
                .addOnSuccessListener { documentReference ->
                    println("DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    println("Error adding document: $e")
                }
        }
    }

    fun favoriteByCityToThisUserExists(city: String, callback: (Boolean) -> Unit) {
        fireStore.collection("favorites")
            .whereEqualTo("city", city)
            .whereEqualTo("userId", currentUser.uid)
            .get()
            .addOnSuccessListener { documents ->
                callback(documents.size() > 0)
            }
            .addOnFailureListener { exception ->
                Log.w("favoriteByCityToThisUserExists", "Error getting documents: ", exception)
                callback(false)
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        fireStore = FirebaseFirestore.getInstance()
        setContent {
            WeatherKotlinTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    MainScreen(
                        onLogoutClick = ::signOut,
                        onAddToFavorite = ::addToFavorite,
                        favoriteByCityToThisUserExists = ::favoriteByCityToThisUserExists,
                        deleteFavorite = ::deleteFavorite,
                        getFavorites = ::getFavorites
                    )
                }
            }
        }
    }
}
