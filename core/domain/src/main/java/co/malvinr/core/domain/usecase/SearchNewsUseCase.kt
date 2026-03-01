package co.malvinr.core.domain.usecase

import co.malvinr.core.domain.model.Article
import co.malvinr.core.domain.repository.ArticleRepository
import javax.inject.Inject

class SearchNewsUseCase @Inject constructor(
    private val repository: ArticleRepository
) {
    suspend operator fun invoke(query: String): Result<List<Article>> =
        repository.searchArticle(query)
}