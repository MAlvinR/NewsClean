package co.malvinr.feature.sources

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.malvinr.core.domain.model.Source

@Composable
fun SourceScreen(
    onItemClick: (String) -> Unit,
    viewModel: SourceViewModel = hiltViewModel()
) {
    val sourceUiState by viewModel.sourceUiState.collectAsStateWithLifecycle()

    SourceContent(
        sourceUiState = sourceUiState,
        onItemClick = onItemClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SourceContent(
    sourceUiState: SourceUiState,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("News Sources") }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            if (sourceUiState is SourceUiState.Content) {
                SourcesList(sources = sourceUiState.sources, onItemClick = onItemClick)
            }
        }
    }
}

@Composable
fun SourcesList(
    sources: List<Source>,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(
            items = sources,
            key = { source -> source.id }
        ) { source ->
            Text(
                text = source.name,

                modifier = modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .clickable(onClick = {  }),
                style = TextStyle(color = Color.Black)
            )
        }
    }
}