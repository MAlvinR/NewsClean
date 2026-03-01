package co.malvinr.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArticleResponse(
    @SerialName("articles") val articles: List<ArticlesListResponse>
)

@Serializable
data class ArticlesListResponse(
    @SerialName("title") val title: String = "",
    @SerialName("description") val description: String = "",
    @SerialName("urlToImage") val urlToImage: String = "",
    @SerialName("url") val url: String = "",
    @SerialName("publishedAt") val publishedAt: String = ""
)