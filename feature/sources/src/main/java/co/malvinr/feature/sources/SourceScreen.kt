package co.malvinr.feature.sources

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.malvinr.core.domain.model.Source

@Composable
fun SourceScreen(
    onItemClick: (Source) -> Unit,
    onBackTapped: () -> Unit,
    viewModel: SourceViewModel = hiltViewModel()
) {
    val sourceUiState by viewModel.sourceUiState.collectAsStateWithLifecycle()
    val query by viewModel.searchQuery.collectAsStateWithLifecycle()

    SourceContent(
        sourceUiState = sourceUiState,
        query = query,
        onSearchQueryChanged = viewModel::onSearchQueryChanged,
        onItemClick = onItemClick,
        onBackTapped = onBackTapped
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SourceContent(
    sourceUiState: SourceUiState,
    query: String,
    onSearchQueryChanged: (String) -> Unit,
    onItemClick: (Source) -> Unit,
    onBackTapped: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("News Sources") },
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
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            SourceSearchBar(
                query = query,
                onTextChanged = onSearchQueryChanged,
                modifier = Modifier.padding(16.dp)
            )

            Box(modifier = Modifier.weight(1f)) {
                when (sourceUiState) {
                    is SourceUiState.Loading -> {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = modifier.fillMaxSize()
                        ) {
                            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                        }
                    }

                    is SourceUiState.Content -> {
                        if (sourceUiState.sources.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No sources found",
                                    style = TextStyle(
                                        color = Color.Gray,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                )
                            }
                        } else {
                            SourcesList(sources = sourceUiState.sources, onItemClick = onItemClick)
                        }
                    }

                    is SourceUiState.Error -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = sourceUiState.error, color = Color.Red)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SourcesList(
    sources: List<Source>,
    onItemClick: (Source) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = sources,
            key = { source -> source.id }
        ) { source ->
            SourceItem(
                source = source,
                onItemClick = onItemClick,
                modifier = modifier
            )
        }
    }
}

@Composable
fun SourceSearchBar(
    query: String,
    onTextChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Search news sources..."
) {
    TextField(
        value = query,
        onValueChange = onTextChanged,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .heightIn(min = 56.dp)
            .clip(RoundedCornerShape(28.dp)),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            disabledContainerColor = MaterialTheme.colorScheme.surface,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = MaterialTheme.colorScheme.primary
        ),
        placeholder = {
            Text(
                text = placeholder,
                fontSize = 16.sp
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon",
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
            )
        },
        singleLine = true,
        shape = RoundedCornerShape(28.dp)
    )
}

@Composable
fun SourceItem(
    source: Source,
    onItemClick: (Source) -> Unit,
    modifier: Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable { onItemClick(source) },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Text(
            text = source.name,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            modifier = modifier.padding(16.dp)
        )
    }
}