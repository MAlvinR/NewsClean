package co.malvinr.feature.home

import app.cash.turbine.test
import androidx.paging.PagingData
import co.malvinr.core.domain.model.Article
import co.malvinr.core.domain.usecase.GetTopHeadlinesUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val useCase: GetTopHeadlinesUseCase = mockk()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `uiState initially emits Idle when query is empty`() = runTest {
        coEvery { useCase("") } returns flowOf(PagingData.empty())
        
        val viewModel = HomeViewModel(useCase)

        viewModel.uiState.test {
            assertEquals(HomeUiState.Idle, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `uiState emits Active when query is not blank`() = runTest {
        coEvery { useCase(any()) } returns flowOf(PagingData.empty())
        
        val viewModel = HomeViewModel(useCase)

        viewModel.uiState.test {
            assertEquals(HomeUiState.Idle, awaitItem())
            
            viewModel.onSearchQueryChanged("bitcoin")
            
            assertEquals(HomeUiState.Active, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
