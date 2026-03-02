package co.malvinr.core.domain.usecase

import androidx.paging.PagingData
import co.malvinr.core.domain.model.Article
import co.malvinr.core.domain.repository.ArticleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

const val COUNTRY_US = "us"
const val EMPTY_STRING = ""

class GetTopHeadlinesUseCase @Inject constructor(
    private val repository: ArticleRepository
) {
    operator fun invoke(query: String = ""): Flow<PagingData<Article>> =
        repository.fetchTopHeadlines(COUNTRY_US, EMPTY_STRING, query)
}

class GetHeadlinesBySourceUseCase @Inject constructor(
    private val repository: ArticleRepository
) {
    operator fun invoke(source: String, query: String = ""): Flow<PagingData<Article>> =
        repository.fetchTopHeadlines(EMPTY_STRING, source, query)
}