package co.malvinr.core.domain.usecase

import co.malvinr.core.domain.model.Category
import co.malvinr.core.domain.repository.CategoryRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetCategoriesCaseTest {

    private lateinit var repository: CategoryRepository
    private lateinit var getCategoriesCase: GetCategoriesCase

    @Before
    fun setUp() {
        repository = mockk()
        getCategoriesCase = GetCategoriesCase(repository)
    }

    @Test
    fun `invoke should return success from repository`() = runTest {
        // Arrange
        val mockCategories = listOf(
            Category(1, "business", "Business"),
            Category(2, "tech", "Technology")
        )
        coEvery { repository.fetchCategories() } returns Result.success(mockCategories)

        // Act
        val result = getCategoriesCase()

        // Assert
        assertEquals(Result.success(mockCategories), result)
        coVerify(exactly = 1) { repository.fetchCategories() }
    }

    @Test
    fun `invoke should return failure when repository fails`() = runTest {
        // Arrange
        val error = RuntimeException("Network error")
        coEvery { repository.fetchCategories() } returns Result.failure(error)

        // Act
        val result = getCategoriesCase()

        // Assert
        assertEquals(Result.failure<List<Category>>(error), result)
        coVerify(exactly = 1) { repository.fetchCategories() }
    }
}
