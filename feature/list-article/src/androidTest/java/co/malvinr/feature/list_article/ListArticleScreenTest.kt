package co.malvinr.feature.list_article

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import co.malvinr.core.domain.model.Article
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test

class ListArticleScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun listArticleContent_showsSourceNameInTopBar() {
        composeTestRule.setContent {
            val pagingItems = flowOf(PagingData.empty<Article>()).collectAsLazyPagingItems()
            
            ListArticleContent(
                sourceName = "MyNewsSource",
                articlePagingItems = pagingItems,
                onItemClick = {},
                onBackTapped = {}
            )
        }

        composeTestRule.onNodeWithText("Articles from MyNewsSource").assertIsDisplayed()
    }
}
