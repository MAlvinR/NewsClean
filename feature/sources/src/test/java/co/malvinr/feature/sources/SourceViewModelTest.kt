package co.malvinr.feature.sources

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import co.malvinr.core.domain.model.Source
import co.malvinr.core.domain.usecase.GetSourceByCategoryUseCase
import co.malvinr.core.domain.usecase.GetSourcesUseCase
import co.malvinr.core.domain.usecase.SearchSourcesUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SourceViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val getSourceByCategoryUseCase: GetSourceByCategoryUseCase = mockk()
    private val getSourcesUseCase: GetSourcesUseCase = mockk()
    private val searchSourcesUseCase = SearchSourcesUseCase()
    
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `sourceUiState emits Content with category sources initially`() = runTest {
        val categorySlug = "tech"
        val savedStateHandle = SavedStateHandle(mapOf("category_slug" to categorySlug))
        
        val categorySources = listOf(Source("1", "Tech Blog", "url"))
        val allSources = listOf(Source("1", "Tech Blog", "url"), Source("2", "Sports", "url"))
        
        coEvery { getSourceByCategoryUseCase(categorySlug) } returns Result.success(categorySources)
        coEvery { getSourcesUseCase() } returns Result.success(allSources)

        val viewModel = SourceViewModel(
            getSourceByCategoryUseCase,
            getSourcesUseCase,
            searchSourcesUseCase,
            savedStateHandle
        )

        viewModel.sourceUiState.test {
            assertEquals(SourceUiState.Content(categorySources), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `sourceUiState emits Error when fetch fails`() = runTest {
        val categorySlug = "tech"
        val savedStateHandle = SavedStateHandle(mapOf("category_slug" to categorySlug))
        
        coEvery { getSourceByCategoryUseCase(categorySlug) } returns Result.failure(RuntimeException("Error loading"))
        coEvery { getSourcesUseCase() } returns Result.success(emptyList())

        val viewModel = SourceViewModel(
            getSourceByCategoryUseCase,
            getSourcesUseCase,
            searchSourcesUseCase,
            savedStateHandle
        )

        viewModel.sourceUiState.test {
            assertEquals(SourceUiState.Error("Error loading"), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
