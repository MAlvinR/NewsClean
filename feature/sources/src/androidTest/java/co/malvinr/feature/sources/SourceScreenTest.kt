package co.malvinr.feature.sources

import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import co.malvinr.core.domain.model.Source
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.assertEquals

class SourceScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loadingState_showsProgressIndicator() {
        composeTestRule.setContent {
            SourceContent(
                sourceUiState = SourceUiState.Loading,
                query = "",
                onSearchQueryChanged = {},
                onItemClick = {},
                onBackTapped = {}
            )
        }
      composeTestRule.onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate)).assertIsDisplayed()
    }

    @Test
    fun emptyState_showsNoSourcesFoundText() {
        composeTestRule.setContent {
            SourceContent(
                sourceUiState = SourceUiState.Content(emptyList()),
                query = "",
                onSearchQueryChanged = {},
                onItemClick = {},
                onBackTapped = {}
            )
        }

        composeTestRule.onNodeWithText("No sources found").assertIsDisplayed()
    }

    @Test
    fun errorState_showsErrorMessage() {
        val errorMessage = "Network Error"
        composeTestRule.setContent {
            SourceContent(
                sourceUiState = SourceUiState.Error(errorMessage),
                query = "",
                onSearchQueryChanged = {},
                onItemClick = {},
                onBackTapped = {}
            )
        }

        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
    }

    @Test
    fun contentState_showsListOfSourcesAndHandlesClicks() {
        val dummySources = listOf(
            Source(id = "1", name = "BBC News", url = "def"),
            Source(id = "2", name = "CNN", url = "def")
        )
        
        var clickedSource: Source? = null

        composeTestRule.setContent {
            SourceContent(
                sourceUiState = SourceUiState.Content(dummySources),
                query = "",
                onSearchQueryChanged = {},
                onItemClick = { clickedSource = it },
                onBackTapped = {}
            )
        }

        composeTestRule.onNodeWithText("BBC News").assertIsDisplayed()
        composeTestRule.onNodeWithText("CNN").assertIsDisplayed()

        // Perform click
        composeTestRule.onNodeWithText("BBC News").performClick()
        
        assertEquals(dummySources[0], clickedSource)
    }

    @Test
    fun searchBar_inputUpdatesQuery() {
        var searchQuery = ""

        composeTestRule.setContent {
            SourceContent(
                sourceUiState = SourceUiState.Content(emptyList()),
                query = "",
                onSearchQueryChanged = { searchQuery = it },
                onItemClick = {},
                onBackTapped = {}
            )
        }

        val searchInputNode = composeTestRule.onNodeWithText("Search news sources...")
        searchInputNode.assertIsDisplayed()
        
        // Ensure editable text behavior
        searchInputNode.performTextInput("Technology")

        assertEquals("Technology", searchQuery)
    }

    @Test
    fun topBar_backButtonTriggersCallback() {
        var backTapped = false

        composeTestRule.setContent {
            SourceContent(
                sourceUiState = SourceUiState.Content(emptyList()),
                query = "",
                onSearchQueryChanged = {},
                onItemClick = {},
                onBackTapped = { backTapped = true }
            )
        }

        composeTestRule.onNodeWithContentDescription("Back").performClick()
        
        assertEquals(true, backTapped)
    }
}
