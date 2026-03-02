package co.malvinr.feature.list_article

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.malvinr.core.domain.model.Article
import co.malvinr.core.domain.usecase.GetHeadlinesBySourceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListArticleViewModel @Inject constructor(
    private val getHeadlinesBySourceUseCase: GetHeadlinesBySourceUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val source: String = checkNotNull(savedStateHandle["source_id"]) {
        "source_id navigation argument is required"
    }

    private val _listArticleState: MutableStateFlow<ListArticleUiState> = MutableStateFlow(ListArticleUiState.Loading)
    val listArticleState: StateFlow<ListArticleUiState> = _listArticleState.asStateFlow()

    init {
        fetchHeadlines()
    }

    private fun fetchHeadlines() {
        viewModelScope.launch {
            _listArticleState.value = ListArticleUiState.Loading
            Log.d("WAWAWA", "hasilnya: ${getHeadlinesBySourceUseCase(source)}")
            _listArticleState.value = getHeadlinesBySourceUseCase(source)
                .fold(
                    onSuccess = { ListArticleUiState.Content(it) },
                    onFailure = { ListArticleUiState.Error(it.message ?: "Unknown Error") }
                )
        }
    }
}

sealed interface ListArticleUiState {
    data object Loading : ListArticleUiState

    data class Content(
        val headlines: List<Article>
    ) : ListArticleUiState

    data class Error(val error: String): ListArticleUiState
}