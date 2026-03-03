package co.malvinr.newsclean.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = ClimateTagText,
    secondary = ScienceTagText,
    background = AppBackgroundLight,
    surface = SurfaceCardLight,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = TextPrimaryLight,
    onSurface = TextPrimaryLight,
)

private val DarkColorScheme = darkColorScheme(
    primary = ClimateTagText, // Keep accent colors consistent
    secondary = ScienceTagText,
    background = AppBackgroundDark,
    surface = SurfaceCardDark,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = TextPrimaryDark,
    onSurface = TextPrimaryDark,
)

object NewsCleanTheme {
    val colors: NewsCleanCustomColors
        @Composable
        get() = LocalNewsCleanColors.current
}

@Composable
fun NewsCleanAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+, but for this specific design, we'll disable it to keep the original palette.
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = androidx.compose.ui.platform.LocalContext.current
            if (darkTheme) androidx.compose.material3.dynamicDarkColorScheme(context) else androidx.compose.material3.dynamicLightColorScheme(
                context
            )
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val customColors = if (darkTheme) {
        NewsCleanCustomColors(
            textSecondary = TextSecondaryDark
        )
    } else {
        NewsCleanCustomColors(
            textSecondary = TextSecondaryLight
        )
    }

    CompositionLocalProvider(LocalNewsCleanColors provides customColors) {
        MaterialTheme(
            colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
            content = content
        )
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}