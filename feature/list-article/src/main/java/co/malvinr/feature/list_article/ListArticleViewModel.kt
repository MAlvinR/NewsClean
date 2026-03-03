package co.malvinr.feature.list_article

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import co.malvinr.core.domain.model.Article
import co.malvinr.core.domain.usecase.GetHeadlinesBySourceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ListArticleViewModel @Inject constructor(
    getHeadlinesBySourceUseCase: GetHeadlinesBySourceUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val source: String = checkNotNull(savedStateHandle["source_id"]) {
        "source_id navigation argument is required"
    }

    val articleDataFlow: Flow<PagingData<Article>> =
        getHeadlinesBySourceUseCase(source).cachedIn(viewModelScope)
}

sealed interface ListArticleUiState {
    data object Loading : ListArticleUiState

    data class Content(
        val headlines: List<Article>
    ) : ListArticleUiState

    data class Error(val error: String) : ListArticleUiState
}