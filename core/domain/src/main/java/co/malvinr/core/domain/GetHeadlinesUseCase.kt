package co.malvinr.core.domain

import javax.inject.Inject

class GetHeadlinesUseCases @Inject constructor(
    private val repository: ArticleRepository
) {
    suspend operator fun invoke(): Result<List<Article>> = repository.getArticle()
}