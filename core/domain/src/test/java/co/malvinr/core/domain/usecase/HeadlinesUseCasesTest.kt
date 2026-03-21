package co.malvinr.core.domain.usecase

import androidx.paging.PagingData
import co.malvinr.core.domain.repository.ArticleRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HeadlinesUseCasesTest {

    private val repository: ArticleRepository = mockk()

    @Test
    fun `GetTopHeadlinesUseCase should fetch top headlines from repository`() = runTest {
        val useCase = GetTopHeadlinesUseCase(repository)
        val query = "tech"
        coEvery { repository.fetchTopHeadlines(COUNTRY_US, EMPTY_STRING, query) } returns flowOf(PagingData.empty())

        val result = useCase(query).first()
        
        assertNotNull(result)
        coVerify(exactly = 1) { repository.fetchTopHeadlines(COUNTRY_US, EMPTY_STRING, query) }
    }

    @Test
    fun `GetHeadlinesBySourceUseCase should fetch from repository by source`() = runTest {
        val useCase = GetHeadlinesBySourceUseCase(repository)
        val source = "cnn"
        val query = "news"
        coEvery { repository.fetchTopHeadlines(EMPTY_STRING, source, query) } returns flowOf(PagingData.empty())

        val result = useCase(source, query).first()
        
        assertNotNull(result)
        coVerify(exactly = 1) { repository.fetchTopHeadlines(EMPTY_STRING, source, query) }
    }
}
