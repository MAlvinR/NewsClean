package co.malvinr.network.api

import co.malvinr.network.model.ArticleResponse
import co.malvinr.network.model.SourceResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface NewsApiService {
    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String,
        @Query("sources") sources: String = "",
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int,
    ): ArticleResponse

    @GET("everything")
    suspend fun searchEverything(
        @Query("q") query: String
    ): ArticleResponse

    @GET("sources")
    suspend fun getSourceByCategory(
        @Query("category") query: String
    ): SourceResponse
}