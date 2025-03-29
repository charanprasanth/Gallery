package com.xlorit.gallery.feature_auth.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.xlorit.gallery.core.data.Resource
import com.xlorit.gallery.navigation.Screens

@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    val authState by viewModel.authState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Hi there!\nRegister to Continue",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier
                .padding(top = 15.dp)
                .fillMaxWidth()
                .height(65.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth()
                .height(65.dp),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth()
                .height(65.dp),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 5.dp)
            )
        }

        when (authState) {
            is Resource.Idle -> {}
            is Resource.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            is Resource.Success -> {
                navController.navigate(Screens.GALLERY) {
                    popUpTo(Screens.REGISTER) { inclusive = true }
                }
            }

            is Resource.Error -> {
                errorMessage = (authState as Resource.Error).message ?: "Unknown error"
            }
        }

        Button(
            onClick = {
                if (email.isEmpty()) {
                    errorMessage = "Email cannot be empty"
                } else if (password.isEmpty()) {
                    errorMessage = "Password cannot be empty"
                } else if (confirmPassword.isEmpty()) {
                    errorMessage = "Confirm Password cannot be empty"
                } else if (password != confirmPassword) {
                    errorMessage = "Passwords do not match"
                } else {
                    errorMessage = ""
                    viewModel.register(email, password, "test")
                }
            },
            modifier = Modifier
                .padding(top = 15.dp)
                .fillMaxWidth()
                .height(60.dp),
        ) {
            Text("Register")
        }

        Text(
            text = buildAnnotatedString {
                append("Existing user? ")
                pushStyle(
                    SpanStyle(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline
                    )
                )
                append("Click to Login")
                pop()
            },
            modifier = Modifier
                .padding(top = 16.dp)
                .clickable {
                    navController.navigate(Screens.LOGIN) {
                        popUpTo(Screens.REGISTER) { inclusive = true }
                    }
                }
                .align(Alignment.CenterHorizontally)
        )
    }
}
