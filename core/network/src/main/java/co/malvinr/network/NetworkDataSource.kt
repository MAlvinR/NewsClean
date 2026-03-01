package co.malvinr.network

import co.malvinr.network.model.ArticleResponse

interface NetworkDataSource {
    suspend fun getTopHeadlines(): ArticleResponse
    suspend fun searchEverything(query: String): ArticleResponse
}