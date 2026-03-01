package co.malvinr.network.model

import kotlinx.serialization.Serializable

@Serializable
data class CategoryResponse(val id: Int, val slug: String, val name: String)