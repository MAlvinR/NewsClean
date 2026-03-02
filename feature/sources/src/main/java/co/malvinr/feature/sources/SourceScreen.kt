package co.malvinr.feature.sources

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import co.malvinr.core.domain.model.Source

@Composable
fun SourceScreen(
    onItemClick: (String) -> Unit,
    viewModel: SourceViewModel = hiltViewModel()
) {
    val sourceUiState by viewModel.sourceUiState.collectAsStateWithLifecycle()

    SourceContent(
        sourceUiState = sourceUiState,
        onSearchQueryChanged = viewModel::onSearchQueryChanged,
        onItemClick = onItemClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SourceContent(
    sourceUiState: SourceUiState,
    onSearchQueryChanged: (String) -> Unit,
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
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            SearchBar(
                hint = "Search sources...",
                onTextChanged = onSearchQueryChanged,
                modifier = Modifier.padding(16.dp)
            )
            
            Box(modifier = Modifier.weight(1f)) {
                when (sourceUiState) {
                    is SourceUiState.Loading -> {
                        // Potential Loading Indicator
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
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
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
                    .clickable(onClick = { onItemClick(source.id) }),
                style = TextStyle(color = Color.Black)
            )
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