package com.xlorit.gallery.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.xlorit.gallery.feature_auth.presentation.LoginScreen
import com.xlorit.gallery.feature_auth.presentation.RegisterScreen
import com.xlorit.gallery.feature_auth.presentation.SplashScreen
import com.xlorit.gallery.feature_gallery.presentation.GalleryScreen

object Screens {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val GALLERY = "gallery"
}

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screens.SPLASH
    ) {
        composable(Screens.SPLASH) {
            SplashScreen(navController = navController)
        }
        composable(Screens.LOGIN) {
            LoginScreen(navController = navController)
        }
        composable(Screens.REGISTER) {
            RegisterScreen(navController = navController)
        }
        composable(Screens.GALLERY) {
            GalleryScreen(navController = navController)
        }
    }
}