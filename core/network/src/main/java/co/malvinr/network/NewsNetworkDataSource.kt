package co.malvinr.network

import co.malvinr.network.api.NewsApiService
import co.malvinr.network.model.ArticleResponse
import co.malvinr.network.model.SourceResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsNetworkDataSource @Inject constructor(
    private val apiService: NewsApiService
) : NetworkDataSource {
    override suspend fun getTopHeadlines(
        country: String,
        sources: String,
        query: String,
        page: Int,
        pageSize: Int
    ): ArticleResponse =
        apiService.getTopHeadlines(country, sources, query, page, pageSize)

    override suspend fun searchEverything(query: String): ArticleResponse =
        apiService.searchEverything(query)

    override suspend fun getSourceByCategory(category: String): SourceResponse =
        apiService.getSourceByCategory(category)
}