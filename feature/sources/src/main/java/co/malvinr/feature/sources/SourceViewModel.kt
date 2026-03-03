package co.malvinr.feature.sources

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.malvinr.core.domain.model.Source
import co.malvinr.core.domain.usecase.GetSourceByCategoryUseCase
import co.malvinr.core.domain.usecase.GetSourcesUseCase
import co.malvinr.core.domain.usecase.SearchSourcesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SourceViewModel @Inject constructor(
    private val getSourceByCategoryUseCase: GetSourceByCategoryUseCase,
    private val getSourcesUseCase: GetSourcesUseCase,
    private val searchSourcesUseCase: SearchSourcesUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val categorySlug: String = checkNotNull(savedStateHandle["category_slug"]) {
        "category_slug navigation argument is required"
    }

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _sourcesByCategoryResults: MutableStateFlow<List<Source>> =
        MutableStateFlow(emptyList())
    private val _rawSources: MutableStateFlow<List<Source>> = MutableStateFlow(emptyList())

    private val _error: MutableStateFlow<String?> = MutableStateFlow(null)
    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(true)

    val sourceUiState: StateFlow<SourceUiState> = combine(
        _sourcesByCategoryResults,
        _rawSources,
        _searchQuery,
        _isLoading,
        _error
    ) { sourcesByCategoryResults, rawSources, query, loading, error ->
        when {
            error != null -> SourceUiState.Error(error)
            loading -> SourceUiState.Loading
            else -> {
                val sourceResults =
                    if (query.isNotBlank())
                        searchSourcesUseCase(rawSources, query)
                    else
                        sourcesByCategoryResults

                SourceUiState.Content(sourceResults)
            }
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        SourceUiState.Loading
    )

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _isLoading.value = true

            val categoryDeferred = getSourceByCategoryUseCase(categorySlug)
            val allSourcesDeferred = getSourcesUseCase()

            categoryDeferred
                .onSuccess { _sourcesByCategoryResults.value = it }
                .onFailure { _error.value = it.message ?: "Unknown Error" }

            allSourcesDeferred
                .onSuccess { _rawSources.value = it }
                .onFailure {
                    _error.value = it.message ?: "Unknown Error"
                }

            _isLoading.value = false
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }
}

sealed interface SourceUiState {
    data object Loading : SourceUiState

    data class Content(val sources: List<Source>) : SourceUiState

    data class Error(val error: String) : SourceUiState
}