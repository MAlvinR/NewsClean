package co.malvinr.core.domain.repository

import androidx.paging.PagingData
import co.malvinr.core.domain.model.Article
import kotlinx.coroutines.flow.Flow

interface ArticleRepository {
    fun fetchTopHeadlines(country: String, sources: String): Flow<PagingData<Article>>
    suspend fun searchArticle(query: String): Result<List<Article>>
}