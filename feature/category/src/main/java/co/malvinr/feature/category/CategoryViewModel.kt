package co.malvinr.feature.category

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.malvinr.core.domain.model.Category
import co.malvinr.core.domain.usecase.GetCategoriesCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesCase
) : ViewModel() {
    private val _categoryUiState: MutableStateFlow<CategoryUiState> = MutableStateFlow(CategoryUiState.Loading)
    val categoryUiState: StateFlow<CategoryUiState> = _categoryUiState.asStateFlow()

    init {
        fetchCategories()
    }

    private fun fetchCategories() {
        viewModelScope.launch {
            _categoryUiState.value = CategoryUiState.Loading
            Log.d("WAWAWA", "hasilnya: ${getCategoriesUseCase()}")
            _categoryUiState.value = getCategoriesUseCase()
                .fold(
                    onSuccess = { CategoryUiState.Content(it) },
                    onFailure = { CategoryUiState.Error(it.message ?: "Unknown Error") }
                )
        }
    }
}

sealed interface CategoryUiState {
    data object Loading : CategoryUiState

    data class Content(
        val headlines: List<Category>
    ) : CategoryUiState

    data class Error(val error: String): CategoryUiState
}