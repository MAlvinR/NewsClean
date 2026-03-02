package co.malvinr.feature.list_article

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import co.malvinr.core.domain.model.Article

@Composable
fun ListArticleScreen(
    onItemClick: (String) -> Unit,
    viewModel: ListArticleViewModel = hiltViewModel()
) {
    val articlePagingItems = viewModel.articleDataFlow.collectAsLazyPagingItems()

    ListArticleContent(
        articlePagingItems = articlePagingItems,
        onItemClick = onItemClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListArticleContent(
    articlePagingItems: LazyPagingItems<Article>,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("List Article") },
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            ArticlesList(articlePagingItems = articlePagingItems, onItemClick = onItemClick)
        }
    }
}

@Composable
fun ArticlesList(
    articlePagingItems: LazyPagingItems<Article>,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(
            count = articlePagingItems.itemCount,
            key = articlePagingItems.itemKey { it.id }
        ) { index ->
            val article = articlePagingItems[index]
            if (article != null) {
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

        item {
            when (articlePagingItems.loadState.append) {
                is LoadState.Loading -> {
                    Box(modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                    }
                }

                is LoadState.Error -> {}
                is LoadState.NotLoading -> Unit
            }
        }
    }
}