package co.malvinr.data.repository

import co.malvinr.core.domain.Article
import co.malvinr.core.domain.ArticleRepository
import co.malvinr.data.mapper.toDomainList
import co.malvinr.network.api.NewsApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticleRepositoryImpl @Inject constructor(
    private val apiService: NewsApiService
) : ArticleRepository {

    override suspend fun getArticle(): Result<List<Article>> =
        runCatching {
            apiService.getTopHeadlines().articles.toDomainList()
        }
}