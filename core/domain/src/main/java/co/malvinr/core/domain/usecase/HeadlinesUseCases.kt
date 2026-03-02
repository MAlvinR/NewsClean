package co.malvinr.core.domain.usecase

import co.malvinr.core.domain.model.Article
import co.malvinr.core.domain.repository.ArticleRepository
import javax.inject.Inject

const val COUNTRY_US = "us"
const val EMPTY_STRING = ""

class GetTopHeadlinesUseCase @Inject constructor(
    private val repository: ArticleRepository
) {
    suspend operator fun invoke(): Result<List<Article>> =
        repository.fetchTopHeadlines(COUNTRY_US, EMPTY_STRING)
}

class GetHeadlinesBySourceUseCase @Inject constructor(
    private val repository: ArticleRepository
) {
    suspend operator fun invoke(source: String): Result<List<Article>> =
        repository.fetchTopHeadlines(EMPTY_STRING, source)
}