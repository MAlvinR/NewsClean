package co.malvinr.feature.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.malvinr.core.domain.model.Article

@Composable
fun SearchScreen(
    onItemClick: (String) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val searchState by viewModel.searchUiState.collectAsStateWithLifecycle()

    ArticleContent(searchUiState = searchState, onItemClick)
}

@Composable
fun ArticleContent(
    searchUiState: SearchUiState,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier.fillMaxSize()
    ) { innerPadding ->
        Box(modifier = modifier.padding(innerPadding)) {
            if (searchUiState is SearchUiState.Content) {
                ArticlesList(articles = searchUiState.headlines, onItemClick = onItemClick)
            }
        }
    }
}

@Composable
fun ArticlesList(
    articles: List<Article>,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(
            items = articles,
            key = { article -> article.id }
        ) { article ->
            Text(
                text = "Title: ${article.title}",

                modifier = modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .clickable(onClick = { onItemClick(article.url) }),
                style = TextStyle(color = Color.Black)
            )
        }
    }
}