package co.malvinr.core.domain

import kotlinx.coroutines.flow.Flow

interface ArticleRepository {
//    fun getArticle(): Flow<Result<List<Article>>>
    suspend fun getArticle(): Result<List<Article>>
}