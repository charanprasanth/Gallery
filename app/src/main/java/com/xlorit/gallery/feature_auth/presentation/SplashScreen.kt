package com.xlorit.gallery.feature_auth.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.xlorit.gallery.navigation.Screens
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        delay(1000)
        viewModel.checkLoggedIn().let {
            if (it) {
                navController.navigate(Screens.GALLERY) {
                    popUpTo(Screens.SPLASH) { inclusive = true }
                }
            } else {
                navController.navigate(Screens.LOGIN) {
                    popUpTo(Screens.SPLASH) { inclusive = true }
                }
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Welcome!",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}
