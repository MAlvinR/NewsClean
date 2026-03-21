package co.malvinr.feature.category

import app.cash.turbine.test
import co.malvinr.core.domain.model.Category
import co.malvinr.core.domain.usecase.GetCategoriesCase
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
class CategoryViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val useCase: GetCategoriesCase = mockk()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `categoryUiState emits Content when usecase is successful`() = runTest {
        val categories = listOf(Category(1, "tech", "Technology"))
        coEvery { useCase() } returns Result.success(categories)

        val viewModel = CategoryViewModel(useCase)

        viewModel.categoryUiState.test {
            assertEquals(CategoryUiState.Content(categories), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `categoryUiState emits Error when usecase fails`() = runTest {
        coEvery { useCase() } returns Result.failure(RuntimeException("Test error"))

        val viewModel = CategoryViewModel(useCase)

        viewModel.categoryUiState.test {
            assertEquals(CategoryUiState.Error("Test error"), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
