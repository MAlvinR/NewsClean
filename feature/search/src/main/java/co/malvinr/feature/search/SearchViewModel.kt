package co.malvinr.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.malvinr.core.domain.model.Article
import co.malvinr.core.domain.usecase.SearchNewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchNewsUseCase: SearchNewsUseCase
) : ViewModel() {
    private val _query = MutableStateFlow("")

    init {
        onQueryChanged("iran")
    }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val searchUiState: StateFlow<SearchUiState> = _query
        .debounce(300L)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            flow {
                if (query.isBlank()) {
                    emit(SearchUiState.Idle)
                } else {
                    emit(SearchUiState.Loading)
                    searchNewsUseCase(query)
                        .fold(
                            onSuccess = { emit(SearchUiState.Content(it)) },
                            onFailure = { emit(SearchUiState.Error(it.message ?: "Search failed")) }
                        )
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SearchUiState.Idle
        )

    fun onQueryChanged(query: String) {
        _query.value = query
    }
}

sealed interface SearchUiState {
    data object Idle : SearchUiState
    data object Loading : SearchUiState

    data class Content(
        val headlines: List<Article>
    ) : SearchUiState

    data class Error(val error: String) : SearchUiState
}