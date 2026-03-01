package co.malvinr.feature.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.malvinr.core.domain.Article
import co.malvinr.core.domain.GetHeadlinesUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getHeadlinesUseCases: GetHeadlinesUseCases
) : ViewModel() {
    private val _homeState: MutableStateFlow<HomeUiState> = MutableStateFlow(HomeUiState.Loading)
    val homeState: StateFlow<HomeUiState> = _homeState.asStateFlow()

    init {
        fetchHeadlines()
    }

    private fun fetchHeadlines() {
        viewModelScope.launch {
            _homeState.value = HomeUiState.Loading
            Log.d("WAWAWA", "hasilnya: ${getHeadlinesUseCases()}")
            _homeState.value = getHeadlinesUseCases()
                .fold(
                    onSuccess = { HomeUiState.Content(it) },
                    onFailure = { HomeUiState.Error(it.message ?: "Unknown Error") }
                )
        }
    }
}

sealed interface HomeUiState {
    data object Loading : HomeUiState

    data class Content(
        val headlines: List<Article>
    ) : HomeUiState

    data class Error(val error: String): HomeUiState
}