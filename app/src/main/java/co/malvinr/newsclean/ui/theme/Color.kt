package co.malvinr.newsclean.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// Backgrounds
val AppBackgroundLight = Color(0xFFF7F8FC) // The very subtle grey background behind the list
val SurfaceCardLight = Color(0xFFFFFFFF)  // The white card background

// Dark Mode Backgrounds (Generated based on standard practices)
val AppBackgroundDark = Color(0xFF121212)
val SurfaceCardDark = Color(0xFF1E1E1E)

// --- Accent Colors from the Tags ---

// "Science" Tag (Yellow/Gold)
val ScienceTagText = Color(0xFFD9B062) // The gold text
val ScienceTagBackground = Color(0xFFFFF7E6) // The very pale yellow background

// "Climate" Tag (Blue/Purple)
val ClimateTagText = Color(0xFF6B7FD7) // The purple-blue text
val ClimateTagBackground = Color(0xFFEDF0FB) // The very pale blue background

// --- Core Text Colors ---

val TextPrimaryLight = Color(0xFF1A1A1A) // Near-black for titles
val TextSecondaryLight = Color(0xFF8A8A8F) // Medium grey for dates

// Dark Mode Text Colors
val TextPrimaryDark = Color(0xFFE0E0E0) // Light grey for dark titles
val TextSecondaryDark = Color(0xFFA0A0A0) // Medium-dark grey for dates

data class NewsCleanCustomColors(
    val textSecondary: Color
)

val LocalNewsCleanColors = staticCompositionLocalOf {
    NewsCleanCustomColors(
        textSecondary = Color.Unspecified
    )
}