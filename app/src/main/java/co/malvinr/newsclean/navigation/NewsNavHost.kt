package co.malvinr.newsclean.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import co.malvinr.feature.home.HomeScreen

@Composable
fun NewsNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppDestinations.HOME
    ) {
        composable(route = AppDestinations.HOME) {
            HomeScreen()
        }
    }
}

object AppDestinations {
    const val HOME  = "home"
}