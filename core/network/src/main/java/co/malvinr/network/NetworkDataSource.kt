package co.malvinr.network

import co.malvinr.network.model.ArticleResponse
import co.malvinr.network.model.SourceResponse

interface NetworkDataSource {
    suspend fun getTopHeadlines(
        country: String,
        sources: String,
        query: String,
        page: Int,
        pageSize: Int
    ): ArticleResponse
    suspend fun searchEverything(query: String): ArticleResponse
    suspend fun getSources(category: String = ""): SourceResponse
}