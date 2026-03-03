package co.malvinr.feature.list_article

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import co.malvinr.core.domain.model.Article
import coil.compose.AsyncImage

@Composable
fun ListArticleScreen(
    sourceName: String,
    onItemClick: (String) -> Unit,
    onBackTapped: () -> Unit,
    viewModel: ListArticleViewModel = hiltViewModel()
) {
    val articlePagingItems = viewModel.articleDataFlow.collectAsLazyPagingItems()

    ListArticleContent(
        sourceName = sourceName,
        articlePagingItems = articlePagingItems,
        onItemClick = onItemClick,
        onBackTapped = onBackTapped
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListArticleContent(
    sourceName: String,
    articlePagingItems: LazyPagingItems<Article>,
    onItemClick: (String) -> Unit,
    onBackTapped: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Articles from $sourceName") },
                navigationIcon = {
                    IconButton(onClick = onBackTapped) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
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
    modifier: Modifier = Modifier,
    emptyContent: (@Composable () -> Unit)? = null,
) {
    val isRefreshing = articlePagingItems.loadState.refresh is LoadState.Loading
    val isEmpty = !isRefreshing && articlePagingItems.itemCount == 0

    if (isRefreshing) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier.fillMaxSize()
        ) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        }
    }

    if (isEmpty && emptyContent != null) {
        emptyContent()
        return
    }

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
                ArticleListItem(
                    newsItem = article,
                    onItemClick = onItemClick
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

@Composable
fun ArticleListItem(
    newsItem: Article,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onItemClick(newsItem.url) },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp)
            ) {
                Text(
                    text = newsItem.title,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W600,
                    lineHeight = 24.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = newsItem.publishedAt,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 14.sp
                    )
                }
            }

            AsyncImage(
                model = newsItem.thumbUrl,
                contentDescription = "News Image",
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(20.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }
}