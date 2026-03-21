package co.malvinr.core.domain.usecase

import co.malvinr.core.domain.model.Article
import co.malvinr.core.domain.repository.ArticleRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchNewsUseCaseTest {

    private lateinit var repository: ArticleRepository
    private lateinit var searchNewsUseCase: SearchNewsUseCase

    @Before
    fun setUp() {
        repository = mockk()
        searchNewsUseCase = SearchNewsUseCase(repository)
    }

    @Test
    fun `invoke should return success when repository returns articles`() = runTest {
        // Arrange
        val query = "bitcoin"
        val mockArticles = listOf(
            Article(id = "1", title = "Bitcoin hits 100k", description = "...", thumbUrl = "", url = "", publishedAt = "")
        )
        coEvery { repository.searchArticle(query) } returns Result.success(mockArticles)

        // Act
        val result = searchNewsUseCase(query)

        // Assert
        assertEquals(Result.success(mockArticles), result)
        coVerify(exactly = 1) { repository.searchArticle(query) }
    }

    @Test
    fun `invoke should return failure when repository fails`() = runTest {
        // Arrange
        val query = "bitcoin"
        val error = RuntimeException("Network error")
        coEvery { repository.searchArticle(query) } returns Result.failure(error)

        // Act
        val result = searchNewsUseCase(query)

        // Assert
        assertEquals(Result.failure<List<Article>>(error), result)
        coVerify(exactly = 1) { repository.searchArticle(query) }
    }
}
