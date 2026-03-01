package co.malvinr.core.domain.usecase

import co.malvinr.core.domain.model.Article
import co.malvinr.core.domain.repository.ArticleRepository
import javax.inject.Inject

class GetHeadlinesUseCases @Inject constructor(
    private val repository: ArticleRepository
) {
    suspend operator fun invoke(): Result<List<Article>> = repository.getArticle()
}