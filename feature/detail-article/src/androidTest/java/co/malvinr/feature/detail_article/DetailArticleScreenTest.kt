package co.malvinr.feature.detail_article

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.assertEquals

class DetailArticleScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun topBar_backButtonTriggersCallback() {
        var backTapped = false

        composeTestRule.setContent {
            DetailArticleScreen(
                url = "https://example.com",
                onBackClick = { backTapped = true }
            )
        }

        composeTestRule.onNodeWithContentDescription("Back").performClick()
        
        assertEquals(true, backTapped)
    }
}
