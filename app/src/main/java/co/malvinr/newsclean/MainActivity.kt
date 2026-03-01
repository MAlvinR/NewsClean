package co.malvinr.newsclean

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import co.malvinr.newsclean.navigation.NewsNavHost
import co.malvinr.newsclean.ui.theme.NewsCleanTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NewsCleanTheme {
                NewsNavHost()
            }
        }
    }
}