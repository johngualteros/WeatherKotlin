package com.app.weatherkotlin.presentation.screens.RegisterScreen

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import com.app.weatherkotlin.presentation.screens.LoginScreen.LoginActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


class RegisterActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        setContent{
            RegisterForm(signUp = ::signUp)
        }
    }

    fun signUp(creds: Credentials, context: Context) {
        Log.d("Credentials", creds.toString())
        Log.d("Credentials", creds.email)
        Log.d("Credentials", creds.pwd)
        auth.createUserWithEmailAndPassword(creds.email, creds.pwd)
            .addOnCompleteListener(this) { task ->
                if(task.isSuccessful) {
                    Log.d("Credentials", "createUserWithEmail:success")
                    val intent = Intent(context, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Log.w("Credentials", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Error en la autenticación", Toast.LENGTH_SHORT).show()
                }
            }
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}


fun goToLogin(context: Context) {
    context.startActivity(Intent(context, LoginActivity::class.java))
    (context as Activity).finish()
}

@Composable
fun RegisterForm(
    signUp: (Credentials, Context) -> Unit
) {
    Surface {
        var credentials by remember { mutableStateOf(Credentials()) }
        val context = LocalContext.current

        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Registrarme",
                    style = TextStyle(
                        fontSize = 30.sp,
                        color = Color(0xFF222222),
                        fontWeight = FontWeight.ExtraBold
                    ),
                    modifier = Modifier.padding(8.dp)
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
            RegisterField(
                value = credentials.email,
                onChange = { data -> credentials = credentials.copy(email = data) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(30.dp))
            PasswordField(
                value = credentials.pwd,
                onChange = { data -> credentials = credentials.copy(pwd = data) },
                submit = {
                    signUp(credentials, context)
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(40.dp))
            Button(
                onClick = {
                          signUp(credentials, context)
                },
                enabled = credentials.isNotEmpty(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8157DA)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    "Registrarme",
                    style = TextStyle(
                        fontSize = 18.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    ),
                )
            }
            Spacer(modifier = Modifier.height(90.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("Ya tienes una cuenta?")
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    "Iniciar sesión",
                    modifier = Modifier.clickable { goToLogin(context) },
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = Color(0xFF24BDFF),
                        textDecoration = TextDecoration.Underline
                    ),
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterField(
    value: String,
    onChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Email",
    placeholder: String = "Digite su email"
) {

    val focusManager = LocalFocusManager.current

    TextField(
        value = value,
        onValueChange = onChange,
        modifier = modifier,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) }
        ),
        placeholder = { Text(placeholder) },
        label = { Text(label) },
        singleLine = true,
        visualTransformation = VisualTransformation.None,
        colors = TextFieldDefaults.textFieldColors(
            unfocusedIndicatorColor = Color(0xFFDADADA),
            cursorColor = Color(0xFF222222),
            containerColor = Color.Transparent,
            unfocusedLabelColor = Color(0xFF222222),
            focusedLabelColor = Color(0xFF222222),
            unfocusedPlaceholderColor = Color(0xFF222222),
        ),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordField(
    value: String,
    onChange: (String) -> Unit,
    submit: () -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Contraseña",
    placeholder: String = "Digite su contraseña"
) {

    var isPasswordVisible by remember { mutableStateOf(false) }

    val trailingIcon = @Composable {
        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
            Icon(
                if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }


    TextField(
        value = value,
        onValueChange = onChange,
        modifier = modifier,
        trailingIcon = trailingIcon,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password
        ),
        keyboardActions = KeyboardActions(
            onDone = { submit() }
        ),
        placeholder = { Text(placeholder) },
        label = { Text(label) },
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            unfocusedIndicatorColor = Color(0xFFDADADA),
            cursorColor = Color(0xFF222222),
            containerColor = Color.Transparent,
            unfocusedLabelColor = Color(0xFF222222),
            focusedLabelColor = Color(0xFF222222),
            unfocusedPlaceholderColor = Color(0xFF222222),
        ),
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()
    )
}