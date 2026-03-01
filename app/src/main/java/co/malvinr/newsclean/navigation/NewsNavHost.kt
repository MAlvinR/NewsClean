package co.malvinr.newsclean.navigation

import android.net.Uri
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import co.malvinr.feature.detail_article.DetailArticleScreen
import co.malvinr.feature.home.HomeScreen
import co.malvinr.feature.search.SearchScreen

@Composable
fun NewsNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppDestinations.HOME
    ) {
        composable(route = AppDestinations.HOME) {
            HomeScreen(
                onItemClick = { articleUrl ->
                    val encodedUrl = Uri.encode(articleUrl)
                    navController.navigate("${AppDestinations.DETAIL}/$encodedUrl")
                },
                onSearchClick = {
                    navController.navigate(AppDestinations.SEARCH)
                }
            )
        }
        composable(route = AppDestinations.SEARCH) {
            SearchScreen(
                onItemClick = { articleUrl ->
                    val encodedUrl = Uri.encode(articleUrl)
                    navController.navigate("${AppDestinations.DETAIL}/$encodedUrl")
                },
            )
        }
        composable(
            route = "${AppDestinations.DETAIL}/{${AppDestinations.Args.NEWS_URL}}",
            arguments = listOf(
                navArgument(AppDestinations.Args.NEWS_URL) { defaultValue = "" }
            ),
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up)
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down)
            }
        ) { backStackEntry ->
            val url = backStackEntry.arguments?.getString(AppDestinations.Args.NEWS_URL) ?: ""
            DetailArticleScreen(
                url = url,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}

object AppDestinations {
    const val HOME = "home"
    const val DETAIL = "detail"
    const val SEARCH = "search"

    object Args {
        const val NEWS_URL = "url"
    }
}