package co.malvinr.feature.category

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import co.malvinr.core.domain.model.Category
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.assertEquals

class CategoryScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun contentState_showsCategoriesAndHandlesClicks() {
        val dummyCategories = listOf(
            Category(id = 1, name = "Technology", slug = "technology"),
            Category(id = 2, name = "Sports", slug = "sports")
        )
        
        var clickedSlug = ""

        composeTestRule.setContent {
            CategoryContent(
                categoryUiState = CategoryUiState.Content(dummyCategories),
                onItemClick = { clickedSlug = it },
                onBackTapped = {}
            )
        }

        composeTestRule.onNodeWithText("Technology").assertIsDisplayed()
        composeTestRule.onNodeWithText("Sports").assertIsDisplayed()

        composeTestRule.onNodeWithText("Technology").performClick()
        assertEquals("technology", clickedSlug)
    }

    @Test
    fun topBar_backButtonTriggersCallback() {
        var backTapped = false

        composeTestRule.setContent {
            CategoryContent(
                categoryUiState = CategoryUiState.Loading,
                onItemClick = {},
                onBackTapped = { backTapped = true }
            )
        }

        composeTestRule.onNodeWithContentDescription("Back").performClick()
        
        assertEquals(true, backTapped)
    }
}
