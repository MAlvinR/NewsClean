package co.malvinr.feature.sources

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.malvinr.core.domain.model.Category
import co.malvinr.core.domain.model.Source
import co.malvinr.core.domain.usecase.GetCategoriesCase
import co.malvinr.core.domain.usecase.GetSourceByCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SourceViewModel @Inject constructor(
    private val getSourceByCategoryUseCase: GetSourceByCategoryUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val categorySlug: String = checkNotNull(savedStateHandle["category_slug"]) {
        "category_slug navigation argument is required"
    }

    private val _sourceUiState: MutableStateFlow<SourceUiState> = MutableStateFlow(SourceUiState.Loading)
    val sourceUiState: StateFlow<SourceUiState> = _sourceUiState.asStateFlow()

    init {
        getSourceByCategory()
    }

    private fun getSourceByCategory() {
        viewModelScope.launch {
            _sourceUiState.value = SourceUiState.Loading
            Log.d("WAWAWASOURCE", "hasilnya: ${getSourceByCategoryUseCase(categorySlug)}")
            _sourceUiState.value = getSourceByCategoryUseCase(categorySlug)
                .fold(
                    onSuccess = { SourceUiState.Content(it) },
                    onFailure = { SourceUiState.Error(it.message ?: "Unknown Error") }
                )
        }
    }
}

sealed interface SourceUiState {
    data object Loading : SourceUiState

    data class Content(
        val sources: List<Source>
    ) : SourceUiState

    data class Error(val error: String): SourceUiState
}