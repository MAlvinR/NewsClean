package co.malvinr.network.api

import co.malvinr.network.model.ArticleResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface NewsApiService {
    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String,
        @Query("q") query: String = ""
    ): ArticleResponse

    @GET("everything")
    suspend fun searchEverything(
        @Query("q") query: String
    ): ArticleResponse
}