package co.malvinr.data.repository

import co.malvinr.network.NetworkDataSource
import co.malvinr.network.model.ArticleResponse
import co.malvinr.network.model.ArticlesListResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ArticleRepositoryImplTest {

    private val networkDataSource: NetworkDataSource = mockk()
    private val repository = ArticleRepositoryImpl(networkDataSource)

    @Test
    fun `searchArticle returns success with mapped articles`() = runTest {
        val query = "tech"
        val mockResponse = ArticleResponse(
            totalResults = 1,
            articles = listOf(
                ArticlesListResponse(
                    title = "Tech News",
                    description = "Description",
                    urlToImage = "image_url",
                    url = "article_url",
                    publishedAt = "2023-01-01T12:00:00Z"
                )
            )
        )
        coEvery { networkDataSource.searchEverything(query) } returns mockResponse

        val result = repository.searchArticle(query)

        assertTrue(result.isSuccess)
        val articles = result.getOrNull()
        assertEquals(1, articles?.size)
        assertEquals("Tech News", articles?.first()?.title)
        
        coVerify(exactly = 1) { networkDataSource.searchEverything(query) }
    }

    @Test
    fun `searchArticle returns failure when network exception occurs`() = runTest {
        val query = "tech"
        val error = RuntimeException("Network Error")
        coEvery { networkDataSource.searchEverything(query) } throws error

        val result = repository.searchArticle(query)

        assertTrue(result.isFailure)
        assertEquals(error, result.exceptionOrNull())
    }
}
