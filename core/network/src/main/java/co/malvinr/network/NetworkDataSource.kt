package co.malvinr.network

import co.malvinr.network.model.ArticleResponse
import co.malvinr.network.model.SourceResponse

interface NetworkDataSource {
    suspend fun getTopHeadlines(country: String, sources: String): ArticleResponse
    suspend fun searchEverything(query: String): ArticleResponse
    suspend fun getSourceByCategory(category: String): SourceResponse
}