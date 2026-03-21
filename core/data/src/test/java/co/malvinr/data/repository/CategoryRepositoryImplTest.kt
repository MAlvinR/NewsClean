package co.malvinr.data.repository

import co.malvinr.network.JsonAssetDataSource
import co.malvinr.network.model.CategoryResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CategoryRepositoryImplTest {

    private val jsonAssetDataSource: JsonAssetDataSource = mockk()
    private val repository = CategoryRepositoryImpl(jsonAssetDataSource)

    @Test
    fun `fetchCategories returns success with mapped categories`() = runTest {
        val mockResponse = listOf(
            CategoryResponse(id = 1, slug = "tech", name = "Technology")
        )
        coEvery { jsonAssetDataSource.getCategories() } returns mockResponse

        val result = repository.fetchCategories()

        assertTrue(result.isSuccess)
        val categories = result.getOrNull()
        assertEquals(1, categories?.size)
        assertEquals("Technology", categories?.first()?.name)
        
        coVerify(exactly = 1) { jsonAssetDataSource.getCategories() }
    }

    @Test
    fun `fetchCategories returns failure when loading fails`() = runTest {
        val error = RuntimeException("File not found")
        coEvery { jsonAssetDataSource.getCategories() } throws error

        val result = repository.fetchCategories()

        assertTrue(result.isFailure)
        assertEquals(error, result.exceptionOrNull())
    }
}
