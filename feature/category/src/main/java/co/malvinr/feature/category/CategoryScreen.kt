package co.malvinr.feature.category

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
import co.malvinr.core.domain.model.Category

@Composable
fun CategoryScreen(
    onItemClick: (String) -> Unit,
    viewModel: CategoryViewModel = hiltViewModel()
) {
    val categoryUiState by viewModel.categoryUiState.collectAsStateWithLifecycle()

    CategoryContent(
        categoryUiState = categoryUiState,
        onItemClick = onItemClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryContent(
    categoryUiState: CategoryUiState,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("News Categories") }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            if (categoryUiState is CategoryUiState.Content) {
                CategoriesList(categories = categoryUiState.headlines, onItemClick = onItemClick)
            }
        }
    }
}

@Composable
fun CategoriesList(
    categories: List<Category>,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(
            items = categories,
            key = { category -> category.id }
        ) { category ->
            Text(
                text = category.name,

                modifier = modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .clickable(onClick = { onItemClick(category.slug) }),
                style = TextStyle(color = Color.Black)
            )
        }
    }
}