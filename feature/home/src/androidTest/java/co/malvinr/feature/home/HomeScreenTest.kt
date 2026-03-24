package co.malvinr.feature.home

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import co.malvinr.core.domain.model.Article
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.assertEquals

class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun homeContent_idleState_showsSearchBar() {
        var queryText = ""
        
        composeTestRule.setContent {
            val pagingItems = flowOf(PagingData.empty<Article>()).collectAsLazyPagingItems()
            
            HomeContent(
                uiState = HomeUiState.Idle,
                query = "",
                articlePagingItems = pagingItems,
                onItemClick = {},
                onCategoryClick = {},
                onTextChanged = { queryText = it }
            )
        }

        val searchInputNode = composeTestRule.onNodeWithText("Search news...")
        searchInputNode.assertIsDisplayed()
        
        searchInputNode.performTextInput("Technology")
        assertEquals("Technology", queryText)
    }

    @Test
    fun homeContent_activeStateEmptyList_showsNoResultsFound() {
        composeTestRule.setContent {
            val pagingItems = flowOf(PagingData.empty<Article>()).collectAsLazyPagingItems()
            
            HomeContent(
                uiState = HomeUiState.Active,
                query = "Some query",
                articlePagingItems = pagingItems,
                onItemClick = {},
                onCategoryClick = {},
                onTextChanged = {}
            )
        }

        // It may need to wait for idle, but empty data is immediate
        composeTestRule.onNodeWithText("No results found").assertExists()
    }
}
