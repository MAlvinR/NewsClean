package co.malvinr.core.domain.repository

import co.malvinr.core.domain.model.Article
import kotlinx.coroutines.flow.Flow

interface ArticleRepository {
//    fun getArticle(): Flow<Result<List<Article>>>
    suspend fun getArticle(): Result<List<Article>>
    fun searchArticle(query: String): Flow<Result<List<Article>>>
}