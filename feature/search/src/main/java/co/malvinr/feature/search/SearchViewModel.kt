package co.malvinr.feature.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.malvinr.core.domain.model.Article
import co.malvinr.core.domain.usecase.SearchNewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchNewsUseCase: SearchNewsUseCase
) : ViewModel() {
    private val _searchUiState: MutableStateFlow<SearchUiState> = MutableStateFlow(SearchUiState.Idle)
    val searchUiState: StateFlow<SearchUiState> = _searchUiState.asStateFlow()

    fun search(query: String) {
        viewModelScope.launch {
            _searchUiState.value = SearchUiState.Loading
            searchNewsUseCase(query)
                .map { result ->
                    Log.d("WAWAWASEARCH", "hasilnya: ${result.getOrNull()}")
                    result.fold(
                        onSuccess = { SearchUiState.Content(result.getOrNull()!!) },
                        onFailure = { SearchUiState.Error(it.message ?: "Unknown Error") }
                    )
                }
                .collect { _searchUiState.value = it }
        }
    }
}

sealed interface SearchUiState {
    data object Idle: SearchUiState
    data object Loading : SearchUiState

    data class Content(
        val headlines: List<Article>
    ) : SearchUiState

    data class Error(val error: String): SearchUiState
}