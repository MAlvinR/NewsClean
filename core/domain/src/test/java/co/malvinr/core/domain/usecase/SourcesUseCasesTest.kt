package co.malvinr.core.domain.usecase

import co.malvinr.core.domain.model.Source
import co.malvinr.core.domain.repository.SourceRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SourcesUseCasesTest {

    private val repository: SourceRepository = mockk()

    @Test
    fun `GetSourceByCategoryUseCase should return sources from category`() = runTest {
        val useCase = GetSourceByCategoryUseCase(repository)
        val category = "tech"
        val expected = listOf(Source("1", "CNN", "url"))
        coEvery { repository.getSourceByCategory(category) } returns Result.success(expected)

        val result = useCase(category)

        assertEquals(Result.success(expected), result)
        coVerify(exactly = 1) { repository.getSourceByCategory(category) }
    }

    @Test
    fun `GetSourcesUseCase should return all sources`() = runTest {
        val useCase = GetSourcesUseCase(repository)
        val expected = listOf(Source("1", "CNN", "url"))
        coEvery { repository.getSources() } returns Result.success(expected)

        val result = useCase()

        assertEquals(Result.success(expected), result)
        coVerify(exactly = 1) { repository.getSources() }
    }

    @Test
    fun `SearchSourcesUseCase should filter sources by name ignoring case`() {
        val useCase = SearchSourcesUseCase()
        val sources = listOf(
            Source("1", "TechCrunch", "url"),
            Source("2", "The Verge", "url"),
            Source("3", "Wired Tech", "url")
        )

        val result = useCase(sources, "tech")

        assertEquals(2, result.size)
        assertEquals("TechCrunch", result[0].name)
        assertEquals("Wired Tech", result[1].name)
    }
}
