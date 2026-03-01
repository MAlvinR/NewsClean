package co.malvinr.feature.home

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.malvinr.core.domain.model.Article

@Composable
fun HomeScreen(
    onItemClick: (String) -> Unit,
    onCategoryClick: () -> Unit,
    onSearchClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val homeState by viewModel.homeState.collectAsStateWithLifecycle()

    HomeContent(
        homeUiState = homeState,
        onItemClick = onItemClick,
        onCategoryClick = onCategoryClick,
        onSearchClick = onSearchClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    homeUiState: HomeUiState,
    onItemClick: (String) -> Unit,
    onCategoryClick: () -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("News Clean") },
                actions = {
                    IconButton(onClick = onCategoryClick) {
                        Icon(
                            imageVector = Icons.Default.Category,
                            contentDescription = "Select Categories"
                        )
                    }
                    IconButton(onClick = onSearchClick) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            if (homeUiState is HomeUiState.Content) {
                ArticlesList(articles = homeUiState.headlines, onItemClick = onItemClick)
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


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun Articles() {
    val articles = listOf(
        Article("1", "title 1", "", "", "", ""),
        Article("2", "title 2", "", "", "", ""),
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("News Clean") },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            ArticlesList(articles, onItemClick = {})
        }
    }
}