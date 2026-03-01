package co.malvinr.core.domain.repository

import co.malvinr.core.domain.model.Article

interface ArticleRepository {
    suspend fun getArticle(): Result<List<Article>>
    suspend fun searchArticle(query: String): Result<List<Article>>
}