package co.malvinr.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import co.malvinr.core.domain.model.Article
import co.malvinr.core.domain.usecase.GetTopHeadlinesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTopHeadlinesUseCase: GetTopHeadlinesUseCase,
) : ViewModel() {
    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    val uiState: StateFlow<HomeUiState> = _query
        .map { if (it.isBlank()) HomeUiState.Idle else HomeUiState.Active }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HomeUiState.Idle)

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val articleDataFlow: Flow<PagingData<Article>> = _query
        .debounce(500L)
        .distinctUntilChanged()
        .flatMapLatest { query -> getTopHeadlinesUseCase(query) }
        .cachedIn(viewModelScope)

    fun onSearchQueryChanged(query: String) {
        _query.value = query
    }
}

sealed interface HomeUiState {
    data object Idle : HomeUiState
    data object Active : HomeUiState
}