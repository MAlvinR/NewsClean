package co.malvinr.core.domain.repository

import co.malvinr.core.domain.model.Article

interface ArticleRepository {
    suspend fun fetchTopHeadlines(country: String, sources: String): Result<List<Article>>
    suspend fun searchArticle(query: String): Result<List<Article>>
}