package co.malvinr.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SourceResponse(
    @SerialName("sources") val articles: List<SourceListResponse>
)

@Serializable
data class SourceListResponse(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("url") val url: String,
    @SerialName("category") val category: String
)