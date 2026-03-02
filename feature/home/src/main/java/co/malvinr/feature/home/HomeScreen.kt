package co.malvinr.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import co.malvinr.core.domain.model.Article

@Composable
fun HomeScreen(
    onItemClick: (String) -> Unit,
    onCategoryClick: () -> Unit,
    onSearchClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val articlePagingItems = viewModel.articleDataFlow.collectAsLazyPagingItems()

    HomeContent(
        uiState = uiState,
        articlePagingItems = articlePagingItems,
        onItemClick = onItemClick,
        onCategoryClick = onCategoryClick,
        onSearchClick = onSearchClick,
        onTextChanged = viewModel::onSearchQueryChanged
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    uiState: HomeUiState,
    articlePagingItems: LazyPagingItems<Article>,
    onItemClick: (String) -> Unit,
    onCategoryClick: () -> Unit,
    onSearchClick: () -> Unit,
    onTextChanged: (String) -> Unit,
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
        Column(modifier = Modifier.padding(innerPadding)) {
            SearchBar(
                hint = "Search news...",
                onTextChanged = onTextChanged,
            )
            when (uiState) {
                HomeUiState.Idle -> {
                    ArticlesList(
                        articlePagingItems = articlePagingItems,
                        onItemClick = onItemClick,
                        emptyContent = null
                    )
                }
                HomeUiState.Active -> {
                    ArticlesList(
                        articlePagingItems = articlePagingItems,
                        onItemClick = onItemClick,
                        emptyContent = {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No results found",
                                    style = TextStyle(
                                        color = Color.Gray,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SearchBar(
    hint: String,
    modifier: Modifier = Modifier,
    isEnabled: (Boolean) = true,
    onSearchClicked: () -> Unit = {},
    onTextChanged: (String) -> Unit,
) {
    var text by remember { mutableStateOf(TextFieldValue()) }
    Row(
        modifier = Modifier
            .height(56.dp)
            .fillMaxWidth()
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(8.dp))
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
            .clickable { onSearchClicked() },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BasicTextField(
            modifier = modifier
                .weight(5f)
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            value = text,
            onValueChange = {
                text = it
                onTextChanged(it.text)
            },
            enabled = isEnabled,
            textStyle = TextStyle(
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            ),
            decorationBox = { innerTextField ->
                if (text.text.isEmpty()) {
                    Text(
                        text = hint,
                        color = Color.Gray.copy(alpha = 0.5f),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
                innerTextField()
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(onSearch = { onSearchClicked() }),
            singleLine = true
        )
        Box(
            modifier = modifier
                .weight(1f)
                .size(40.dp)
                .background(color = Color.Transparent, shape = CircleShape)
                .clickable {
                    if (text.text.isNotEmpty()) {
                        text = TextFieldValue(text = "")
                        onTextChanged("")
                    }
                },
        ) {
            if (text.text.isNotEmpty()) {
                Icon(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    imageVector = Icons.Filled.Clear,
                    contentDescription = "Clear",
                    tint = Color.Black,
                )
            } else {
                Icon(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search",
                    tint = Color.Black,
                )
            }
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