package co.malvinr.network.api

import co.malvinr.network.model.ArticleResponse
import retrofit2.http.GET


interface NewsApiService {
    @GET("top-headlines?country=us")
    suspend fun getTopHeadlines(): ArticleResponse
}