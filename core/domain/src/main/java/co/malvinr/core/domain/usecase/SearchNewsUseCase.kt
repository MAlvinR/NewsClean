package co.malvinr.core.domain.usecase

import co.malvinr.core.domain.model.Article
import co.malvinr.core.domain.repository.ArticleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchNewsUseCase @Inject constructor(
    private val repository: ArticleRepository
) {
    operator fun invoke(query: String): Flow<Result<List<Article>>> = repository.searchArticle(query)
}